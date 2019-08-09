package com.panshi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author huangxiaolei
 * @description
 * @create 2019/08/04
 */
//@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    //@Autowired
    private CrossDomain crossDomain;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println();
        registry.addInterceptor(crossDomain).addPathPatterns("/marketingController/spikeActivity/**/**");
    }

}
