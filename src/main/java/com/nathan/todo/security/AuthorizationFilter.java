package com.nathan.todo.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.nathan.todo.shared.JwtProvider;

import io.jsonwebtoken.Jwts;

public class AuthorizationFilter extends BasicAuthenticationFilter{

	@Autowired
	JwtProvider jwtProvider;
	
	public AuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest req,
									HttpServletResponse res,
									FilterChain chain) throws IOException, ServletException {
		String header = req.getHeader("Authorization");
		
		if(header == null || !header.startsWith("Bearer") ) {
			chain.doFilter(req, res);
			return;
		}
		
		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		
		if(token != null) {
			token = token.replace("Bearer ", "");
			
			String user = Jwts.parser()
					.setSigningKey("h827dedolo39hf0hs")
					.parseClaimsJws(token)
					.getBody()
					.getSubject();
			
			if(user != null) {
				return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
			}
		}
		
		return null;
	}
	

}
