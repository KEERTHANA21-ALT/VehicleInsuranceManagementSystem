package com.springboot.insurance.utility;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtUtility {


    private String SECRET_KEY = "secret";
    // setting expiration date, it is optional
    private final long expiry_time= 1*24*60*60*1000;  // it is like for one day 24 hrs 60 min 60 sec 1000 ms

    // Extracts the username from the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // This method is used by Spring to check if the token is expired or valid
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Spring uses this method to check if the incoming token is the same that was given using claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // This extracts all the claims having tokens that were given away to UI clients
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(SECRET_KEY.getBytes())).parseClaimsJws(token).getBody();
    }

    // This is to check if the token has expired or is still valid
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // This is the method we call from jwtFilter to generate token
    public String generateToken(String username) { //We use this
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    // This method creates a token
    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // This is the username
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiry_time))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                .compact();
    }

    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

}
