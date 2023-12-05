package com.swisscom.tasks.task3.configuration;

import com.swisscom.tasks.task3.service.MongoUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SecurityFilterChainConfig is a Configuration class for security.
 */
@RequiredArgsConstructor
@Configuration
public class SecurityFilterChainConfig {
    private final PasswordEncoder passwordEncoder;
    private final MongoUserDetailsService mongoUserDetailsService;

    @Value("${application.security.auth.enabled:true}")
    private boolean authEnabled;
    private static final String[] WHITE_LIST_URL = {
//            "/actuator/**",
            "/actuator/info",
            "/actuator/health",
            "/api/v1/auth/login",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/graphiql"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                            if(authEnabled)
                                auth.requestMatchers(WHITE_LIST_URL).permitAll().anyRequest().authenticated();
                            else
                                auth.requestMatchers(WHITE_LIST_URL).permitAll().anyRequest().permitAll();
                        }
                )
                .userDetailsService(mongoUserDetailsService)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(o -> o.jwt(Customizer.withDefaults()))
                .build();
    }

    @Bean
    public AuthenticationManager authManager(UserDetailsService userDetailsService) {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authProvider);
    }
}
