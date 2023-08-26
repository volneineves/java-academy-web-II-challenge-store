package com.ada.avanadestore.filter;

import com.ada.avanadestore.dto.StandardErrorDTO;
import com.ada.avanadestore.handler.CustomExceptionHandler;
import com.ada.avanadestore.service.AuthService;
import com.ada.avanadestore.service.JwtService;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

import static com.ada.avanadestore.constants.ErrorMessages.AUTHENTICATION_INVALID;

@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);
    private final JwtService jwtService;
    private final AuthService authService;

    public AuthenticationTokenFilter(JwtService jwtService, AuthService authService) {
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null) {
                String token = authHeader.replace("Bearer", "").trim();
                Boolean isValidToken = jwtService.isValidToken(token);

                if (isValidToken) {
                    String username = jwtService.getUsernameByToken(token);
                    UserDetails user = authService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), null, null);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (SignatureException | AuthenticationException exception) {
            handleException(response, request, exception);
        }
    }

    private void handleException(HttpServletResponse response, HttpServletRequest request, Exception exception) throws IOException {
        LOGGER.error("{}: {}", exception.getClass(), exception.getMessage());
        StandardErrorDTO error = new StandardErrorDTO(401, AUTHENTICATION_INVALID, request.getRequestURI(), request.getMethod(), new Date().toString());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(error.toString());
    }
}
