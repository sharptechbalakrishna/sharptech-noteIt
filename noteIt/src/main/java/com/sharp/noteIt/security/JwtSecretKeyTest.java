package com.sharp.noteIt.security;

import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;

import org.junit.Test;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtSecretKeyTest {

    public static void main(String[] args) {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String encodedKey = DatatypeConverter.printHexBinary(key.getEncoded());
        System.out.printf("\nKey = [%s]\n", encodedKey);
    }
}
//Key = [D4B6667451B6212C191507A1285E89EA6F4C34CDBEB7E5538B10A2BBBDBD2D021C9D7471218908179B66FD499BF5F1A173B754454ED5ED048ECF5DE1F3CEB168]

