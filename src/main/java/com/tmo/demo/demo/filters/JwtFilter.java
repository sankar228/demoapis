package com.tmo.demo.demo.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmo.demo.demo.jwtutils.TokenManager;
import com.tmo.demo.demo.service.JwtUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtUserDetailsService userDetailsService;
    @Autowired
    TokenManager tokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        String tokenHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;
        if (request.getServletPath().equals("/api/cry") || request.getServletPath().equals("/query")) {
            filterChain.doFilter(request, response);
        } else {
            if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
                token = tokenHeader.substring(7);
                try {
                    username = tokenManager.getUsernameFromToken(token);
                } catch (IllegalArgumentException e) {
                    log.error("Unable to get JWT Token");
                } catch (ExpiredJwtException e) {
                    log.error("JWT Token has expired", e);
                    throw new BadCredentialsException("Token expired");
                }
            } else {
                System.out.println("Bearer String not found in token");
                // throw new BadCredentialsException("Unauthorized access");
            }
            if (null != username && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (tokenManager.validateJwtToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null,
                            userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        }
    }

}
