package com.spearkkk.ylba.controller.user;

import com.spearkkk.ylba.configuration.auth.SessionUser;
import com.spearkkk.ylba.domain.user.ServiceUser;
import com.spearkkk.ylba.domain.user.ServiceUserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Tag(name = "User API")
@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {
    private final ServiceUserRepository serviceUserRepository;
    private final UserResponseMapper userResponseMapper;

    @Operation(summary = "현재 로그인한 사용자 정보 조회", description = "현재 로그인한 사용자의 데이터를 조회한다.")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/api/users")
    public UserResponse users(@SessionUser User user) {
        String email = user.getUsername();
        Optional<ServiceUser> found = serviceUserRepository.findByEmail(email);
        return found.map(userResponseMapper::map)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please retry later."));
    }
}
