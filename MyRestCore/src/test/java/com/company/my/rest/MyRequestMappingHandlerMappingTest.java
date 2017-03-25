package com.company.my.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.HashMap;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class MyRequestMappingHandlerMappingTest {
    @Configuration
    static class ContextConfiguration {

        @Bean
        public TestController testController() {
            return new TestController();
        }

        @Bean
        public MyRequestMappingHandlerMapping handlerMapping() {
            return new MyRequestMappingHandlerMapping();
        }
    }

    @MyRestController
    @MyRequestMapping("/test")
    static class TestController {

        @MyRequestMapping("/{testString}/{testId}")
        public String testWithParam(final int testId, final String testString) {
            return testString + testId;
        }

        @MyRequestMapping("/path/subPath")
        public String testWithoutParams() {
            return "noParameters";
        }
    }

    @Autowired
    private MyRequestMappingHandlerMapping handlerMapping;

    @Test
    public void getStraightPath() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest("get", "/test/path/subPath");
        final HashMap<String, String> values = new HashMap<>();
        MyRequestMappingHandlerMapping.MyMappingRegistry registry = handlerMapping.getHandlerInternal(request, values);
        assertEquals("testWithoutParams", registry.getInvocableMethod().getName());
        assertEquals(0, values.size());
    }

    @Test
    public void getParametrizedPath() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest("post", "/test/testString/23");
        final HashMap<String, String> values = new HashMap<>();
        MyRequestMappingHandlerMapping.MyMappingRegistry registry = handlerMapping.getHandlerInternal(request, values);
        assertEquals("testWithParam", registry.getInvocableMethod().getName());
        assertEquals("testString", values.get("testString"));
        assertEquals("23", values.get("testId"));
        assertEquals("testController", registry.getHandler());
        assertEquals(2, registry.getParameters().length);
        assertEquals("testId", registry.getParameters()[0].getName());
        assertEquals("testString", registry.getParameters()[1].getName());
    }

    @Test
    public void getWrongPath() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest("post", "/WrongPath/testString/23");
        final HashMap<String, String> values = new HashMap<>();
        MyRequestMappingHandlerMapping.MyMappingRegistry registry = handlerMapping.getHandlerInternal(request, values);
        assertNull("Wrong path should not be found", registry);
    }
}