package com.spearkkk.ylba.controller.guest;

import lombok.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

@ToString
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class VerificationCodeVerifyDto {
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$")
    private String phone;
    @PositiveOrZero
    private Integer code;
}
