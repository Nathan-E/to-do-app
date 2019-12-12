package com.nathan.todo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nathan.todo.entity.TaskEntity;
import com.nathan.todo.entity.UserEntity;

@Repository
public interface TaskRepository extends PagingAndSortingRepository<TaskEntity, Long>{

	TaskEntity findByTaskId(String taskId);
	TaskEntity findAllByTaskId(String taskId);

	@Query(value="select * from Tasks t where t.status = :status",
			countQuery="select count(*) from Tasks t where t.status = :status",
			nativeQuery=true)
	Page<TaskEntity> getAllTaskByStatus(Pageable pageableRequest, @Param("status") String status);
	
	/**************************** JPQL *************************/
	@Query("select t from TaskEntity t where t.userDetails = :userEntity")
	Page<TaskEntity> getAllUserTask(Pageable pageableRequest, @Param("userEntity")UserEntity userEntity);
}
