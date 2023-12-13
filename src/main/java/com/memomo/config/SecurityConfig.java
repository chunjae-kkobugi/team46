package com.memomo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public CorsConfigurationSource corsConfig() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080")); // 허용할 출처를 여기에 설정
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // cors, csrf, logout, formLogin, httpBasic, exceptionHandling, authorizeHttpRequests

        http.cors(cors->{
            cors.configurationSource(corsConfig());
        });

        http.formLogin((formLogin) ->
            formLogin
                    .usernameParameter("id")
                    .passwordParameter("pw")
                    .loginPage("/member/login")
//                    .loginProcessingUrl("/member/loginPro")
        );

        http.logout((logout)->
                logout.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                        .logoutSuccessUrl("/")
        );


        http.authorizeRequests((authorize)->
                authorize
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers("*/").permitAll()
                        .requestMatchers("/", "/member/**").permitAll()
                        .requestMatchers("/board/**").permitAll()
                        .requestMatchers("/post/**").permitAll()
                        .anyRequest().authenticated()
        );

        return http.build();
    }
}