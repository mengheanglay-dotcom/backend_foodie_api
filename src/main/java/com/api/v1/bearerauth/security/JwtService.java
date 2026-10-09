package com.api.v1.bearerauth.security;

import com.api.v1.bearerauth.common.response.TokenResponse;
import com.api.v1.bearerauth.config.AppProperty;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtService {
    private final AppProperty appProperty;
    private final SecretKey signingKey;

    public JwtService(AppProperty appProperty) {
        this.appProperty = appProperty;
        this.signingKey = Keys.hmacShaKeyFor(appProperty.getJwt().getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public TokenResponse generateAccess(UserDetails userDetails) {
        Date issuedAt = new Date();
        Date expiredAt = new Date(issuedAt.getTime() + appProperty.getJwt().getAccessExpiration());
        String access = createToken(userDetails.getUsername(), "access", appProperty.getJwt().getAccessExpiration());
        String refresh = createToken(userDetails.getUsername(), "refresh", appProperty.getJwt().getRefreshExpiration());
        return TokenResponse.builder().access(access).refresh(refresh).issuedAt(issuedAt).expiredAt(expiredAt).build();
    }

    public String generateAccessOnly(UserDetails userDetails) {
        return createToken(userDetails.getUsername(), "access", appProperty.getJwt().getAccessExpiration());
    }

    public String generateRefreshOnly(UserDetails userDetails) {
        return createToken(userDetails.getUsername(), "refresh", appProperty.getJwt().getRefreshExpiration());
    }

    public Date refreshExpiration() {
        return new Date(System.currentTimeMillis() + appProperty.getJwt().getRefreshExpiration());
    }

    private String createToken(String subject, String type, long expiration) {
        Date now = new Date();
        return Jwts.builder()
                .claim("type", type)
                .subject(subject)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiration))
                .signWith(signingKey, Jwts.SIG.HS256)
                .compact();
    }

    private Claims extractAllClaim(String token) {
        return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaim(token));
    }

    public String extractUsername(String token) { return extractClaim(token, Claims::getSubject); }

    public boolean validateAccessToken(String token, UserDetails userDetails) {
        return validate(token, userDetails, "access");
    }

    public boolean validateRefreshToken(String token, UserDetails userDetails) {
        return validate(token, userDetails, "refresh");
    }

    private boolean validate(String token, UserDetails userDetails, String type) {
        try {
            String username = extractUsername(token);
            String tokenType = extractClaim(token, c -> c.get("type", String.class));
            Date expiration = extractClaim(token, Claims::getExpiration);
            return type.equals(tokenType) && username.equals(userDetails.getUsername()) && expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
