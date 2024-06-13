package exercise.controller;

import java.util.List;
import java.util.Optional;

import exercise.dto.TaskCreateDTO;
import exercise.dto.TaskDTO;
import exercise.dto.TaskUpdateDTO;
import exercise.mapper.TaskMapper;
import exercise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.config.Task;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import exercise.exception.ResourceNotFoundException;
import exercise.repository.TaskRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TasksController {
    // BEGIN
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskMapper taskMapper;

    @GetMapping("")
    public List<TaskDTO> getAll() {
        var posts = taskRepository.findAll();
        return posts.stream().map(taskMapper::map).toList();
    }

    @GetMapping("/{id}")
    public TaskDTO get(@PathVariable long id) {
        return taskMapper.map(taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not Found!")));
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(@RequestBody @Valid TaskCreateDTO dto) {
        taskRepository.save(taskMapper.map(dto));
        return taskMapper.map(taskMapper.map(dto));
    }

    @PutMapping("/{id}")
    public TaskDTO update(@RequestBody @Valid TaskUpdateDTO dto, @PathVariable long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found!"));

        taskMapper.update(dto, task);
        var user = userRepository.findById(dto.getAssigneeId()).orElseThrow(() -> new ResourceNotFoundException("Not Found!"));
        task.setAssignee(user);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        taskRepository.deleteById(id);
    }

    // END
}
