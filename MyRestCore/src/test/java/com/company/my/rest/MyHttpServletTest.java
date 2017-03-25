package com.company.my.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class MyHttpServletTest {
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

        @Bean
        public MyHttpServlet httpServlet() {
            return new MyHttpServlet();
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
        public TestType testWithoutParams() {
            return new TestType(55, "noParametersTestType");
        }

        @MyRequestMapping("/primitive")
        public int testPrimitive() {
            return 21;
        }

        @MyRequestMapping("/primitiveWrapper")
        public Double testPrimitiveWrapper() {
            return 34.2;
        }
    }

    static class TestType {
        private final int val;
        private final String caption;

        TestType(final int val, final String caption) {
            this.val = val;
            this.caption = caption;
        }

        public int getVal() {
            return val;
        }

        public String getCaption() {
            return caption;
        }
    }

    @Autowired
    private MyHttpServlet myHttpServlet;

    @Test
    public void testGetWithParams() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest("get", "/test/testString/23");
        final MockHttpServletResponse response = new MockHttpServletResponse();
        myHttpServlet.doGet(request, response);
        assertEquals("\"testString23\"", response.getContentAsString());
    }

    @Test
    public void testClassReturn() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest("post", "/test/path/subPath");
        final MockHttpServletResponse response = new MockHttpServletResponse();
        myHttpServlet.doPost(request, response);
        assertEquals("{\r\n" +
                "  \"val\" : 55,\r\n" +
                "  \"caption\" : \"noParametersTestType\"\r\n" +
                "}", response.getContentAsString());
    }

    @Test
    public void testPrimitiveWraper() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest("get", "/test/primitive");
        final MockHttpServletResponse response = new MockHttpServletResponse();
        myHttpServlet.doGet(request, response);
        assertEquals("21", response.getContentAsString());
    }

    @Test
    public void testPrimitive() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest("get", "/test/primitiveWrapper");
        final MockHttpServletResponse response = new MockHttpServletResponse();
        myHttpServlet.doGet(request, response);
        assertEquals("34.2", response.getContentAsString());
    }
}