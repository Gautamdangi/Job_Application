package com.jobs.jobtracker.Security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity

public class SecurityConfig {



    @Autowired  JWTFilter JWTFilter;


    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->session.
                        sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
//->
//                        .requestMatchers(
//                                "/api/auth/**",
//                                "/swagger-ui/**",
//                                "/v3/api-docs/**",
//                                "/swagger-ui.html"
//                        ).permitAll()

                        // Student endpoints
                        .requestMatchers("/api/applicant/**").hasRole("Applicant")

                        // Recruiter endpoints
                        .requestMatchers("/api/recruiter/**").hasRole("Recruiter")

                        // Admin endpoints
                        .requestMatchers("/api/admin/**").hasRole("Admin")

//                        // All other requests must be authenticated
//                        .anyRequest().authenticated()
//                        //public endpoints
                .requestMatchers("/api/auth/**","/hello").permitAll()
                        // applicant endpoint
               // .requestMatchers("/jobs/**").authenticated()
                        //
                .anyRequest().authenticated())
                        .addFilterBefore(JWTFilter,
                                UsernamePasswordAuthenticationFilter.class);

//.formLogin(form -> form.disable())
//
//.httpBasic(Customizer.withDefaults()
//
//
//
//        auth -> auth
//                .requestMatchers("/api/auth/**").permitAll()
        return http.build();
    }
// customizing own auth provider
//@Bean
//    public AuthenticationProvider authenticationProvider() {
//
//
//    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//    provider.setPasswordEncoder(passwordEncoder());
//    provider.setUserDetailServiceImpl(UserDetailsServiceImpl);
//
//    return provider;
//}


    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
// configure cross-origin resource sharing

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource(){
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("http://localhot:8080","http://localhost:5123"));
//        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE"));
//        configuration.setAllowedHeaders(List.of("*"));
//        configuration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//
//
//
//    }
// AuthenticationManager ->AuthenticationManager acts as a coordinator that delegates authentication requests to
// one or more AuthenticationProviders and returns the final result.
    // spring do it auto
//              Login request
//                   !
//           AuthenticationManager.authenticate()
//                   !
//           AuthenticationProvider.authenticate() ->AuthenticationProvider performs the actual authentication by validating credentials
//                   !
//           PasswordEncoder.matches()
//                   !
//        Authenticated token returned


//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new InMemoryUserDetailsManager();
//    }
}
