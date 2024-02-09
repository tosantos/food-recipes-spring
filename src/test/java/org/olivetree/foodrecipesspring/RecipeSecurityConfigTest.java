package org.olivetree.foodrecipesspring;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public class RecipeSecurityConfigTest {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
                        (authz) -> authz
                                .anyRequest().permitAll()
                ).csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
