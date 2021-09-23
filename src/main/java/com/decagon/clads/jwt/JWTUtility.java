package com.decagon.clads.jwt;

import com.decagon.clads.entities.artisan.Artisan;
import com.decagon.clads.model.dto.ArtisanDTO;
import com.fasterxml.jackson.core.JsonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@NoArgsConstructor
@Component
public class JWTUtility implements Serializable {

    private static final long serialVersionUID = 234234523523L;

    public static final long JWT_TOKEN_VALIDITY = 24*60*60*1000L;


    @Value("${jwt.secret}")
    public String secretKey;

    //retrieve username from jwt token
    public String getEmailAddressFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve username from jwt token
    public String isEnabled(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve id from jwt token
    public Long getIdFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return Long.valueOf(String.valueOf(claims.get("id"))) ;
    }
    //retrieve username from jwt token
    public String getPasswordFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return (String) claims.get("password");
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }


    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    //for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }


    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


    //generate token for user
    public String generateToken(Artisan artisan) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", artisan.getRole());
        claims.put("id", artisan.getId());
        return doGenerateToken(claims, artisan.getUsername());
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getEmailAddressFromToken(token);
//        final String password = getPasswordFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}