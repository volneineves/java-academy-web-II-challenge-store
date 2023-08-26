package com.ada.avanadestore.service;

import com.ada.avanadestore.dto.LoginDTO;
import com.ada.avanadestore.dto.TokenDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import static com.ada.avanadestore.constants.ErrorMessages.INVALID_CREDENTIALS;

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
            UserDetails userDetails = (UserDetails) authManager.authenticate(authenticationToken).getPrincipal();
            return jwtService.generateToken(userDetails);
        } catch (Exception e) {
            throw new BadCredentialsException(INVALID_CREDENTIALS);
        }
    }
}
