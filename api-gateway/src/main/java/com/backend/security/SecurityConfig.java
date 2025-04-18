/*
 * @(#) $(NAME).java    1.0     3/30/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.security;

import com.backend.filters.JwtGlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.server.WebFilter;

import java.util.Arrays;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 30-March-2025 3:54 PM
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http.csrf(csrfSpec -> csrfSpec.disable())
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .pathMatchers("/products/**", "/user/**", "/order/**").authenticated()
                        .anyExchange().permitAll()
                );
//                .addFilterBefore((WebFilter) new JwtGlobalFilter(), SecurityWebFiltersOrder.AUTHENTICATION);
        return http.build();
    }
}
