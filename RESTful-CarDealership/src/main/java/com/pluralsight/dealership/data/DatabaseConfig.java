package com.pluralsight.dealership.data;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// the configuration tag tells spring that this class is a factory for creating beans that spring needs to manage
// this class creates the 'tools' that the services need 
@Configuration
public class DatabaseConfig {
    private String username;
    private String password;
    private String url;
    
    // the values come from the application.properties in the resource folder
    // we separate this data so we don't have to hardcode our database login
    public DatabaseConfig(@Value("${datasource.username}") String username,
                          @Value("${datasource.password}") String password,
                          @Value("${datasource.url}") String url) {
        this.username = username;
        this.password = password;
        this.url = url;
    }
    
    // the Bean annotation tells Spring to create this object for later use
    // it contains "object factory instructions" to create a BasicDataSource
    @Bean
    public BasicDataSource dataSource(){
        BasicDataSource bds = new BasicDataSource();
        bds.setUsername(username);
        bds.setPassword(password);
        bds.setUrl(url);
        
        return bds;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
