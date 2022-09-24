package com.spearkkk.ylba.controller.guest;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@ToString
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SignUpDto {
    @Email
    private String email;
    @NotBlank
    private String encryptedPassword;
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$")
    private String phone;
    @NotBlank
    private String name;
    private String nickname;
}
