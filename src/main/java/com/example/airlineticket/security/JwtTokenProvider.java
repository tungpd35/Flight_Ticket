package com.example.airlineticket.security;

import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
    public String getUsernameFromToken(HttpServletRequest request) {
        String token = getJwtFromRequest(request);
        Claims claims = Jwts.parser().setSigningKey(secret_key.getBytes()).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String token = getCookie(request,"token");
        // Kiểm tra xem header Authorization có chứa thông tin jwt không
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        } else if(token != "Cookie not found") {
            return token;
        }
        return null;
    }
    public String getCookie(HttpServletRequest request,String name) {
        // Lấy danh sách các cookie từ request
        Cookie[] cookies = request.getCookies();

        // Kiểm tra nếu danh sách cookie không null và không rỗng
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    String cookieValue = cookie.getValue();
                    return  cookieValue;
                }
            }
        }
        return "Cookie not found";
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
