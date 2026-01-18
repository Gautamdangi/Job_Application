package com.jobs.jobtracker.Security;

import com.jobs.jobtracker.Repository.UserRepository;
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

//    The filter tries to validate a token that doesn't exist for login/signup
//    which could cause errors or unnecessary processing.


//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        return request.getServletPath().startsWith("/api/auth/");
//    }
//@Override
//protected boolean shouldNotFilter(HttpServletRequest request) {
//    String path = request.getServletPath();
//    return path.startsWith("/api/auth/") ||
//            path.startsWith("/jobs/") ||
//            path.equals("/hello");
//            path.equals("/public")
//}


    @Override
    protected void doFilterInternal(
          @NonNull HttpServletRequest request,
          @NonNull  HttpServletResponse response,
          @NonNull  FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");


//        System.out.println("=== JWT Filter Debug ===");
//        System.out.println("Request Path: " + request.getServletPath());
//        System.out.println("Auth Header: " + authHeader);
//

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {


         //   System.out.println("No valid auth header, continuing filter chain");

            filterChain.doFilter(request, response);
            return;
        }

//
//        try {
//            String token = authHeader.substring(7);
//            System.out.println("Token extracted: " + token.substring(0, 20) + "...");
//
//            String username = jwtUtil.extractUserName(token);
//            System.out.println("Username from token: " + username);
//
//            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//                UserDetails userDetails = context.getBean(UserDetailServiceImpl.class)
//                        .loadUserByUsername(username);
//
//                System.out.println("UserDetails loaded: " + userDetails.getUsername());
//
//                if (jwtUtil.isTokenValid(token, userDetails)) {
//                    System.out.println("Token is valid!");
//
//                    UsernamePasswordAuthenticationToken authentication =
//                            new UsernamePasswordAuthenticationToken(
//                                    userDetails,
//                                    null,
//                                    userDetails.getAuthorities()
//                            );
//
//                    authentication.setDetails(
//                            new WebAuthenticationDetailsSource().buildDetails(request)
//                    );
//
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                    System.out.println("Authentication set in SecurityContext");
//                } else {
//                    System.out.println("Token validation failed!");
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("Error in JWT Filter: " + e.getMessage());
//            e.printStackTrace();
//        }
//
//        System.out.println("=== End JWT Filter ===");
//        filterChain.doFilter(request, response);
//    }
//}








try {

    String token = authHeader.substring(7);
    String userName = jwtUtil.extractUserName(token);

    if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

        //UserDetails userDetails = context.getBean(UserDetailServiceImpl.class).loadUserByUsername(userName);
        var user = userRepository.findByUserName(userName).orElse(null);

        if (user != null && user.getIsActive() && jwtUtil.isTokenValid(token, userName)) {

            //if (jwtUtil.isTokenValid(token, userDetails)) {

            var authorities = List.of(new SimpleGrantedAuthority("Role" + user.getRole().name()));

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


