package com.spearkkk.ylba.controller.guest;

import com.spearkkk.ylba.domain.user.ServiceUser;
import com.spearkkk.ylba.domain.user.ServiceUserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

@Tag(name = "Guest API")
@Slf4j
@RequiredArgsConstructor
@RestController
public class GuestController {
    /**
     * todo: 전화번호 인증을 위해 임시로 로컬 메로리를 사용하여 기능 구현.
     * 필요하다면 shared memory cache(redis, memcache) 또는 DB 테이블로 변경해야 함.
     * 인증 이력을 소멸하기 위해 TTL 설정이나 이력에 대한 datetime을 따로 관리할 필요가 있음.
     */
    private static final Map<String, Integer> PHONE_TO_VERIFICATION_CODE = new HashMap<>();
    private static final Set<String> VERIFIED_PHONE = new HashSet<>();

    private final ServiceUserRepository serviceUserRepository;

    @Operation(summary = "회원가입", description = "사용자의 정보를 등록한다.")
    @PostMapping(value = "/sign-up")
    public void signUp(@Valid  @RequestBody SignUpDto signUpDto) {
        if (!VERIFIED_PHONE.contains(signUpDto.getPhone())) {
            log.warn("Phone is not verified yet. `signUpDto`: {}", signUpDto);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please verify your phone number first.");
        }

        if (serviceUserRepository.findByEmail(signUpDto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please sign in.");
        }

        ServiceUser serviceUser = ServiceUser.builder()
                .email(signUpDto.getEmail())
                .password(signUpDto.getEncryptedPassword())
                .phone(signUpDto.getPhone())
                .name(signUpDto.getName())
                .nickname(signUpDto.getNickname())
                .build();

        try {
            serviceUserRepository.save(serviceUser);
            VERIFIED_PHONE.remove(signUpDto.getPhone());
        } catch (Exception e) {
            log.error("Exception is occurred in sign-up, `signUpDto`: {}", signUpDto, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please retry to sign up later.");
        }
    }

    @Operation(summary = "전화번호 인증 코드 확인", description = "사용자의 전화번호로 발송한 코드를 확인한다.")
    @PostMapping(value = "/verify/code")
    public void verifyCode(@Valid @RequestBody VerificationCodeVerifyDto verificationCodeVerifyDto) {
        String phone = verificationCodeVerifyDto.getPhone();
        Integer code = verificationCodeVerifyDto.getCode();

        if (PHONE_TO_VERIFICATION_CODE.containsKey(phone) && PHONE_TO_VERIFICATION_CODE.get(phone).equals(code)) {
            VERIFIED_PHONE.add(phone);
            return;
        }

        log.info("Verification is failed because phone or verification code is invalid. `phone`: {}, `code`: {}", phone, code);
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Verification failed.");
    }

    @Operation(summary = "전화번호 인증 코드 보내기", description = "사용자의 전화번호로 인증 코드를 발송한다.")
    @PostMapping(value = "/request/code")
    public VerificationCodeResponse requetCode(@Valid @RequestBody VerificationCodeRequestDto verificationCodeRequestDto) {
        int code = Math.abs(new Random(System.currentTimeMillis()).nextInt()) % 1_000_000;
        // todo: 인증번호를 SMS로 직접 보내는 부분은 구현하지 않아서 추후 구현해야 함.
        PHONE_TO_VERIFICATION_CODE.put(verificationCodeRequestDto.getPhone(), code);

        // todo: 현재 sms를 보내고 있지 않아서 생성된 코드를 반환한다. 추후 제거 가능함.
        return VerificationCodeResponse.builder().phone(verificationCodeRequestDto.getPhone()).code(code).build();
    }

    @Operation(summary = "비밀번호 재설정", description = "사용자의 비밀번호를 재설정한다.")
    @Transactional
    @PostMapping(value = "/change-password")
    public void changePassword(@Valid @RequestBody PasswordChangeRequestDto passwordChangeRequestDto) {
        // todo: 단순히 전화번호 인증만 확인하고 있는데, 요청 자체를 확인할 수 있는 방안을 마련해야 함.
        if (!VERIFIED_PHONE.contains(passwordChangeRequestDto.getPhone())) {
            log.warn("Phone is not verified yet. `passwordChangeRequestDto`: {}", passwordChangeRequestDto);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please verify your phone number first.");
        }
        Optional<ServiceUser> foundUser = serviceUserRepository.findByPhone(passwordChangeRequestDto.getPhone());
        foundUser.ifPresent(serviceUser -> serviceUser.updatePassword(passwordChangeRequestDto.getEncryptedPassword()));
    }
}
