package io.github.mat3e.todoapp.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TaskRepository { //to jest repozytorium zawierające metody,  ktore chcemy upublicznić
    List<Task> findAll();

    Page<Task> findAll(Pageable page);

    Optional<Task> findById(Integer id);

    boolean existsById(Integer var1);

    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);

    List<Task> findByDone(boolean done);

    Task save(Task entity);
}