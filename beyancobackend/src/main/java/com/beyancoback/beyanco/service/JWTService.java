package com.beyancoback.beyanco.service;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Service
public class JWTService {
	
	private String secreteKey = "";
	
	private JWTService() {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
			SecretKey secretKey = keyGenerator.generateKey();
			secreteKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException();
		}
	}

	public String generateToken(String username) {
		Map<String, Object> map = new HashMap<>();
		
		return Jwts.builder()
				.claims()
				.add(map)
				.subject(username)
				.issuedAt(new Date(System.currentTimeMillis()))
//				.expiration(new Date(System.currentTimeMillis() + 60 * 60 * 30))
				.expiration(new Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000)) // 2 hour
				.and()
				.signWith(getKey())
				.compact();
		
	}

	private SecretKey getKey() {
		byte[] bytekey = Decoders.BASE64.decode(secreteKey);
		return Keys.hmacShaKeyFor(bytekey);
	}

	public String extractUsername(String token) {
		return extractclaim(token,Claims::getSubject);
	}
	
	private <T> T extractclaim(String token,Function<Claims, T> claimresolver) {
		final Claims claims = extractAllClaim(token);
		return claimresolver.apply(claims);
	}

	private Claims extractAllClaim(String token) {
		return Jwts
				.parser()
				.verifyWith(getKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	public boolean validateToken(String token, UserDetails userdetails) {
		final String username = extractUsername(token);
		return (username.equals(userdetails.getUsername()) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractclaim(token, Claims::getExpiration);
	}

}
