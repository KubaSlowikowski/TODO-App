package io.github.mat3e.todoapp;

import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

@Configuration
class TestConfiguration {

    @Bean
    @Primary
    @Profile("!integration")
    DataSource e2eTestDataSource() { // baza H2 na potrzeby testów
        var result = new DriverManagerDataSource("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
        result.setDriverClassName("org.h2.Driver");
        return result;
    }

    @Bean
    @Primary //ten bean przejmuje kontrolę, gdy odpalamy wszystko w profilu "integration"
    @Profile("integration") //to repo bedzie dzialalo, jeśli odpalimy Springa na profilu 'integration'
    TaskRepository testRepo() { //repozytorium symulujące bazę danych (jest niezalezne od niej) do używania w testach
        return new TaskRepository() {
            private Map<Integer, Task> tasks = new HashMap<>();

            @Override
            public List<Task> findAll() {
                return new ArrayList<>(tasks.values());
            }

            @Override
            public Page<Task> findAll(final Pageable page) {
                return null;
            }

            @Override
            public Optional<Task> findById(final Integer id) {
                return Optional.ofNullable(tasks.get(id));
            }

            @Override
            public List<Task> findAllByGroup_Id(final Integer groupId) {
                return List.of();
            }

            @Override
            public List<Task> findAllByDeadlineBeforeOrDeadlineNullAndDoneFalse(final LocalDateTime date) {
                return null;
            }

            @Override
            public boolean existsById(final Integer var1) {
                return tasks.containsKey(var1);
            }

            @Override
            public boolean existsByDoneIsFalseAndGroup_Id(final Integer groupId) {
                return false;
            }

            @Override
            public List<Task> findByDone(final boolean done) {
                return null;
            }

            @Override
            public Task save(final Task entity) {
                int key = tasks.size() + 1;
                Field field = null;
                try {
                    field = Task.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity,key);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                tasks.put(key, entity);
                return tasks.get(key);
            }
        };
    }
}