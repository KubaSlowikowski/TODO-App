package io.github.mat3e.todoapp.controller;

import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerE2ETest { //test uzywa bazę danych H2 stworzoną na potrzeby testów
    @LocalServerPort //numer wylosowanego portu
    private int port;

    @Autowired
    private TestRestTemplate restTemplate; //klasa do odpytywanie usług (np. strzela GET-em)

    @Autowired
    TaskRepository repo;

    @Test
    void httpGet_returnsAllTheTasks() {
        //given
        final int initialSize = repo.findAll().size();
        repo.save(new Task("foo", LocalDateTime.now()));
        repo.save(new Task("bar", LocalDateTime.now()));

        //when
        Task[] result = restTemplate.getForObject("http://localhost:" + port + "/tasks", Task[].class);

        //then
        assertThat(result).hasSize(initialSize + 2);
    }

    @Test
    void httpGet_returnsSpecificTask() {
        //given
        final String description = "specificTask";
        final int initialSize = repo.findAll().size();
        final int id = repo.save(new Task(description, LocalDateTime.now())).getId();

        //when
        Task result = restTemplate.getForObject("http://localhost:" + port + "/tasks/" + id, Task.class);

        //then
        assertThat(result).hasFieldOrPropertyWithValue("description", description);
        assertThat(repo.findAll().size()).isEqualTo(initialSize + 1);
    }

    @Test
    void httpPut_returnPutTask() {
        //given
        final String description = "changedDescription";
        final var today = LocalDate.now().atStartOfDay();
        final int initialSize = repo.findAll().size();
        final int id = repo.save(new Task("foo", today.minusDays(1))).getId(); //task to modify
        //when
        restTemplate.put("http://localhost:" + port + "/tasks/" + id, new Task(description, today)); //modified task

        Task result = restTemplate.getForObject("http://localhost:" + port + "/tasks/" + id, Task.class);
        //then
        assertThat(result)
                .hasFieldOrPropertyWithValue("description", description)
                .hasFieldOrPropertyWithValue("done", false)
                .hasFieldOrPropertyWithValue("deadline", today);
        assertThat(repo.findAll().size()).isEqualTo(initialSize + 1);
    }

    @Test
    void httpPost_returnPostedTask() {
        //given
        final String description = "description";
        final var today = LocalDate.now().atStartOfDay();
        final int initialSize = repo.findAll().size();
        //when
        ResponseEntity<Task> response = restTemplate.postForEntity("http://localhost:" + port + "/tasks", new Task(description, today), Task.class);
        //then
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody())
                .hasFieldOrPropertyWithValue("description", description)
                .hasFieldOrPropertyWithValue("done", false)
                .hasFieldOrPropertyWithValue("deadline", today);
        assertThat(repo.findAll().size()).isEqualTo(initialSize + 1);
    }
}