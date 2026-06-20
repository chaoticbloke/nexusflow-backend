package io.canduer.nexusflow.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import io.jsonwebtoken.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecretKey;

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> customClaims = new HashMap<>();
        /*
        customClaims eg-
        {
          "role": "ROLE_ADMIN",
          "userId": "123",
          "department": "engineering"
        }

        currently JWT payload -
        {
          "sub": "aditya@gmail.com",
          "iat": 171000000,
          "exp": 1710003600
        }
        these are claims.
         */
       return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(customClaims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1000 * 60 * 60))
                .signWith(getSignInKey(jwtSecretKey))
                .compact();
    }

    private SecretKey getSignInKey(String jwtSecretKey) {
        byte[] decode = Decoders.BASE64.decode(jwtSecretKey);
        return Keys.hmacShaKeyFor(decode);
    }

    public String extractUsername(String token) throws SignatureException, MalformedJwtException {
       return Jwts.parser().verifyWith(getSignInKey(jwtSecretKey)).build()
                .parseSignedClaims(token)
               .getPayload()
               .getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) throws SignatureException , MalformedJwtException{
        Date expiration = extractExpiration(token);

        Date currentDate = new Date();

        boolean isTokenValid = currentDate.before(expiration);


        return extractUsername(token).equalsIgnoreCase(userDetails.getUsername()) && isTokenValid;
    }

    public Date extractExpiration(String token) throws SignatureException , MalformedJwtException {
        Date expiration = Jwts.parser().verifyWith(getSignInKey(jwtSecretKey)).build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        return expiration;
    }

}
