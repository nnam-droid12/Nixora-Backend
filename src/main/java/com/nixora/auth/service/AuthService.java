package com.nixora.auth.service;



import com.nixora.auth.entities.User;
import com.nixora.auth.repository.UserRepository;
import com.nixora.auth.utils.AuthResponse;
import com.nixora.auth.utils.LoginRequest;
import com.nixora.auth.utils.RegisterRequest;
import com.nixora.loan.document.exception.UserAlreadyExistException;
import com.nixora.loan.document.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;


    public AuthService(PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       RefreshTokenService refreshTokenService,
                       UserRepository userRepository,
                       AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;



    }


    public AuthResponse register(RegisterRequest registerRequest){

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("User Already Exist in database.");
        }

        var user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();
        User savedUser = userRepository.save(user);

        var accessToken = jwtService.generateToken(savedUser);

        var refreshToken = refreshTokenService.createRefreshToken(savedUser.getEmail());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }

    public AuthResponse login(LoginRequest loginRequest){

        if (!userRepository.findByEmail(loginRequest.getEmail()).isPresent()) {
            throw new UserNotFoundException("User does not exist, please signup");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        var accessToken = jwtService.generateToken(user);

        var refreshToken = refreshTokenService.createRefreshToken(loginRequest.getEmail());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }

    public void logout(String token) {
        if (token == null || token.isBlank()) {
            throw new RuntimeException("Access Token cannot be empty or blank.");
        }

        try {
            jwtService.addToBlocklist(token);
            System.out.println("User logged out successfully: " + jwtService.extractUsername(token));
        } catch (Exception e) {
            throw new RuntimeException("Logout failed for token: " + token);
        }
    }


}



