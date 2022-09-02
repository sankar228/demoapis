package com.tmo.demo.demo.config;

import javax.swing.JWindow;

import com.tmo.demo.demo.filters.JwtFilter;
import com.tmo.demo.demo.service.JwtUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecuritySecFilterChainConfigurer {

    @Autowired
    JwtUserDetailsService myUserDetailsService;

    @Autowired
    JwtFilter jwtFilter;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // http.csrf().disable().authorizeRequests().antMatchers("/").permitAll();

        http.csrf().disable();
        http.userDetailsService(myUserDetailsService);
        http.authorizeHttpRequests(
                (authz) -> authz.antMatchers("/login/**", "/login", "/api/cry",
                        "/api/cry/**", "/query")
                        .permitAll()
                        .and()
                        .antMatcher("/home/**"));
        http.httpBasic(Customizer.withDefaults());
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
