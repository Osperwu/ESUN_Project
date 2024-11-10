package com.esun;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 將所有未找到的路徑重定向到 index.html
        registry.addViewController("/{spring:[^.]*}").setViewName("forward:/index.html");
    }
}