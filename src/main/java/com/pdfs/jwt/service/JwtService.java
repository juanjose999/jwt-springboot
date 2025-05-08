package com.pdfs.jwt.service;

import com.pdfs.jwt.entity.Jwt;
import com.pdfs.jwt.repository.JwtRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {

    private final Long EXPIRATION_TIME_ACCESS = TimeUnit.MINUTES.toSeconds(10);
    private final Long EXPIRATION_TIME_REFRESH = TimeUnit.DAYS.toSeconds(16);
    private final JwtRepository jwtRepository;

    public JwtService(JwtRepository jwtRepository) {
        this.jwtRepository = jwtRepository;
    }

    public static KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);

    static {
        try {
            Path dir = Path.of("keys");
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
            Path publicPath = Path.of("keys/public.pem");
            Path privatePath = Path.of("keys/private.pem");
            Files.deleteIfExists(publicPath);
            Files.deleteIfExists(privatePath);

            String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
            String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

            // Formato PEM para la pública
            String publicKeyPem = "-----BEGIN PUBLIC KEY-----\n"
                    + splitLines(publicKey)
                    + "-----END PUBLIC KEY-----\n";

            // Formato PEM para la privada
            String privateKeyPem = "-----BEGIN PRIVATE KEY-----\n"
                    + splitLines(privateKey)
                    + "-----END PRIVATE KEY-----\n";

            // Guardar los archivos
            Files.writeString(Path.of("keys/public_key.pem"), publicKeyPem);
            Files.writeString(Path.of("keys/private_key.pem"), privateKeyPem);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static String splitLines(String base64) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < base64.length(); i+=64) {
            int end = Math.min(i+64, base64.length());
            sb.append(base64, i, end).append("\n");
        }
        return sb.toString();
    }

    private String generateJwt(String email, Long expirationTime, String ipUser) {
        return Jwts.builder()
                .setClaims(Map.of("ipUser",ipUser))
                .setSubject(email)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(expirationTime)))
                .signWith(keyPair.getPrivate())
                .compact();
    }

    public String generateJwtAccessToken(String email, String ipUser) {
        return generateJwt(email, EXPIRATION_TIME_ACCESS, ipUser);
    }

    public String generateJwtRefreshToken(String email, String ipUser) {
        return generateJwt(email, EXPIRATION_TIME_REFRESH, ipUser);
    }

    private Claims getClaims(String jwt){
        return Jwts.parserBuilder()
                .setSigningKey(keyPair.getPublic())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public  String getJit(String jwt){
        return getClaims(jwt).getId();
    }

    public String getEmailFromJwt(String jwt){
        return getClaims(jwt).getSubject();
    }

    public boolean validateJwt(String jwt){
        return  validExpiration(jwt);
    }

    public boolean validateJwtRefresh(String jwt){
        return tokenExists(jwt) && jwtIsActive(jwt);
    }

    private boolean validExpiration(String jwt){
        return getClaims(jwt).getExpiration().after(new Date());
    }

    private boolean jwtIsActive(String jwt){
        return jwtRepository.findByToken(jwt).get().active();
    }

    public boolean tokenExists(String jwt){
        return jwtRepository.findByToken(jwt).isEmpty();
    }
}
