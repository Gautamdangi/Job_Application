package com.jobs.jobtracker.security;

import com.jobs.jobtracker.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private  JWTUtil jwtUtil;

    @Autowired
    ApplicationContext context;

    @Autowired
    UserRepository userRepository;


    @Override
    protected void doFilterInternal(
          @NonNull HttpServletRequest request,
          @NonNull  HttpServletResponse response,
          @NonNull  FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");


        System.out.println("=== JWT Filter Debug ===");
        System.out.println("Request Path: " + request.getServletPath());
        System.out.println("Auth Header: " + authHeader);


        if (authHeader == null || !authHeader.startsWith("Bearer ")) {


            System.out.println("No valid auth header, continuing filter chain");

            filterChain.doFilter(request, response);
            return;
        }




try {

    String token = authHeader.substring(7);
    String userName = jwtUtil.extractUserName(token);
    String role =  jwtUtil.extractRole(token);

    if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

        //UserDetails userDetails = context.getBean(UserDetailServiceImpl.class).loadUserByUsername(userName);
        var user = userRepository.findByUserName(userName).orElse(null);

        if (user != null && user.getIsActive() && jwtUtil.isTokenValid(token, userName)) {

            //if (jwtUtil.isTokenValid(token, userDetails)) {

            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            authorities

                            // Here grant/assign role
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
}
        catch(Exception e){

        logger.error("JWT Authentication failed: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}










