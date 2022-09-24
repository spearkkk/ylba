package com.spearkkk.ylba.controller.user;

import com.spearkkk.ylba.domain.user.ServiceUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {
    UserResponse map(ServiceUser serviceUser);
}
