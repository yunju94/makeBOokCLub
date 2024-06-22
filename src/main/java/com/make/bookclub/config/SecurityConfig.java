package com.make.bookclub.config;

import com.make.bookclub.service.Memberservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    Memberservice memberservice;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws  Exception{
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico", "/error").permitAll()
                .requestMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        ).formLogin(formLogin -> formLogin
                .loginPage("/members/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .failureUrl("/members/login/error")
        );

        http.exceptionHandling(exception -> exception
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()));

        return  http.build();
    }

    @Bean static PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Autowired
    public void  configure(AuthenticationManagerBuilder auth) throws  Exception{
        auth.userDetailsService(memberservice).passwordEncoder(passwordEncoder());
    }

}
