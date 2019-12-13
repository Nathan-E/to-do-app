package com.nathan.todo.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nathan.todo.SpringApplicationContext;
import com.nathan.todo.request.UserLoginRequestModel;
import com.nathan.todo.service.UserService;
import com.nathan.todo.shared.JwtProvider;
import com.nathan.todo.dto.UserDto;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	private final AuthenticationManager authenticationManager;
	
	JwtProvider jwtProvider;
	
	public AuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req,
												HttpServletResponse res) throws AuthenticationException {
		try {
			UserLoginRequestModel creds = new ObjectMapper()
					.readValue(req.getInputStream(), UserLoginRequestModel.class);
			
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							creds.getEmail(),
							creds.getPassword(),
							new ArrayList<>()
							)
					);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
				
	@Override
	protected void successfulAuthentication(HttpServletRequest req,
											HttpServletResponse res,
											FilterChain chain,
											Authentication auth) throws IOException, ServletException {
		
		String userName = ((User) auth.getPrincipal()).getUsername();
		System.out.println(userName);
		
		String token = Jwts.builder()
				.setSubject(userName)
				.setExpiration(new Date(System.currentTimeMillis() + 864000000))
				.signWith(SignatureAlgorithm.HS512, "h827dedolo39hf0hs")
				.compact();
		
		System.out.println(token);
		UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImpl"); 
		UserDto userDto = userService.getUser(userName);
		
		res.addHeader("Authorization", "Bearer " + token);
		res.addHeader("UserID", userDto.getUserId());	
	}
}
