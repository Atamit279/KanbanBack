package com.example.examenback.service.impl;


import com.example.examenback.DTO.TaskDTO;
import com.example.examenback.entities.Task;
import com.example.examenback.repository.TaskRepository;
import com.example.examenback.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final ModelMapper modelMapper;
    private final TaskRepository repository;

    @Override
    public TaskDTO save(TaskDTO taskDTO) {

        Task savedTask = repository.save(modelMapper.map(taskDTO, Task.class));

        return modelMapper.map(savedTask, TaskDTO.class);
    }

    @Override
    public TaskDTO update(TaskDTO taskDTO) {
        Task newTask = modelMapper.map(taskDTO, Task.class);
        Optional<Task> optionalTask = repository.findById(newTask.getId());
        if(optionalTask.isPresent()){
            Task oldTask = optionalTask.get();

            newTask.setId(oldTask.getId());
            oldTask.setStatus(newTask.getStatus());
            oldTask.setDescription(newTask.getDescription());
            oldTask.setName(newTask.getName());
            oldTask.setPriority(newTask.getPriority());
            repository.save(newTask);

            return modelMapper.map(newTask, TaskDTO.class);
        }
        return null;
    }

    @Override
    public boolean delete(Integer taskId) {
        return false;
    }


    @Override
    public List<TaskDTO> getAll() {
        return repository.findAll().stream()
                .map(task -> modelMapper.map(task, TaskDTO.class))
                .collect(Collectors.toList());
    }





    @Override
    public TaskDTO getById(Integer id) throws Exception {
        Optional<Task> optionalTask = repository.findById(id);
        if (optionalTask.isEmpty()){
            throw new Exception(String.format("Task with id %s not found", id));
        }
        return modelMapper.map(optionalTask.get(),TaskDTO.class);
    }

    @Override
    public HttpStatus deleteById(Integer id){
        Optional<Task> task = repository.findById(id);
        if (task.isPresent()){
            repository.deleteById(id);
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

}
