package com.spearkkk.ylba.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spearkkk.ylba.configuration.auth.CustomAuthenticationManager;
import com.spearkkk.ylba.configuration.auth.CustomUserDetailsService;
import com.spearkkk.ylba.configuration.auth.CustomUsernamePasswordAuthenticationFilter;
import com.spearkkk.ylba.domain.user.ServiceUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final ServiceUserRepository serviceUserRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable().headers().frameOptions().disable();
        http.formLogin().disable().httpBasic().disable().exceptionHandling()
                .authenticationEntryPoint((request, response, authException)
                        -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getLocalizedMessage()));
        http.authorizeRequests()
                .antMatchers(PERMITTED).permitAll()
                .and().authorizeRequests()
                .antMatchers("/api/**").hasAuthority("USER")
                .anyRequest().authenticated();


        http.addFilterBefore(customUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter() {
        return new CustomUsernamePasswordAuthenticationFilter(new ObjectMapper(), authenticationManager());
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new CustomAuthenticationManager(userDetailsService());
    }

    @Bean
    public CustomUserDetailsService userDetailsService() {
        return new CustomUserDetailsService(serviceUserRepository);
    }

    private static final String[] PERMITTED = {"/", "/error", "/favicon.ico", "/hello", "/h2-console/**", "/sign-up", "/sign-in", "/request/code", "/verify/code","/change-password"};
}
