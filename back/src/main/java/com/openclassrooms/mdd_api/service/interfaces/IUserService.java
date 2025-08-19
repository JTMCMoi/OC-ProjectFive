package com.openclassrooms.mdd_api.service.interfaces;

import com.openclassrooms.mdd_api.dto.user.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public interface IUserService {

    UserAuthResponseDto registerUser(final UserRegisterRequestDto user);

    UserAuthResponseDto loginUser(final UserLoginRequestDto user);

    GetUserResponseDto getUser(final JwtAuthenticationToken token);

    GetUserResponseDto updateUser(final JwtAuthenticationToken token, final UserUpdateRequestDto user);
}
