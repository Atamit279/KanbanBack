package com.example.examenback.service;

import com.example.examenback.DTO.TaskDTO;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface TaskService {
    TaskDTO save(TaskDTO taskDTO) throws Exception;

    TaskDTO update(TaskDTO taskDTO);

    boolean delete(Integer taskId);

    HttpStatus deleteById(Integer id);

    List<TaskDTO> getAll();

    TaskDTO getById(Integer id) throws Exception;


}
