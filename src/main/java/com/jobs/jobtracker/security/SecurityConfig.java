package com.jobs.jobtracker.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

}
