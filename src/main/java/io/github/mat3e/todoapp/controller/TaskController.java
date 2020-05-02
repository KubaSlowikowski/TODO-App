package io.github.mat3e.todoapp.controller;

import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/tasks")
class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;
    //private final TaskService service;

    TaskController(final TaskRepository repository) {
        this.repository = repository;
      //  this.service = service;
    }

    //@GetMapping(value = "/tasks", params = {"!sort", "!page", "!size"}) //wchodzę w tą metodę, gdy nie użyłem metod sort,page,size
    @RequestMapping(method = RequestMethod.GET, params = {"!sort", "!page", "!size"}) // jak tylko przychodzi request, ma on trafić do tej metody
    ResponseEntity<List<Task>> readAllTasks() { //wypisuje tylko listę tasków
        logger.warn("Exposing all the tasks!");
        return ResponseEntity.ok(repository.findAll());
    }

    //@GetMapping(value = "/tasks") //wchodzę tu jak użyłem metod sort/page/pageable
    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<List<Task>> readAllTasks(Pageable page) {
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    //    @GetMapping("/tasks/{id}")
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    ResponseEntity<Task> readTask(@PathVariable int id) {
        return repository.findById(id)
                .map(task -> ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());
    }

//    @GetMapping(value = "/search/done", produces = MediaType.APPLICATION_JSON_VALUE) //gdy żądanie HTTP zawiera JSON
//    String foo() {
//        return "";
//    }
//    @GetMapping(value = "/search/done", produces = MediaType.TEXT_XML_VALUE) //gdy żądanie HTTP zawiera XML
//    String bar() {
//        return "";
//    }

    @GetMapping("/search/done")
    ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true") boolean state) {
        return ResponseEntity.ok(
                repository.findByDone(state)
        );
    }

    //@PutMapping("/tasks/{id}")
    @RequestMapping( method = RequestMethod.PUT, path = "/{id}")
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> {
                    task.updateFrom(toUpdate);
                    repository.save(task); //mozna tak zrobic zamiast używać @Transactional
                });
        return ResponseEntity.noContent().build();
    }

    //@PostMapping("/tasks")
    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<Task> createTask (@RequestBody @Valid Task toCreate) {
        Task result = repository.save(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @Transactional //zmiana zostanie zarejestrowana w bazie danych
    @RequestMapping(method = RequestMethod.PATCH, path = "/{id}")
    public ResponseEntity<?> toogleTask(@PathVariable int id) { //warunek konieczny - metoda publiczna
        if(!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/today")
    ResponseEntity<List<Task>> searchToday() {
        LocalDateTime today = LocalDate.now().atTime(LocalTime.MAX);
        var a = repository.findAllByDeadlineBeforeOrDeadlineNullAndDoneFalse(today);
        return ResponseEntity.ok(a);
    }
}