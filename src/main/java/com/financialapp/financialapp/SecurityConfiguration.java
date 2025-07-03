package com.financialapp.financialapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration{



    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public PasswordEncoder PasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
            http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/","/getUserRole","/test","/csrf-token","/login","/login.html","/index.html","/login.js","/main.js","/signup","/signup.html","/signup.js").permitAll()
                .requestMatchers("/**","/api/plaid/**").hasAnyRole("USER","ADMIN")
                .requestMatchers("/admin**").hasRole("ADMIN")
                .anyRequest().authenticated()
                
            )
            .userDetailsService(userDetailsService)
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .permitAll()
                .defaultSuccessUrl("/",true)
                .failureUrl("/login?error")
		    )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()

            );


        return http.build();

    }

}