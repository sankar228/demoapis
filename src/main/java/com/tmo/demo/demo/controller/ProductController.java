package com.tmo.demo.demo.controller;

import java.time.Duration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.tmo.demo.demo.jwtutils.TokenManager;
import com.tmo.demo.demo.model.JwtRequest;
import com.tmo.demo.demo.model.JwtResponse;
import com.tmo.demo.demo.service.GraphQLService;
import com.tmo.demo.demo.service.JwtUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import graphql.ExecutionResult;

@RestController(value = "/")
public class ProductController {

    @Autowired
    JwtUserDetailsService userDetailsService;
    @Autowired
    TokenManager tokenManager;

    @Autowired
    GraphQLService graphQLService;

    @GetMapping("/home")
    public String home() {
        return "This a Home";
    }

    @PostMapping(path = "/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getGraphqldata(@RequestBody String query) {
        ExecutionResult execresult = graphQLService.getGraphQL().execute(query);

        return new ResponseEntity<Object>(execresult, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> generateJwtToken(@RequestBody JwtRequest jwtuser, HttpServletResponse response) {

        UserDetails user = userDetailsService.loadUserByUsername(jwtuser.getUsername());
        String jwtToken = tokenManager.generateJwtToken(user);
        JwtResponse jwtresponse = new JwtResponse();
        jwtresponse.setJwtToken(jwtToken);

        /* set cookie */
        Cookie cookie = new Cookie("demoAuthCookie", jwtToken);
        cookie.setHttpOnly(true);
        cookie.setDomain("localhost");
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(Duration.ofDays(1).toSecondsPart());

        response.addCookie(cookie);

        return ResponseEntity.ok(jwtresponse);

    }
}
