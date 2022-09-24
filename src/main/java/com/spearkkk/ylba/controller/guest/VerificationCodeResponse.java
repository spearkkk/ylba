package com.spearkkk.ylba.controller.guest;

import lombok.*;

@ToString
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class VerificationCodeResponse {
    private String phone;
    private int code;
}
