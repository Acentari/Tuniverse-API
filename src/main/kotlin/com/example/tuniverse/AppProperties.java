package com.example.tuniverse;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path =  path;
    }
}