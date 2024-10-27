package com.realestateprosofia.realestateprosofia.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class CustomDataSourceBuilder {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${DATABASE}")
    private String database;

    public DataSource buildDataSource(final String tenant) {
        return DataSourceBuilder.create()
                .url(url.replace(database, tenant))
                .driverClassName(driverClassName)
                .username(username)
                .password(password)
                .build();
    }
}