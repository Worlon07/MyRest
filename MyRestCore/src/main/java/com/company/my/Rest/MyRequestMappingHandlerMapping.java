package com.company.my.rest;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * The mapping for MyRequestMapping annotation
 */
@Component
public class MyRequestMappingHandlerMapping extends ApplicationObjectSupport implements InitializingBean {

    private final List<MyMappingRegistry> myMappingRegistries = new ArrayList<>();
    /**
     * List of the URL without conditions
     */
    private final Map<String, MyMappingRegistry> myStrictMappingRegistries = new HashMap<>();

    /**
     * List of the URL with conditions
     */
    private final Map<PathCondition[], MyMappingRegistry> myConditionsMappingRegistries = new HashMap<>();

    private void initHandlerMethods() {
        final String[] beanNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(getApplicationContext(), Object.class);

        for (final String beanName : beanNames) {
            Class<?> beanType = null;
            try {
                beanType = getApplicationContext().getType(beanName);
            } catch (Throwable ex) {
                logger.error("Error in initHandler", ex);
            }
            if (beanType != null && isHandler(beanType)) {
                detectHandlerMethods(beanName);
            }
        }
    }

    /**
     * Detects handler methods at initialization.
     */
    @Override
    public void afterPropertiesSet() {
        initHandlerMethods();
    }

    private boolean isHandler(Class<?> beanType) {
        return (AnnotatedElementUtils.hasAnnotation(beanType, MyRestController.class) ||
                AnnotatedElementUtils.hasAnnotation(beanType, MyRequestMapping.class));
    }

    private void detectHandlerMethods(final Object handler) {
        Class<?> handlerType = (handler instanceof String ?
                getApplicationContext().getType((String) handler) : handler.getClass());
        final Class<?> userType = ClassUtils.getUserClass(handlerType);

        Map<Method, MyRequestMappingInfo> methods = MethodIntrospector.selectMethods(userType,
                new MethodIntrospector.MetadataLookup<MyRequestMappingInfo>() {
                    @Override
                    public MyRequestMappingInfo inspect(Method method) {
                        try {
                            return getMappingForMethod(method, userType);
                        } catch (Throwable ex) {
                            throw new IllegalStateException("Invalid mapping on handler class [" +
                                    userType.getName() + "]: " + method, ex);
                        }
                    }
                });

        logger.debug(methods.size() + " request handler methods found on " + userType + ": " + methods);
        for (final Map.Entry<Method, MyRequestMappingInfo> entry : methods.entrySet()) {
            Method invocableMethod = AopUtils.selectInvocableMethod(entry.getKey(), userType);
            MyRequestMappingInfo mapping = entry.getValue();
            registerHandlerMethod(handler, invocableMethod, mapping);
        }
    }

    private void registerHandlerMethod(final Object handler, final Method invocableMethod, final MyRequestMappingInfo mapping) {
        final MyMappingRegistry mr = new MyMappingRegistry(handler, invocableMethod, mapping);
        myMappingRegistries.add(mr);
        for (final String path : mapping.getPaths()) {
            if (path.contains("{")) {
                myConditionsMappingRegistries.put(PathCondition.getPathConditions(path), mr);
            } else {
                myStrictMappingRegistries.put(path, mr);
            }
        }
    }

    private MyRequestMappingInfo getMappingForMethod(final Method method, Class<?> handlerType) {
        MyRequestMappingInfo info = createRequestMappingInfo(method);
        if (info != null) {
            MyRequestMappingInfo typeInfo = createRequestMappingInfo(handlerType);
            if (typeInfo != null) {
                info = typeInfo.combine(info);
            }
        }
        return info;
    }

    private MyRequestMappingInfo createRequestMappingInfo(final AnnotatedElement element) {
        MyRequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(element, MyRequestMapping.class);
        return (requestMapping != null ? new MyRequestMappingInfo(requestMapping) : null);
    }

    MyMappingRegistry getHandlerInternal(final HttpServletRequest request, final Map<String, String> values) {
        final String lookupPath = request.getRequestURI();
        logger.debug("Looking up handler method for path " + lookupPath);
        MyMappingRegistry result = myStrictMappingRegistries.get(lookupPath);

        if (result == null) {
            final String[] splitPath = lookupPath.split("/");
            final Optional<Map.Entry<PathCondition[], MyMappingRegistry>> entry =
                    myConditionsMappingRegistries.entrySet().stream()
                            .filter(e -> PathCondition.pathEquals(splitPath, e.getKey()))
                            .findFirst();
            if (entry.isPresent()) {
                final PathCondition[] cond = entry.get().getKey();
                for (int i = 0; i < splitPath.length; i++) {
                    if (cond[i].isCondition()) {
                        values.put(cond[i].getName(), splitPath[i]);
                    }
                }
                result = entry.get().getValue();
            }
        }

        return result;
    }

    class MyMappingRegistry {
        private final Object handler;
        private final Method invocableMethod;
        private final MyRequestMappingInfo mapping;
        private final Parameter[] parameters;

        MyMappingRegistry(Object handler, Method invocableMethod, MyRequestMappingInfo mapping) {
            this.handler = handler;
            this.invocableMethod = invocableMethod;
            this.mapping = mapping;
            this.parameters = invocableMethod.getParameters();

        }

        public Object getHandler() {
            return handler;
        }

        public Method getInvocableMethod() {
            return invocableMethod;
        }

        public MyRequestMappingInfo getMapping() {
            return mapping;
        }

        public Parameter[] getParameters() {
            return parameters;
        }
    }
}