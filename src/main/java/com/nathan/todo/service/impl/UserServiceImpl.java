package com.nathan.todo.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nathan.todo.dto.UserDto;
import com.nathan.todo.entity.RoleEntity;
import com.nathan.todo.entity.UserEntity;
import com.nathan.todo.repository.TaskRepository;
import com.nathan.todo.repository.UserRepository;
import com.nathan.todo.service.UserService;
import com.nathan.todo.shared.Utils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	Utils utils;

	@Override
	public UserDto createUser(UserDto user) {
		UserDto returnValue = new UserDto();

		UserEntity userEntity = new UserEntity();

		BeanUtils.copyProperties(user, userEntity);

		userEntity.setUserId(utils.generateRandomId(20));
		userEntity.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		
		UserEntity createdUser = userRepository.save(userEntity);
		BeanUtils.copyProperties(createdUser, returnValue);

		return returnValue;
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);

		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);

		return returnValue;
	}

	@Override
	public List<UserDto> getAllUsers(int page, int limit) {
		List<UserDto> returnValue = new ArrayList<>();

		if (page > 0)
			page--;

		Pageable pageableRequest = PageRequest.of(page, limit);

		Page<UserEntity> usersPage = userRepository.getAllUsers(pageableRequest);
		List<UserEntity> users = usersPage.getContent();

		for (UserEntity userEntity : users) {
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(userEntity, userDto);
			returnValue.add(userDto);
		}

		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(username);

		if (userEntity == null)
			throw new UsernameNotFoundException(username);

		return new User(userEntity.getEmail(), userEntity.getPassword(), new ArrayList<>());
	}

//	private static Collection<? extends GrantedAuthority> getAuthorities(UserEntity user) {
//		Set<String> roleAndPermissions = new HashSet<>();
////		List<RoleEntity> roles = user.getRoles();
//
//		for (RoleEntity role : roles) {
//			roleAndPermissions.add(role.getName());
//		}
//		String[] roleNames = new String[roleAndPermissions.size()];
//		Collection<GrantedAuthority> authorities = AuthorityUtils
//				.createAuthorityList(roleAndPermissions.toArray(roleNames));
//		return authorities;
//	}

	@Override
	public UserDto getUser(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);

		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);

		return returnValue;
	}
}
