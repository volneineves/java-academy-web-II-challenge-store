package com.ada.avanadestore.config;

import com.ada.avanadestore.filter.AuthenticationTokenFilter;
import com.ada.avanadestore.handler.CustomAuthenticationEntryPointHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final AuthenticationTokenFilter authenticationTokenFilter;
    private final CustomAuthenticationEntryPointHandler customAuthEntryPointHandler;

    public SecurityConfig(AuthenticationTokenFilter authenticationTokenFilter, CustomAuthenticationEntryPointHandler customAuthEntryPointHandler) {
        this.authenticationTokenFilter = authenticationTokenFilter;
        this.customAuthEntryPointHandler = customAuthEntryPointHandler;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(handling -> handling.authenticationEntryPoint(customAuthEntryPointHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers(new AntPathRequestMatcher("/auth")).permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers(new AntPathRequestMatcher("/customers", HttpMethod.POST.name())).permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll().anyRequest().authenticated())
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
