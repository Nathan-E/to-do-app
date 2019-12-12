package com.nathan.todo.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nathan.todo.response.RequestOperationStatus;
import com.nathan.todo.dto.TaskDto;
import com.nathan.todo.request.TaskRequestModel;
import com.nathan.todo.request.TaskUpdateModel;
import com.nathan.todo.response.OperationStatusModel;
import com.nathan.todo.response.TaskRest;
import com.nathan.todo.service.TaskService;

@RestController
@RequestMapping("/tasks")
public class TaskController {
	
	@Autowired
	TaskService taskService;
	
	@GetMapping("/health-check")
	public String testApp() {
		return "We are Good";
	}
	
	@PostMapping(path="/{userId}",
			produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
			consumes= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
			)
	public TaskRest createTask(@RequestBody TaskRequestModel taskRequestModel, 
			@PathVariable (value = "userId") String userId) {
		TaskDto taskDto = new TaskDto();
		
		BeanUtils.copyProperties(taskRequestModel, taskDto);
		
		TaskDto createdTask = taskService.createTask(taskDto, userId);
		
		TaskRest returnValue = new TaskRest();
		
		ModelMapper modelMapper = new ModelMapper();
		returnValue = modelMapper.map(createdTask, TaskRest.class);
		
		return returnValue;
	}
	
	@GetMapping(path="/all",
			produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
			)
	public List<TaskRest> getAllTask(@RequestParam(value="page", defaultValue="0") int page, 
									@RequestParam(value="limit", defaultValue="2") int limit) {
		List<TaskDto> tasks = taskService.getAllTasks(page, limit);
		List<TaskRest> returnValue = new ArrayList<>();
		
		ModelMapper modelMapper = new ModelMapper();
		
		for(TaskDto task: tasks) {
			TaskRest taskRest = new TaskRest();
		
			taskRest = modelMapper.map(task, TaskRest.class);
			
			returnValue.add(taskRest);
		}
		
		return returnValue;
	}
	
	@GetMapping( path="/{taskId}",
			produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
			)
	public TaskRest getTaskById(@PathVariable String taskId) {
		TaskRest returnValue = new TaskRest();
		
		TaskDto task = taskService.getTasksById(taskId);
		
		ModelMapper modelMapper = new ModelMapper();

		returnValue = modelMapper.map(task, TaskRest.class);

		return returnValue;
	}
	
	@GetMapping(
			produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
			)
	public List<TaskRest> getAllTaskByStatus(@RequestParam(value="page", defaultValue="0") int page, 
									@RequestParam(value="limit", defaultValue="10") int limit,
									@RequestParam String status) {
		List<TaskDto> tasks = taskService.getAllTasksByStatus(page, limit, status);
		List<TaskRest> returnValue = new ArrayList<>();
		
		ModelMapper modelMapper = new ModelMapper();

		for(TaskDto task: tasks) {
			TaskRest taskRest = new TaskRest();
			
			taskRest = modelMapper.map(task, TaskRest.class);
			
			returnValue.add(taskRest);
		}
		
		return returnValue;
	}
	
	@GetMapping(path="/{userId}/all",
			produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
			)
	public List<TaskRest> getAllUserTask(@RequestParam(value="page", defaultValue="0") int page, 
									@RequestParam(value="limit", defaultValue="2") int limit, @PathVariable String userId) {
		List<TaskDto> tasks = taskService.getAllUserTask(page, limit, userId);
		List<TaskRest> returnValue = new ArrayList<>();
		
		ModelMapper modelMapper = new ModelMapper();
		
		for(TaskDto task: tasks) {
			TaskRest taskRest = new TaskRest();
		
			taskRest = modelMapper.map(task, TaskRest.class);
			
			returnValue.add(taskRest);
		}
		
		return returnValue;
	}
	
	@PutMapping( path="/{taskId}",
			produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
			consumes= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
			)
	public TaskRest updateTask(@RequestBody TaskUpdateModel taskUpdateModel, @PathVariable String taskId) {
		TaskRest returnValue = new TaskRest();
		
		TaskDto task = new TaskDto();
		
		BeanUtils.copyProperties(taskUpdateModel, task);
		
		TaskDto updatedTask = taskService.updateTask(taskId, task);
		
		ModelMapper modelMapper = new ModelMapper();

		returnValue = modelMapper.map(updatedTask, TaskRest.class);
		
		return returnValue;
	}
	
	@DeleteMapping( path="/{taskId}",
			produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
			)
	public OperationStatusModel deleteTask(@PathVariable String taskId) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		
		taskService.deleteTask(taskId);
		
	 	returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
	 	
		return returnValue;
	}
}
