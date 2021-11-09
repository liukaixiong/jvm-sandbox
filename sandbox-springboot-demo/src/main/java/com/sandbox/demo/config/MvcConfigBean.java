package com.sandbox.demo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * mvc层定义
 *
 * @author Liuhx
 * @create 2018/6/14 18:41
 * @email liuhx@elab-plus.com
 **/
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.sandbox.demo.controller"}, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = {Controller.class})})
public class MvcConfigBean extends WebMvcConfigurerAdapter {

}
