package com.spearkkk.ylba.controller.user;

import com.spearkkk.ylba.domain.user.ServiceUser;
import com.spearkkk.ylba.domain.user.ServiceUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {
    private final ServiceUserRepository serviceUserRepository;
    private final UserResponseMapper userResponseMapper;

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/api/users")
    public UserResponse users(@AuthenticationPrincipal User user) {
        String email = user.getUsername();
        Optional<ServiceUser> found = serviceUserRepository.findByEmail(email);
        return found.map(userResponseMapper::map)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please retry later."));
    }
}
