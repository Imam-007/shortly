package com.url.shortener.services;

import com.url.shortener.dto.LoginRequest;
import com.url.shortener.models.User;
import com.url.shortener.security.jwt.JwtAuthenticationResponse;

public interface UserService {
    public User registerUser(User user);

    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest);

    public User findByUsername(String name);
}
