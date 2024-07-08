package com.leo.leo_final;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/profileImage/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/upload/uploads/profileImage/");

        registry.addResourceHandler("/uploads/statusImages/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/upload/uploads/statusImages/");
    }
}
