package com.company.my.rest;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.*;

import javax.servlet.ServletRegistration;

/**
 * MyHttpServlet auto configuration
 */
@AutoConfigureOrder(-2147483640)
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({MyHttpServlet.class})
@AutoConfigureAfter({EmbeddedServletContainerAutoConfiguration.class})
class MyHttpServletAutoConfiguration {
    private static final String DEFAULT_MY_SERVLET_BEAN_NAME = "myHttpServlet";
    private static final String DEFAULT_MY_SERVLET_REGISTRATION_BEAN_NAME = "myHttpServletRegistration";

    @Configuration
    @ConditionalOnClass({ServletRegistration.class})
    @EnableConfigurationProperties({WebMvcProperties.class})
    @Import({MyHttpServletAutoConfiguration.MyHttpServletConfiguration.class})
    static class MyHttpServletRegistrationConfiguration {
        private final ServerProperties serverProperties;

        public MyHttpServletRegistrationConfiguration(ServerProperties serverProperties) {
            this.serverProperties = serverProperties;
        }

        @Bean(
                name = {DEFAULT_MY_SERVLET_REGISTRATION_BEAN_NAME}
        )
        @ConditionalOnBean(
                value = {MyHttpServlet.class},
                name = {DEFAULT_MY_SERVLET_BEAN_NAME}
        )
        public ServletRegistrationBean myHttpServletRegistration(final MyHttpServlet myHttpServlet) {
            ServletRegistrationBean registration = new ServletRegistrationBean(myHttpServlet, this.serverProperties.getServletMapping());
            registration.setName(DEFAULT_MY_SERVLET_BEAN_NAME);
            registration.setLoadOnStartup(-1);

            return registration;
        }
    }

    @Configuration
    @ConditionalOnClass({ServletRegistration.class})
    static class MyHttpServletConfiguration {

        @Bean(
                name = {DEFAULT_MY_SERVLET_BEAN_NAME}
        )
        public MyHttpServlet MyHttpServlet() {
            final MyHttpServlet MyHttpServlet = new MyHttpServlet();
            return MyHttpServlet;
        }
    }
}
