package com.shoply.Products.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConfigCloudinary {
    @Value("${cloudinary.cloudname}")
    private String cloud_name;
    @Value("${cloudinary.apikey}")
    private String api_key;
    @Value("${cloudinary.secretkey}")
    private String api_secret;

    @Bean
    public Cloudinary configKey(){

        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloud_name);
        config.put("api_key", api_key);
        config.put("api_secret", api_secret);

        return new Cloudinary(config);
    }
}
