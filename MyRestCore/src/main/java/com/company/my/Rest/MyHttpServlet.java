package com.company.my.Rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.BeansException;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * The servlet to process http requests.
 */
public class MyHttpServlet extends HttpServlet implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    /**
     * Type converter to convert url parameters
     */
    private final SimpleTypeConverter typeConverter = new SimpleTypeConverter();

    /**
     *
     */
    private MyRequestMappingHandlerMapping mappingHandler;

    /**
     * The wrappers of the primitive types.
     */
    private final Set<Class<?>> WRAPPER_TYPES = new HashSet<>(Arrays.asList(Double.class, Float.class, Boolean.class,
            Character.class, Byte.class, Short.class, Integer.class, Long.class));

    /**
     * Delegate GET requests to processRequest/doService.
     */
    @Override
    protected final void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Delegate POST requests to {@link #processRequest}.
     */
    @Override
    protected final void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    /**
     * Process this request, publishing an event regardless of the outcome.
     * <p>The actual event handling is performed by the abstract
     */
    private void processRequest(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            final Map<String, String> values = new HashMap<>();
            MyRequestMappingHandlerMapping.MyMappingRegistry registry = mappingHandler.getHandlerInternal(request, values);
            if (registry != null) {
                final Parameter[] parameters = registry.getParameters();
                Object[] objects = null;
                if (parameters.length > 0) {
                    objects = new Object[parameters.length];
                    for (int i = 0; i < parameters.length; i++) {
                        final String val = values.get("arg" + i);
                        objects[i] = typeConverter.convertIfNecessary(val, parameters[i].getType());
                    }
                }
                ReflectionUtils.makeAccessible(registry.getInvocableMethod());
                final Object bean = applicationContext.getAutowireCapableBeanFactory().getBean((String) registry.getHandler());
                final Object result = registry.getInvocableMethod().invoke(bean, objects);
                writeResponse(response, result);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void writeResponse(final HttpServletResponse response, Object result) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        if (result != null) {
            if (result.getClass().isPrimitive() || WRAPPER_TYPES.contains(result.getClass())) {
                response.getWriter().write(result.toString());
            } else {
                final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                ow.writeValue(response.getOutputStream(), result);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        mappingHandler = this.applicationContext.getBean(MyRequestMappingHandlerMapping.class);
    }
}
