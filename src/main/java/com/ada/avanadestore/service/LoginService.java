package com.ada.avanadestore.service;

import com.ada.avanadestore.dto.LoginDTO;
import com.ada.avanadestore.dto.TokenDTO;
import com.ada.avanadestore.entitity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import static com.ada.avanadestore.constants.Messages.INVALID_CREDENTIALS;

@Service
public class LoginService {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public LoginService(AuthenticationManager authManager, JwtService jwtService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    public TokenDTO login(LoginDTO dto) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
            User userDetails = (User) authManager.authenticate(authenticationToken).getPrincipal();
            return jwtService.generateToken(userDetails);
        } catch (Exception e) {
            throw new BadCredentialsException(INVALID_CREDENTIALS);
        }
    }
}
