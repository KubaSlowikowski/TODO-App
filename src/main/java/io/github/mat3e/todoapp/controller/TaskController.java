package io.github.mat3e.todoapp.controller;

import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller
class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;

    TaskController(final TaskRepository repository) {
        this.repository = repository;
    }


    //@GetMapping(value = "/tasks", params = {"!sort", "!page", "!size"}) //wchodzę w tą metodę, gdy nie użyłem metod sort,page,size
    @RequestMapping(method = RequestMethod.GET, path = "/tasks", params = {"!sort", "!page", "!size"}) // jak tylko przychodzi request, ma on trafić do tej metody
    ResponseEntity<List<Task>> readAllTasks() { //wypisuje tylko listę tasków
        logger.warn("Exposing all the tasks!");
        return ResponseEntity.ok(repository.findAll());
    }

    //@GetMapping(value = "/tasks") //wchodzę tu jak użyłem metod sort/page/pageable
    @RequestMapping(method = RequestMethod.GET, path = "/tasks")
    ResponseEntity<List<Task>> readAllTasks(Pageable page) {
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

//    @GetMapping(value = "/tasks") //wchodzę tu jak użyłem metod sort/page/pageable
//    ResponseEntity<?> readAllTasks(Pageable page) {
//        logger.info("Custom pageable");
//        return ResponseEntity.ok(repository.findAll(page));
//    }

//    @GetMapping("/tasks/{id}")
    @RequestMapping(method = RequestMethod.GET, path = "/tasks/{id}")
    ResponseEntity<Task> readTask(@PathVariable int id) {
        return repository.findById(id)
            .map(task -> ResponseEntity.ok(task))
            .orElse(ResponseEntity.notFound().build());
    }

    //    @GetMapping("/tasks/{id}")
//    ResponseEntity<Task> readTask(@PathVariable int id) {
//        Optional<Task> optionalTask = repository.findById(id);
//        if (!optionalTask.isPresent())
//            return new ResponseEntity<>(HttpStatus.CONFLICT);
//        else {
//            return ResponseEntity.ok(optionalTask.get());
//        }
//    }

    //@PutMapping("/tasks/{id}")
    @RequestMapping( method = RequestMethod.PUT, path = "/tasks/{id}")
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        toUpdate.setId(id);
        repository.save(toUpdate);
        return ResponseEntity.noContent().build();
    }

//    @PostMapping("/tasks")
//    ResponseEntity<?> createTask(@RequestBody @Valid Task task) {
//        Optional<Task> optional = repository.findById(task.getId());
//        if(optional.isPresent()) {
//            logger.warn("Task already exist");
//            return ResponseEntity.badRequest().build();
//        }
//        else {
//            repository.save(task);
//            logger.info("Created new Task");
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Location", "http://localhost:8080/tasks/" + task.getId());
//            return new ResponseEntity<>(task, headers, HttpStatus.CREATED);
//        }
//    }

    //@PostMapping("/tasks")
    @RequestMapping(method = RequestMethod.POST, path = "/tasks")
    ResponseEntity<Task> createTask (@RequestBody @Valid Task toCreate) {
        Task result = repository.save(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }
}