package com.spearkkk.ylba.controller.user;

import lombok.*;

@ToString
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String phone;
    private String name;
    private String nickname;
}
