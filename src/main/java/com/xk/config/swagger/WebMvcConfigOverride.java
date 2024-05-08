package com.xk.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

/**
 * 拓展 springmvc
 * WebMvcConfigurationSupport 只能存在一个, 当前的会覆盖其他的
 */
@Configuration
@EnableWebMvc // 如果標注了就會被 springmvc 全面接管
public class WebMvcConfigOverride implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("index.html").setViewName("index");
    }

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/"};

    /**
     * InternalResourceViewResolver: jsp編譯
     * ThymeleafViewResolver: Thymeleaf編譯
     * @return
     */
    @Bean
    public ThymeleafViewResolver viewResolver() {
        return new ThymeleafViewResolver();// 用于解析重定向 redirect:
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern("/webjars/**")) {
            registry.addResourceHandler("/webjars/**").addResourceLocations(
                    "classpath:/META-INF/resources/webjars/");
        }
        if (!registry.hasMappingForPattern("/**")) {
            registry.addResourceHandler("/**").addResourceLocations(
                    CLASSPATH_RESOURCE_LOCATIONS);
        }
    }

}