package io.github.mat3e.todoapp.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Set;

@Configuration
class MvcConfiguration implements WebMvcConfigurer { //interface pozwala wpiąć się do Springa, zareagować na niektóre rzeczy
    private Set<HandlerInterceptor> interceptors; //wstrzykniecie wszystkich interceptorów przez konstruktor

    MvcConfiguration(final Set<HandlerInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        interceptors.stream().forEach(registry::addInterceptor);
    }
}
