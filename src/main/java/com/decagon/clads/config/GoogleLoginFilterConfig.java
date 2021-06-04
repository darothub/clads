package com.decagon.clads.config;

import com.decagon.clads.filter.GoogleLoginFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class GoogleLoginFilterConfig {

    @Bean
    FilterRegistrationBean<GoogleLoginFilter> registrationBean(){
        FilterRegistrationBean<GoogleLoginFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new GoogleLoginFilter());
        registrationBean.setUrlPatterns(Collections.singleton("/login"));
        return registrationBean;
    }
}
