package com.dora.koreny.todoapp.App.auth0;

import com.auth0.spring.security.api.JwtWebSecurityConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
@Configuration
@PropertySource(value = "auth0.properties")
public class AppConfig extends WebSecurityConfigurerAdapter {

    @Value(value = "${auth0.apiAudience}")
    private String apiAudience;

    @Value(value = "${auth0.issuer}")
    private String issuer;

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.addAllowedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        JwtWebSecurityConfigurer
                .forRS256(apiAudience, issuer)
                .configure(http)
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/getAll").permitAll()
                .antMatchers(HttpMethod.POST, "/list").authenticated()
                .antMatchers(HttpMethod.POST, "/addTodo").authenticated()
                .antMatchers(HttpMethod.DELETE, "/todos/completed").hasAuthority("read:admin")
                .antMatchers(HttpMethod.PUT, "/todos/{id}/toggle_status").hasAuthority("read:admin")
                .antMatchers(HttpMethod.PUT, "/todos/toggle_all").hasAuthority("read:admin")
                .antMatchers(HttpMethod.DELETE, "/todos/{id}").hasAuthority("read:admin")
                .antMatchers(HttpMethod.PUT, "/todos/{id}").hasAuthority("read:admin");

    }

}
