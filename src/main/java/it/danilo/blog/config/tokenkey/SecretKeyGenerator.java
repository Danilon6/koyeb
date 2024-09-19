package it.danilo.blog.config.tokenkey;

import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;

public class SecretKeyGenerator {
    public static void main(String[] args) {
        // Genera una chiave di 256 bit (32 byte) per HS256
        byte[] keyBytes = new byte[32];
        new java.security.SecureRandom().nextBytes(keyBytes);

        Key key = Keys.hmacShaKeyFor(keyBytes);
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("Generated Secret Key: " + base64Key);
    }
}