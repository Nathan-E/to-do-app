package com.nathan.todo.service;

import java.util.List;

//import java.util.List;

//import org.springframework.security.core.userdetails.UserDetailsService;

import com.nathan.todo.dto.UserDto;

public interface UserService  {
	UserDto createUser(UserDto userDto);

	UserDto getUserByUserId(String id);

	List<UserDto> getAllUsers(int page, int limit);
}

//extends UserDetailsService