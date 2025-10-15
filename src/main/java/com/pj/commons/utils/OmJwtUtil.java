package com.pj.commons.utils;

import com.pj.commons.dto.OmUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.security.Key;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class OmJwtUtil implements Serializable {

    @Value("${app.token.user}")
    private long JWT_TOKEN_USER;

    @Value("${app.token.pre}")
    private long JWT_TOKEN_PRE;

    @Value("${app.token.refresh}")
    private long JWT_TOKEN_REFRESH;

    @Value("${app.token.process}")
    private long JWT_TOKEN_PROCESS;

    @Value("${app.jwt.secret}")
    private String secret;

    private static Logger log = LoggerFactory.getLogger(OmJwtUtil.class);

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public OmUser getClaim(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return OmUser.builder()
                .userName(claims.getSubject())
                .uuid(UUID.fromString(claims.get("uuid", String.class)))
                .email(claims.get("email", String.class))
                .origin(OmCommons.getOrigin(claims))
                .issued(claims.getIssuedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build();
    }

    public OmUser getAllClaim(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        var claimOrigin = OmCommons.getOrigin(claims);
        var user = OmUser.builder()
                .userName(claims.getSubject())
                .uuid(UUID.fromString(claims.get("uuid", String.class)))
                .email(claims.get("email", String.class))
                //.organizationUuid(UUID.fromString(claims.get("organizationUuid", String.class)))
                .origin(claimOrigin)
                .issued(claims.getIssuedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build();
        var organization = claims.get("organizationUuid", String.class);
        if (organization != null) {
            user.setOrganizationUuid(UUID.fromString(organization));
        }
        return user;
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
        return Jwts.parser().setSigningKey(secret.getBytes()).build().parseSignedClaims(token).getPayload();
    }

    //check if the token has expired
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateTempToken(OmUser user, OmCommons.PLATFORM_ORIGIN origin) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userName", user.getUserName());
        claims.put("temp", true);
        claims.put("uuid", user.getUuid());
        claims.put("registryCompleted", user.getRegistryCompleted());
        claims.put("origin", origin);
        return doGenerateToken(claims, user.getUserName(), new Date(System.currentTimeMillis() + JWT_TOKEN_PRE * 1000));
    }

    //generate token for user
    public String generateToken(OmUser userDetails, UUID organizationUuid) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userName", userDetails.getUserName());
        claims.put("email", userDetails.getEmail());
        claims.put("phone", userDetails.getPhone());
        claims.put("registryCompleted", userDetails.getRegistryCompleted());
        claims.put("fullName", userDetails.getPerson() != null ? userDetails.getPerson().getFullName() : "");
        claims.put("uuid", userDetails.getUuid());
        claims.put("temp", false);
        claims.put("organizationUuid", organizationUuid);
        claims.put("origin", OmCommons.PLATFORM_ORIGIN.TIMEST);
        return doGenerateToken(claims, userDetails.getUserName(), new Date(System.currentTimeMillis() + JWT_TOKEN_USER * 1000));
    }

    public String generateProcessToken(OmUser usersBean) {
        var claims = new HashMap<String, Object>();
        claims.put("userName", usersBean.getUserName());
        claims.put("uuid", usersBean.getUuid());
        claims.put("process", true);
        claims.put("origin", OmCommons.PLATFORM_ORIGIN.TIMEST);
        return doGenerateToken(claims, usersBean.getUserName(), new Date(System.currentTimeMillis() + JWT_TOKEN_PROCESS * 1000));
    }

    public String generateRefreshToken(OmUser user, OmCommons.PLATFORM_ORIGIN origin) {
        var claims = new HashMap<String, Object>();
        claims.put("uuid", user.getUuid());
        if (user.getOrganizationUuid() != null)
            claims.put("organizationUuid", user.getOrganizationUuid());
        claims.put("refresh", true);
        claims.put("origin", origin);
        return doGenerateToken(claims, user.getUserName(), new Date(System.currentTimeMillis() + JWT_TOKEN_REFRESH * 1000));
    }

    public OmUser getClaimOfRefreshToken(String token, OmCommons.PLATFORM_ORIGIN orgin) {
        final Claims claims = getAllClaimsFromToken(token);
        var claimOrigin = OmCommons.getOrigin(claims);
        if (!claimOrigin.equals(orgin)) {
            log.error("Se intenta leer un token de un origen invalido {} -> {}", claimOrigin, orgin);
            throw new RuntimeException("Unknown origin");
        }
        var user = OmUser.builder()
                .userName(claims.getSubject())
                .uuid(UUID.fromString(claims.get("uuid", String.class)))
                .email(claims.get("email", String.class))
                .origin(claimOrigin)
                .build();
        var organization = claims.get("organizationUuid", String.class);
        if (organization != null) {
            user.setOrganizationUuid(UUID.fromString(organization));
        }
        return user;
    }

    public String generateBooktRefreshToken(UUID uuid, UUID organizationUuid) {
        var claims = new HashMap<String, Object>();
        claims.put("uuid", uuid);
        claims.put("refresh", true);
        claims.put("origin", OmCommons.PLATFORM_ORIGIN.BOOK);
        return doGenerateToken(claims, uuid.toString(), new Date(System.currentTimeMillis() + JWT_TOKEN_REFRESH * 1000));
    }

    public String generateBookToken(OmUser usersBean) {
        var claims = new HashMap<String, Object>();
        claims.put("userName", usersBean.getUserName());
        claims.put("uuid", usersBean.getUuid());
        claims.put("book", true);
        claims.put("origin", OmCommons.PLATFORM_ORIGIN.BOOK);
        return doGenerateToken(claims, usersBean.getUserName(), new Date(System.currentTimeMillis() + JWT_TOKEN_USER * 1000));
    }

    //while creating the token -
//1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
//2. Sign the JWT using the HS512 algorithm and secret key.
//3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
//   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject, Date tokenValidity) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = secret.getBytes();//DatatypeConverter.parseBase64Binary(secret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .expiration(tokenValidity)
                .issuedAt(new Date())
                .signWith(signatureAlgorithm, signingKey)
                .compact();
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
