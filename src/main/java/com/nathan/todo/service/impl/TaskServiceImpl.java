package com.nathan.todo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.nathan.todo.dto.TaskDto;
import com.nathan.todo.dto.UserDto;
import com.nathan.todo.entity.TaskEntity;
import com.nathan.todo.entity.UserEntity;
import com.nathan.todo.repository.TaskRepository;
import com.nathan.todo.repository.UserRepository;
import com.nathan.todo.service.TaskService;
import com.nathan.todo.shared.Utils;

@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	TaskRepository taskRepository;
	
	@Autowired
    UserRepository userRepository;

	@Autowired
	Utils utils;

	@Override
	public TaskDto createTask(TaskDto taskDto, String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);

		ModelMapper modelMapper = new ModelMapper();
		TaskEntity taskEntity = modelMapper.map(taskDto, TaskEntity.class);
		

		taskEntity.setTaskId(utils.generateRandomId(20));
		taskEntity.setStatus("pending");
		taskEntity.setUserDetails(userEntity);
		
		TaskEntity createdTask = taskRepository.save(taskEntity);
		
		TaskDto returnValue = modelMapper.map(createdTask, TaskDto.class);

		return returnValue;
	}

	@Override
	public List<TaskDto> getAllTasks(int page, int limit) {
		List<TaskDto> returnValue = new ArrayList<>();

		if (page > 0)
			page--;

		Pageable pageableRequest = PageRequest.of(page, limit);

		Page<TaskEntity> tasksPage = taskRepository.findAll(pageableRequest);
		List<TaskEntity> tasks = tasksPage.getContent();

		ModelMapper modelMapper = new ModelMapper();
		
		for (TaskEntity taskEntity : tasks) {
			
			TaskDto taskDto = new TaskDto();
			TaskDto task = modelMapper.map(taskEntity, TaskDto.class);

			returnValue.add(task);
		}

		return returnValue;
	}

	@Override
	public TaskDto getTasksById(String taskId) {
		TaskDto returnValue = new TaskDto();

		TaskEntity taskEntity = taskRepository.findByTaskId(taskId);

		BeanUtils.copyProperties(taskEntity, returnValue);

		return returnValue;
	}

	@Override
	public List<TaskDto> getAllTasksByStatus(int page, int limit, String status) {
		List<TaskDto> returnValue = new ArrayList<>();

		if (page > 0)
			page--;

		Pageable pageableRequest = PageRequest.of(page, limit);

		Page<TaskEntity> tasksPage = taskRepository.getAllTaskByStatus(pageableRequest, status);
		List<TaskEntity> tasks = tasksPage.getContent();

		for (TaskEntity taskEntity : tasks) {
			TaskDto taskDto = new TaskDto();
			BeanUtils.copyProperties(taskEntity, taskDto);
			returnValue.add(taskDto);
		}

		return returnValue;
	}

	@Override
	public TaskDto updateTask(String taskId, TaskDto task) {
		TaskDto taskDto = new TaskDto();
		
		TaskEntity storedTask = taskRepository.findAllByTaskId(taskId);
		
		if(task.getTitle() != null) storedTask.setTitle(task.getTitle());
		if(task.getDescription() != null) storedTask.setDescription(task.getDescription());
		if(task.getStatus() != null) storedTask.setStatus(task.getStatus());
		
		TaskEntity updatedTask = taskRepository.save(storedTask);
		
		BeanUtils.copyProperties(updatedTask, taskDto);

		return taskDto;
	}

	@Override
	public void deleteTask(String taskId) {
		TaskEntity taskEntity = taskRepository.findByTaskId(taskId);
		
		taskRepository.delete(taskEntity);
	}
	
	@Override
	public List<TaskDto> getAllUserTask(int page, int limit, String userId) {
		List<TaskDto> returnValue = new ArrayList<>();
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
//		Long id = userEntity.getId();

		if (page > 0)
			page--;

		Pageable pageableRequest = PageRequest.of(page, limit);

		Page<TaskEntity> tasksPage = taskRepository.getAllUserTask(pageableRequest, userEntity);
		List<TaskEntity> tasks = tasksPage.getContent();

		for (TaskEntity taskEntity : tasks) {
			TaskDto taskDto = new TaskDto();
			BeanUtils.copyProperties(taskEntity, taskDto);
			returnValue.add(taskDto);
		}

		return returnValue;
	}
}
