package com.example.airlineticket.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {
    //Tao ra khoa bi mat chi phia server biet
    private final String secret_key = "aaaaaaaaaaaaaaaaaaaaaaacbbbbbbbbbbbbbbbbbcccccccccccccccc";

    //Thoi gian hieu luc cua jwt
    private final long expiration = 604800000L;
    //Tao ra token
    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expiration);
        return Jwts
                .builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256,secret_key.getBytes())
                .compact();
    }
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret_key.getBytes()).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret_key.getBytes()).parseClaimsJws(token).getBody();
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}
