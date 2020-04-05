package io.github.mat3e.todoapp.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository { //to jest repozytorium zawierające metody,  ktore chcemy upublicznić
    List<Task> findAll();

    Page<Task> findAll(Pageable page);

    Optional<Task> findById(Integer id);

    boolean existsById(Integer var1);

    Task save(Task entity);

    List<Task> findByDone(@Param("state") boolean done); //z racji tego, że interfejs ma adnotację @RepositoryRestResource, metoda ta będzie dostępna pod jakimś adresem
}