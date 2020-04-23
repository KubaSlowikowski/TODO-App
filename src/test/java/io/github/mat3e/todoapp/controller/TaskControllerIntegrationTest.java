package io.github.mat3e.todoapp.controller;

import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class TaskControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc; //pozwala wykonać zapytanie i asercje na nich (np sprawdzać statusy odpowiedzi)

    @Autowired
    private TaskRepository repo; //profil - integration, zatem uzywamy falszywego repozytorium

    @Test
    void httpGet_returnsGivenTask() throws Exception {
        //given
        int id = repo.save(new Task("foo", LocalDateTime.now())).getId();

        //when+then
        mockMvc.perform(get("/tasks/" + id))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void httpGet_returnsAllTheTasks() throws Exception {
        //given
        final int initialSize = repo.findAll().size();
        final var today = LocalDate.now().atStartOfDay();
        repo.save(new Task("foo", today));
        repo.save(new Task("bar", today));
        //when + then
        var result = mockMvc.perform(get("/tasks"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json("[{\"id\":1,\"description\":\"foo\",\"done\":false,\"deadline\":\"" + today + ":00\"},{\"id\":2,\"description\":\"bar\",\"done\":false,\"deadline\":\"" + today + ":00\"}]"));
    }
}