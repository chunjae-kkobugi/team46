package com.memomo.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig {

    private final DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((auth) -> auth
                //.requestMatchers("/admin/**", "/lecture/**", "/teacher/**").permitAll()
                //.requestMatchers("/", "/**", "/login", "/join_frm", "join_frm_u", "join_frm_t").permitAll()
                .requestMatchers("/", "/member/**", "/board/list", "/post/**", "/send-mail", "/confirm").permitAll()
                .anyRequest().authenticated());

        http.formLogin((formLogin) -> formLogin
                .loginPage("/member/login")
                .loginProcessingUrl("/login_proc")
                .usernameParameter("id")
                .passwordParameter("pw")
                .failureUrl("/member/login/error")
                .defaultSuccessUrl("/member/loginPro", true)
        );

        http.csrf((csrf) -> csrf.disable());

        http.logout((logout) -> logout
                .logoutUrl("/member/logout")
                .logoutSuccessUrl("/")
        );
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring().requestMatchers(
                PathRequest.toStaticResources().atCommonLocations());
    }
}