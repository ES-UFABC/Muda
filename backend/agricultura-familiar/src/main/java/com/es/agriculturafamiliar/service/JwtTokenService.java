package com.es.agriculturafamiliar.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.es.agriculturafamiliar.entity.JwtToken;
import com.es.agriculturafamiliar.exception.AuthException;
import com.es.agriculturafamiliar.exception.TokenException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenService implements ITokenService {

    @Value("${security.jwt.signing-key}")
    private String SECRET_KEY;

    @Value("${security.jwt.token-duration-in-minutes}")
    private Long TOKEN_VALIDITY;


    @Override
    public JwtToken generateToken(UserDetails userDetails) {      
        Instant issuetAt = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expiration = issuetAt.plus(TOKEN_VALIDITY, ChronoUnit.MINUTES);

        Map<String, Object> claims = new HashMap<>();

        String token = Jwts.builder()
            .setClaims(claims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(Date.from(issuetAt))
            .setExpiration(Date.from(expiration))
            .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
            .compact();

        return JwtToken.builder()
            .token(token)
            .expirationDate(expiration.toString())
            .build();
    }

    @Override
    public String getUsernameFromToken(String token) {        
        return getClaimsFromToken(token, Claims::getSubject);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) throws AuthException {
        final String username = getUsernameFromToken(token);
        boolean isValidUsername = username.equals(userDetails.getUsername());

        if (!isValidUsername || isTokenExpired(token)) {
            throw new TokenException();
        }

        return true;
    }

    private boolean isTokenExpired(String token) {
        final Date expirationDate = getClaimsFromToken(token, Claims::getExpiration);
        final Date now = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        return expirationDate.before(now);
    }

    private <T> T getClaimsFromToken(String token, Function<Claims, T> claimCallback) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimCallback.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }
    
}
