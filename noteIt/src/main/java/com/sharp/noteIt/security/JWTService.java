package com.sharp.noteIt.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import javax.xml.bind.DatatypeConverter;

@Service
public class JWTService {

    private static final String SECRET_KEY = "4CCBD212EFD1E1E7CA30A4010BB4F6B730964454644A7DFD4B75C831F73D7C83CBD5E17D16849D3E7E0C9C3AF1DC131842F1B6EA1FDE4481C55D58EC6A846A65";

    private static final long TOKEN_VALIDITY = 10 * 60 * 60 * 1000; // 10 hours

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // Store phone number as username
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes()) // Sign with HS512 and secret key bytes
                .compact();
    }

    public String extractPhone(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String phone = extractPhone(token);
        return (phone.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes()); // Generate the key

        System.out.println("SECRET_KEY: " + DatatypeConverter.printHexBinary(SECRET_KEY.getBytes())); // Debugging

        return Jwts.parser()
                   .setSigningKey(key) // Use the key
                   .build()
                   .parseClaimsJws(token) // Parse the JWT
                   .getBody(); // Extract the Claims
    }
}
