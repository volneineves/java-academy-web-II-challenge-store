package com.ada.avanadestore.service;

import com.ada.avanadestore.dto.TokenDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    public TokenDTO generateToken(UserDetails userDetails) {
        String token = "Bearer " + Jwts.builder().setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512).compact();

        return new TokenDTO("access_token", token);
    }

    private Key getSigningKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }
}