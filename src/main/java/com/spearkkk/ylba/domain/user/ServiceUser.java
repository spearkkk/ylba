package com.spearkkk.ylba.domain.user;

import com.spearkkk.ylba.domain.BaseDatetime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "service_user")
public class ServiceUser extends BaseDatetime {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String name;

    @Column
    private String nickname;

    @Builder
    private ServiceUser(String email, String password, String phone, String name, String nickname) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.name = name;
        this.nickname = nickname;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
