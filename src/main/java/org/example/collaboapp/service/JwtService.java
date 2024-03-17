package org.example.collaboapp.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {


    private static final String SECRET_KEY="VPrCmQDum6v8Got2wkgyq8Ohx6kzUpo8CJeL0hM/PkLyF3Emzz7eqKSdS1Z6nMs1AJoJBJEOLeOPg8hViSxrIFtL3/0TIBkBAjQn0hToztnRLZIi9KW/gz8jjmVOWIPiFi8kAKhrfh/EOGCbncasiS7BN5YYfzn6ZOTTADk66BtMyVsTfpdJf4FiN6guK3APwNwsZiwhrG2gAomRwD5v6SOqut4bAK20o3z3D9Kge66A3d7Ou7wiVT89iJmr30tILMWK2O1vceBLeF3MI5NCIUFqi9KAhIEj/uuHexN+U88yM5pwr3ieXWc1sW6QoumUgBLoRB0BtkxDc2ye8QgEZA==";



    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }


    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    private Claims extractAllClaims(String jwt){
        return Jwts
                .parserBuilder()
                .setSigningKey( getSigninKey() )
                .build().parseClaimsJws(jwt)
                .getBody();
    }

    public String generateToken(UserDetails userDetails){
        return generateToken( new HashMap<>() , userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ){


        // Convert the user's roles to a comma-separated string
        String roles = userDetails.getAuthorities().stream()
                .map( GrantedAuthority::getAuthority)
                .collect( Collectors.joining(","));

        // Add roles to the claims
        extraClaims.put("roles", roles);


        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt( new Date(System.currentTimeMillis()) )
                .setExpiration( new Date(System.currentTimeMillis() + 1000*60*24))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Key getSigninKey() {

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);

        return Keys.hmacShaKeyFor(keyBytes);
    }

}
