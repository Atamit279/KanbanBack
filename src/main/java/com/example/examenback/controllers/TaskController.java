package com.example.examenback.controllers;

import com.example.examenback.DTO.TaskDTO;
import com.example.examenback.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/")
    public TaskDTO save(@RequestBody TaskDTO taskDTO) throws  Exception{
        return taskService.save(taskDTO);
    }

    @GetMapping("/{id}")
    public TaskDTO getById(@PathVariable Integer id) throws Exception{
        return taskService.getById(id);
    }

    @GetMapping("/all")
    public List<TaskDTO> getAll() throws Exception{
        return taskService.getAll();
    }

    @PutMapping("/update")
    public TaskDTO update(@RequestBody TaskDTO taskDTO) {
        return taskService.update(taskDTO);
    }

    @DeleteMapping("/deleteById/{id}")
    public HttpStatus deleteById(@PathVariable Integer id){
        return taskService.deleteById(id);
    }

}
