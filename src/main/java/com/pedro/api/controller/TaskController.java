package com.pedro.api.controller;

import com.pedro.api.dto.TaskDTO;
import com.pedro.api.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping
    public TaskDTO insert(@RequestBody TaskDTO dto) {
        return service.insert(dto);
    }

    @GetMapping
    public Page<TaskDTO> list(Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    public TaskDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public TaskDTO update(@PathVariable Long id, @RequestBody TaskDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}