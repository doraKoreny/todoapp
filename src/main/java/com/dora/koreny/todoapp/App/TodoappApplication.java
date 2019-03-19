package com.dora.koreny.todoapp.App;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@SpringBootApplication
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource("classpath:auth0.properties")
})
public class TodoappApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoappApplication.class, args);
    }


}
