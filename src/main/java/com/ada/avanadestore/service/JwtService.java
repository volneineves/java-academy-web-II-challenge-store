package com.ada.avanadestore.service;

import com.ada.avanadestore.dto.TokenDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.expTime}")
    private String expTime;

    public TokenDTO generateToken(UserDetails userDetails) {

        Date actualDate = new Date();
        Date expDate = new Date(actualDate.getTime() + Long.parseLong(expTime));

        String token = "Bearer " + Jwts.builder().setSubject(userDetails.getUsername())
                .setIssuedAt(actualDate)
                .setExpiration(expDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512).compact();

        return new TokenDTO("access_token", token);
    }

    private Key getSigningKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }
}