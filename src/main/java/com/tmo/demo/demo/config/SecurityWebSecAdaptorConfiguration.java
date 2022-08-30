package com.tmo.demo.demo.config;

import java.io.InputStream;

import com.fasterxml.jackson.core.type.TypeReference;

import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.Md4PasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityWebSecAdaptorConfiguration extends WebSecurityConfigurerAdapter {

    // Manage: Authentication
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user")
                .password("pass123")
                .roles("USER")
                .and()
                .withUser("user2")
                .password("pass1234")
                .roles("MANAGER");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    // Manage: Authorization
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Example: 1, access by role
        /*
         * http.authorizeRequests()
         * .antMatchers("/employee/fetchall/**").hasRole("ADMIN")
         * .antMatchers("/employee/fetch/**").hasAnyRole("EMPLOYEE", "ADMIN")
         * .antMatchers("/employee/*").permitAll()
         * .and().formLogin();
         */

        // Example: 2, access by one time login for all the roles
        http.authorizeRequests()
                .antMatchers("/employee/fetchall/*", "/employee/fetch/*").authenticated()
                .antMatchers("/employee/*").permitAll()
                .and().formLogin();
    }
}
