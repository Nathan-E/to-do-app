package com.nathan.todo.shared;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider {
	public String generateUserToken(String email) {
		System.out.println(email);
		String token = Jwts.builder()
				.setSubject(email)
				.setExpiration(new Date(System.currentTimeMillis() + 864000000))
				.signWith(SignatureAlgorithm.HS512, "h827dedolo39hf0hs")
				.compact();
		
		return token;
	}
	
	public String decodeUserToken(String token) {
		return Jwts.parser()
			.setSigningKey("h827dedolo39hf0hs")
			.parseClaimsJws(token)
			.getBody()
			.getSubject();
	}
	
//	public static boolean hasTokenExpired(String token) {
//		boolean returnValue = true;
//		try {
//			Claims claims = Jwts.parser().setSigningKey(SecurityConstants.getTokenSecret()).parseClaimsJws(token)
//					.getBody();
//
//			Date tokenExpirationDate = claims.getExpiration();
//			Date todayDate = new Date();
//
//			returnValue = tokenExpirationDate.before(todayDate);
//		} catch (ExpiredJwtException ex) {
//			returnValue = true;
//		}
//		return returnValue;
//	}

}
