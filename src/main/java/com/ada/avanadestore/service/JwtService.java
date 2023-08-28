package com.ada.avanadestore.service;

import com.ada.avanadestore.dto.TokenDTO;
import com.ada.avanadestore.entitity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final Key signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Value("${jwt.expTime}")
    private String expTime;

    public TokenDTO generateToken(User user) {

        Date actualDate = new Date();
        Date expDate = new Date(actualDate.getTime() + Long.parseLong(expTime));

        String token = "Bearer " + Jwts.builder().setSubject(user.getUsername())
                .setIssuedAt(actualDate)
                .setExpiration(expDate)
                .signWith(signingKey, SignatureAlgorithm.HS512).compact();

        return new TokenDTO(user.getId(), user.getUserType(), "access_token", token);
    }

    public String getUsernameByToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Boolean isValidToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .isSigned(token);
    }
}