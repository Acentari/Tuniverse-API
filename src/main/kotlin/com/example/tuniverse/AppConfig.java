package com.example.tuniverse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {

    @Value("${app.path}")
    String path;

    public String getPath() {
        return path;
    }
}