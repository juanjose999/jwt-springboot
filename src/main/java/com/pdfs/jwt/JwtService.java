package com.pdfs.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {

    private final Long EXPIRATION_TIME = TimeUnit.MINUTES.toSeconds(10);

    //firma simetrica
    //private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    //firma asimetrica
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

    public String generateJwt(String email){
        return Jwts.builder()
                .setSubject(email)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(EXPIRATION_TIME)))
                .signWith(keyPair.getPrivate())
                .compact();
    }

    private Claims getClaims(String jwt){
        return Jwts.parserBuilder()
                .setSigningKey(keyPair.getPublic())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public String getEmailFromJwt(String jwt){
        String email = getClaims(jwt).getSubject();
        return email;
    }

    public boolean validateJwt(String jwt){
        return getClaims(jwt).getExpiration().after(new Date())
                && getClaims(jwt).getIssuedAt().before(new Date());
    }

}
