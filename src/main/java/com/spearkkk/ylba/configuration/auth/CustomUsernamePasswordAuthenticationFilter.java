package com.spearkkk.ylba.configuration.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
public class CustomUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    @Autowired
    public CustomUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher("/sign-in", "POST"), authenticationManager);
        this.objectMapper = objectMapper;
    }
    

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {

        if (!request.getMethod().equals(HttpMethod.POST.name()) || !request.getContentType().contains("application/json")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        EmailAndPassword emailAndPassword = objectMapper.readValue(request.getInputStream(), EmailAndPassword.class);
        String email = emailAndPassword.getEmail();
        String password = emailAndPassword.getPassword();

        if(email == null || password == null){
            log.info("Sign-in is failed. `email`: {}", email);
            throw new AuthenticationServiceException("Please fill information.");
        }

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password, List.of(new SimpleGrantedAuthority("USER")));
        authRequest.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        Authentication authentication = this.getAuthenticationManager().authenticate(authRequest);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    @ToString
    @Getter
    @NoArgsConstructor
    private static class EmailAndPassword {
        String email;
        String password;
    }
}