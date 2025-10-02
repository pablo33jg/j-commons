package com.pj.commons.utils;

import com.pj.commons.dto.UsuariosBean;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

//@Component
public class JwtUtil implements Serializable {

    @Value("${app.jwt.seconds-expire}")
    private long JWT_TOKEN_VALIDITY;

    @Value("${app.jwt.secret}")
    private String secret;

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public UsuariosBean getClaim(String token){
        final Claims claims = getAllClaimsFromToken(token.replace(",ROLE_ANONYMOUS", ""));
        return UsuariosBean.builder()
                .userName(claims.getSubject())
                .id(claims.get("id", Integer.class))
                .almacenId(claims.get("almacenId", Integer.class))
                .vendedorId(claims.get("vendedorId", Integer.class))
                .entidadId(claims.get("entidadId", Integer.class))
                .ejecutivoId(claims.get("ejecutivoId", Integer.class)).build();
    }
    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    //for retrieveing any information from token we will need the secret key
    public Claims getAllClaimsFromToken(String token) {
        //return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        //return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secret)).build().parseSignedClaims(token).getPayload();

        return Jwts.parser().setSigningKey(secret.getBytes()).build().parseSignedClaims(token).getPayload();
    }

    //check if the token has expired
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    //generate token for user
    /*public String generateToken(Usuarios userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userDetails.getId());
        claims.put("entidadId", userDetails.getEntidadId());
        claims.put("almacenId", userDetails.getAlmacenId());
        claims.put("ejecutivoId", userDetails.getEjecutivoId());
        claims.put("vendedorId", userDetails.getVendedorId());
        return doGenerateToken(claims, userDetails.getUserName());
    }*/
    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    /*private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }*/

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(signatureAlgorithm, signingKey)
                .compact();
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
