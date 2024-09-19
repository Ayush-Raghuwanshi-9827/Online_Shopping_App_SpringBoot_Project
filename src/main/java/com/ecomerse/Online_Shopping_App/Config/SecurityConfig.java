package com.ecomerse.Online_Shopping_App.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


import java.security.Security;

@Configuration
public class SecurityConfig {

    @Autowired
    private AuthSuccessHandlerImpl authSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsServiceImple();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())  // Disabling CSRF (optional, but necessary for H2 Console)
                .securityContext(securityContext -> securityContext.requireExplicitSave(false))  // Ensure security context is saved automatically

                // Updated headers configuration for Spring Security 6.1+
                .headers(headers -> headers
                        .defaultsDisabled()  // Disable all default headers to allow manual configuration
                        .contentTypeOptions(contentTypeOptions -> contentTypeOptions.disable())  // Disable content type options
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())  // Allow iframe embedding for same-origin requests (H2 console)
                )
                .cors(cors -> cors.disable())  // Disable CORS, as per your original config

                // Authorization configuration
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/user/**").hasRole("USER")  // Restrict `/user/**` to users with the role `USER`
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // Restrict `/admin/**` to users with the role `ADMIN`
                        .requestMatchers("/**").permitAll()  // Allow unrestricted access to all other routes
                )
                // Login form configuration
                .formLogin(form -> form
                        .loginPage("/singin")  // Custom login page
                        .loginProcessingUrl("/login")  // URL for processing login
                        .successHandler(authSuccessHandler)  // Custom success handler for login (preserved as is)
                )
                .logout(logout -> logout.permitAll());  // Allow everyone to access logout functionality

        return http.build();
    }
}
