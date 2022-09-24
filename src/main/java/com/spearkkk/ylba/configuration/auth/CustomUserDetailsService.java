package com.spearkkk.ylba.configuration.auth;

import com.spearkkk.ylba.domain.user.ServiceUser;
import com.spearkkk.ylba.domain.user.ServiceUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final ServiceUserRepository serviceUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ServiceUser> user = serviceUserRepository.findByEmail(username);
        return user.map(it ->
                        new User(it.getEmail(), it.getPassword(), List.of(new SimpleGrantedAuthority("USER"))))
                .orElseThrow(() -> new UsernameNotFoundException("Please check your information."));
    }
}
