package com.nathan.todo.service;

import java.util.List;

import com.nathan.todo.dto.TaskDto;
import com.nathan.todo.dto.UserDto;

public interface TaskService {

	TaskDto createTask(TaskDto taskDto, String userId);

	List<TaskDto> getAllTasks(int page, int limit);

	TaskDto getTasksById(String taskId);

	List<TaskDto> getAllTasksByStatus(int page, int limit, String status);

	TaskDto updateTask(String taskId, TaskDto task);

	void deleteTask(String taskId);

	List<TaskDto> getAllUserTask(int page, int limit, String userId);
	
}
