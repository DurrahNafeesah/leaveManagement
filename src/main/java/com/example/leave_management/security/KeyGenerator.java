package com.example.leave_management.security;

import io.jsonwebtoken.security.Keys;

import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        byte[] key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256).getEncoded();
        System.out.println("Secret Key: " + Base64.getEncoder().encodeToString(key));
    }
}
