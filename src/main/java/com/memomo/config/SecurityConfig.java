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
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

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
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers(new MvcRequestMatcher(introspector,"/")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/board/list")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/post/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/send-mail")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/confirm")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/member/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/stomp/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/socket/**")).permitAll()
                .anyRequest().authenticated());

        http.formLogin((formLogin) -> formLogin
                .loginPage("/member/login")
                .loginProcessingUrl("/login_proc")
                .usernameParameter("id")
                .passwordParameter("pw")
                .failureUrl("/member/login/error")
                .defaultSuccessUrl("/member/loginPro", true)
        );

        http.csrf(AbstractHttpConfigurer::disable);

        http.logout((logout) -> logout
                .logoutUrl("/member/logout")
                .logoutSuccessUrl("/")
        );
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring().
                requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .requestMatchers(new AntPathRequestMatcher( "/css/**"))
                .requestMatchers(new AntPathRequestMatcher( "/js/**"))
                .requestMatchers(new AntPathRequestMatcher( "/img/**"))
                .requestMatchers(new AntPathRequestMatcher( "/lib/**"));
    }

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        DefaultHttpFirewall firewall = new DefaultHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }
}