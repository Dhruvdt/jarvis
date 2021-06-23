package com.unravel.scout.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@Configuration
@ComponentScan({"com.unravel.scout*"})
@EnableJpaRepositories("com.unravel.scout*")
public class DatabaseConfig {

    @Bean(name = "datasource1")
    @Primary
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource dataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "datasource2")
    @ConfigurationProperties(prefix="spring.data.mongodb")
    public DataSource dataSource2(){
        return DataSourceBuilder.create().build();
    }
}
