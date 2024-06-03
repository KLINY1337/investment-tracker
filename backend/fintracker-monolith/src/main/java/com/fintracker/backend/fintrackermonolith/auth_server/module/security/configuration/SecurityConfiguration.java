package com.fintracker.backend.fintrackermonolith.auth_server.module.security.configuration;


import com.fintracker.backend.fintrackermonolith.auth_server.module.security.filter.TokenAuthenticationFilter;
import com.fintracker.backend.fintrackermonolith.auth_server.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration {

    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    @Value("${server.hostname}")
    private String hostname;

    @Value("${server.port}")
    private int port;

    @Value("${server.protocol}")
    private String protocol;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()));
        http.sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.authorizeHttpRequests(requestMatcherRegistry -> requestMatcherRegistry
                .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .requestMatchers("/auth/**", "/v3/api-docs","/actuator/**","/gigachat/**", "/swagger-ui.html", "/api/**")
                .permitAll()
                .anyRequest()
                .permitAll()
        );
        return http.build();
    }

    private static final String GET = "GET";

    private static final String POST = "POST";

    private static final String DELETE = "DELETE";

    private static final String PUT = "PUT";

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedMethods(GET, POST, DELETE, PUT, "OPTIONS")
//                        .allowedHeaders("*")
//                        .allowedOriginPatterns("*")
//                        .allowCredentials(true);
//            }
//        };
//    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//
//        configuration.setAllowedOrigins(List.of(
//                "https://investment.lexa2hk.ru:443",
//                "https://investment.lexa2hk.ru",
//                protocol +"://" + hostname + ":" + port,
//                protocol +"://" + "localhost" + ":" + port));
//        configuration.setAllowedMethods(List.of("GET","POST",  "PUT", "DELETE"));
//        configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//
//        source.registerCorsConfiguration("/**",configuration);
//
//        return source;
//    }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();

            // Allow all origins
            configuration.setAllowedOrigins(List.of("*", "http://89.223.121.170:8080"));

            // Allow all methods
            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));

            // Allow all headers
            configuration.setAllowedHeaders(Collections.singletonList("*"));

    //        // Allow credentials
    //        configuration.setAllowCredentials(true);

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);

            return source;
        }
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(PasswordUtils.getPasswordEncoder());

        return authProvider;
    }
}