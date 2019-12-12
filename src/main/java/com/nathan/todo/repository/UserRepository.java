package com.nathan.todo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.nathan.todo.entity.UserEntity;


@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
	
	/******************** JPA *********************/
	UserEntity findByEmail(String email);
	UserEntity findByUserId(String id);
	
	@Query(value="select * from Users",
			countQuery="select count(*) from Users",
			nativeQuery=true)
	Page<UserEntity> getAllUsers(Pageable pageableRequest);
}
