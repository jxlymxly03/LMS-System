// src/main/java/com/lms/lmsbackend/config/WebConfig.java
package com.lms.lmsbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${video.storage.location}")
    private String storageLocation;  // e.g. /absolute/path/to/videos

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
          .addResourceHandler("/videos/**")
          .addResourceLocations("file:" + storageLocation + "/")
          .setCachePeriod(3600)        // cache for 1 hour
          .resourceChain(true);
    }
}
