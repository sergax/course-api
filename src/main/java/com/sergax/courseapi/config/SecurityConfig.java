package com.sergax.courseapi.config;

import com.sergax.courseapi.security.JwtConfigurer;
import com.sergax.courseapi.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider tokenProvider;
    private static final String ADMIN_ENDPOINT = "/api/v1/admin/**";
    private static final String USER_ENDPOINT = "/api/v1/profile/**";
    private static final String AUTH_ENDPOINT = "/api/v1/auth/**";

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        configureSafe(http);
    }

    @Bean
    protected JwtConfigurer configureSafe(HttpSecurity http) throws Exception {
        return http

                .cors().and()
//                .cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
                .csrf().disable()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests()
                .antMatchers(AUTH_ENDPOINT).permitAll()
                .antMatchers(USER_ENDPOINT).hasAnyRole("USER", "ADMIN")
                .antMatchers(ADMIN_ENDPOINT).hasAnyRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(tokenProvider));
    }


}
