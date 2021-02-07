package org.kuehnenagel.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class for generic JWT manipulation
 */
@Component
public class JwtUtil {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration-minutes}")
    private long expirationMinutes;
    
    public String generateToken(final String username) {
        return createToken(new HashMap<>(), username);
    }
    
    public String getUsername(final String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    /**
     * Extracts any claim from token and <b>also verifies signature</b>
     * @param token valid JWT
     * @param claimProc function for claim processing
     * @param <T> claim type
     * @return extracted claim
     * @throws io.jsonwebtoken.SignatureException in case of invalid signature
     */
    public <T> T extractClaim(final String token, Function<Claims, T> claimProc) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claimProc.apply(claims);
    }
    
    /**
     * Creating token with claims and subject
     * @param claims e.g iss, aud
     * @param subject usually must be unique (user id or unique username)
     * @return signed JWT
     */
    private String createToken(final Map<String, Object> claims, String subject) {
        long currentMillis = System.currentTimeMillis();
        long expiresAt = currentMillis + expirationMinutes * 60_000;
        logger.info("Token expiration millis: {}", expiresAt);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(currentMillis))
                .setExpiration(new Date(expiresAt))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
}
