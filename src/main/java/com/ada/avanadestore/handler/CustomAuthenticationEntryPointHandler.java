package com.ada.avanadestore.handler;

import com.ada.avanadestore.dto.StandardErrorDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.SignatureException;
import java.util.Date;

import static com.ada.avanadestore.constants.Messages.AUTHENTICATION_INVALID;

@Component
public class CustomAuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationEntryPointHandler.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        LOGGER.error("{}: {}", authException.getClass(), authException.getMessage());
        StandardErrorDTO error = new StandardErrorDTO(401, AUTHENTICATION_INVALID, request.getRequestURI(), request.getMethod(), new Date().toString());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(error.toString());
    }
}
