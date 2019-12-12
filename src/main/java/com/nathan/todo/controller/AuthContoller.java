package com.nathan.todo.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nathan.todo.dto.AuthDto;
import com.nathan.todo.response.AuthRest;
import com.nathan.todo.request.UserLoginModel;
import com.nathan.todo.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthContoller {
	
	@Autowired
	AuthService authService;
	
	@PostMapping(path="/login",
			produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
			consumes= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
			)
	public AuthRest login(@RequestBody UserLoginModel userLoginModel) {
		AuthRest returnValue = new AuthRest();
		
		AuthDto authDto =  new AuthDto();
		
		BeanUtils.copyProperties(userLoginModel, authDto);
		
		AuthDto accessCredentials = authService.login(authDto);
		
		BeanUtils.copyProperties(accessCredentials, returnValue);

		return returnValue;
	}
}
