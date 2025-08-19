package com.openclassrooms.mdd_api.service.implementations;

import com.openclassrooms.mdd_api.dto.user.*;
import com.openclassrooms.mdd_api.exception.ApiException;
import com.openclassrooms.mdd_api.mapper.UserMapper;
import com.openclassrooms.mdd_api.model.User;
import com.openclassrooms.mdd_api.repository.UserRepository;
import com.openclassrooms.mdd_api.service.interfaces.IUserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JWTService jwtService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserService(final UserRepository userRepository, final UserMapper userMapper, final JWTService jwtService, final BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public UserAuthResponseDto registerUser(final UserRegisterRequestDto user) {
        Optional<User> emailExist = this.userRepository.findByEmail(user.email());
        Optional<User> nameExist = this.userRepository.findByName(user.name());
        if (emailExist.isPresent()) {
            throw new ApiException("Cette adresse email n'est pas disponible !");
        }
        if (nameExist.isPresent()) {
            throw new ApiException("Ce nom d'utilisateur n'est pas disponible !");
        }
        User newUser = this.userMapper.UserRegisterToEntity(user);
        this.userRepository.save(newUser);
        String token = this.jwtService.generateToken(newUser);
        return new UserAuthResponseDto(token);
    }

    @Override
    public UserAuthResponseDto loginUser(final UserLoginRequestDto user) {
        Optional<User> findUser = this.userRepository.findByNameOrEmail(user.name(), user.name());
        if (findUser.isEmpty()) {
            throw new ApiException("Adresse email ou nom d'utilisateur non trouvé !");
        }
        boolean isLoginCorrect = this.bCryptPasswordEncoder.matches(user.password(), findUser.get().getPassword());
        if (!isLoginCorrect) {
            throw new ApiException("Adresse email/Nom d'utilisateur/Mot de passe incorrect !");
        }
        String token = this.jwtService.generateToken(findUser.get());
        return new UserAuthResponseDto(token);
    }

    @Override
    public GetUserResponseDto getUser(final JwtAuthenticationToken token) {
        int userId = Integer.parseInt(token.getToken().getSubject());
        Optional<User> user = this.userRepository.findById(userId);
        if (user.isPresent()) {
            return this.userMapper.toGetUserResponseDto(user.get());
        } else {
            throw new ApiException("Cet utilisateur n'existe pas");
        }
    }

    @Override
    public GetUserResponseDto updateUser(final JwtAuthenticationToken token, final UserUpdateRequestDto user) {
        int userId = Integer.parseInt(token.getToken().getSubject());
        Optional<User> foundUser = this.userRepository.findById(userId);
        if (foundUser.isPresent()) {
            User emailAlreadyExist = this.userRepository.findByEmail(user.email()).orElse(null);
            User nameAlreadyExist = this.userRepository.findByName(user.username()).orElse(null);

            if (!Objects.equals(foundUser.get().getEmail(), user.email()) && emailAlreadyExist != null) {
                throw new ApiException("Cette Adresse email n'est pas disponible");
            }

            if (!Objects.equals(foundUser.get().getName(), user.username()) && nameAlreadyExist != null) {
                throw new ApiException("Ce nom d'utilisateur n'est pas disponible");
            }

            User updatedUser = this.userMapper.toUpdatedUser(foundUser.get(), user);

            this.userRepository.save(updatedUser);
            return this.userMapper.toGetUserResponseDto(updatedUser);

        } else {
            throw new ApiException("Cet utilisateur n'existe pas");
        }
    }
}
