package com.nixora.auth.service;


import com.nixora.auth.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class JwtService {



    @Autowired
    private  RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.key}")
    private String secretKey;


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // extract information from JWT
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // decode and get the key
    private Key getSignInKey() {
        // decode SECRET_KEY
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String generateToken(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getName());
        extraClaims.put("userId", user.getUserId());
        return generateToken(extraClaims, user);
    }

    // generate token using Jwt utility class and return token as String
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() +  2 *60*60* 1000))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // if token is expired
    private void validateTokenExpiration(String token) {
        Date expirationDate = extractExpiration(token);
        if (expirationDate.before(new Date())) {
            throw new RuntimeException("Access token has expired.");
        }
    }


    public boolean isTokenValid(String token, UserDetails userDetails) {

        if (isTokenInBlocklist(token)) {
            return false;
        }

        validateTokenExpiration(token);

        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }


    private Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);
    }



    public void addToBlocklist(String token) {
        redisTemplate.opsForValue().set(token, "BLOCKED", 2, TimeUnit.HOURS);
    }

    public boolean isTokenInBlocklist(String token) {
        return redisTemplate.hasKey(token);
    }


}



