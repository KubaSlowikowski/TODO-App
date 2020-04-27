package io.github.mat3e.todoapp.controller;

import io.github.mat3e.todoapp.logic.TaskGroupService;
import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskGroupRepository;
import io.github.mat3e.todoapp.model.TaskRepository;
import io.github.mat3e.todoapp.model.projection.GroupReadModel;
import io.github.mat3e.todoapp.model.projection.GroupWriteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/groups")
class TaskGroupController {
    private static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);
    private final TaskGroupRepository repository;
    private final TaskRepository taskRepository;
    private final TaskGroupService service;

    TaskGroupController(final TaskGroupRepository repository, final TaskRepository taskRepository, final TaskGroupService service) {
        this.repository = repository;
        this.taskRepository = taskRepository;
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<GroupReadModel> createGroup(@RequestBody @Valid GroupWriteModel toCreate) {
        var result = service.createGroup(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<List<GroupReadModel>> readAllGroups() {
        logger.warn("Exposing all the groups!");
        return ResponseEntity.ok(service.readAll());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    ResponseEntity<GroupReadModel> readGroup(@PathVariable int id) {
        return repository.findById(id)
                .map(taskGroup -> ResponseEntity.ok(new GroupReadModel(taskGroup)))
                .orElse(ResponseEntity.notFound().build());
    }


    @Transactional
    @RequestMapping(method = RequestMethod.PATCH, path = "/toogle/{id}")
    ResponseEntity<?> toogleGroup(@PathVariable int id) {
        service.toogleGroup(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/tasks")
    ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id) {
        return ResponseEntity.ok(taskRepository.findAllByGroup_Id(id));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handelIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleIllegalState(IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}