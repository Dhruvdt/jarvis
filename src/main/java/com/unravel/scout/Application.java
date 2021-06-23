package com.unravel.scout;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

@SpringBootApplication
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  ModelMapper getModelMapper() {
    return new ModelMapper();
  }

  @Bean
  public WebMvcConfigurer corsConfigarator() {
    return new WebMvcConfigurer() {

      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry
            .addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedMethods("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS")
            .allowedHeaders("*");
      }
    };
  }




 /* @Bean(name = "datasource2")
  @ConfigurationProperties("database2.datasource")
  public DataSource dataSource2(){
    return DataSourceBuilder.create().build();
  }*/
}
