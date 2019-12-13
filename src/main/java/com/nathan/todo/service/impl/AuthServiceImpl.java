package com.nathan.todo.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nathan.todo.dto.AuthDto;
import com.nathan.todo.entity.UserEntity;
import com.nathan.todo.repository.UserRepository;
import com.nathan.todo.service.AuthService;
import com.nathan.todo.shared.JwtProvider;

@Service
public class AuthServiceImpl implements AuthService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	JwtProvider jwtProvider;
	
	@Override
	public AuthDto login(AuthDto authDto) {
		AuthDto returnValue = new AuthDto();
		
		UserEntity userEntity = userRepository.findByEmail(authDto.getEmail());
		
		if(userEntity.getPassword() != authDto.getPassword()) {
			
		}
		
		String token = jwtProvider.generateUserToken(userEntity.getEmail());
		
		BeanUtils.copyProperties(userEntity, returnValue);
		
		returnValue.setToken(token);
		returnValue.setUserId(userEntity.getUserId());
		
		return returnValue;
	}
}
