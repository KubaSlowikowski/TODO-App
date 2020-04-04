package io.github.mat3e.todoapp.controller;

import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RepositoryRestController
class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;

    TaskController(final TaskRepository repository) {
        this.repository = repository;
    }

    //@RequestMapping(method = RequestMethod.GET, path = "/tasks") // jak tylko przychodzi request, ma on trafić do tej metody
    @GetMapping(value = "/tasks", params = {"!sort", "!page", "!size"}) //wchodzę w tą metodę, gdy nie użyłem metod sort,page,size
    ResponseEntity<List<Task>> readAllTasks() { //wypisuje tylko listę tasków
        logger.warn("Exposing all the tasks!");
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping(value = "/tasks") //wchodzę tu jak użyłem metod sort/page/pageable
    ResponseEntity<List<Task>> readAllTasks(Pageable page) {
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

//    @GetMapping(value = "/tasks") //wchodzę tu jak użyłem metod sort/page/pageable
//    ResponseEntity<?> readAllTasks(Pageable page) {
//        logger.info("Custom pageable");
//        return ResponseEntity.ok(repository.findAll(page));
//    }
}