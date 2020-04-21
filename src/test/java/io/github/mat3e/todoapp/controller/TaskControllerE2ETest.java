package io.github.mat3e.todoapp.controller;

import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

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
        int initialSize = repo.findAll().size();
        repo.save(new Task("foo", LocalDateTime.now()));
        repo.save(new Task("bar", LocalDateTime.now()));

        //when
        Task[] result = restTemplate.getForObject("http://localhost:" + port + "/tasks", Task[].class);

        //then
        assertThat(result).hasSize(initialSize + 2);
    }
}