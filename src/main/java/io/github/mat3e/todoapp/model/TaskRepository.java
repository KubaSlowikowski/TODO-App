package io.github.mat3e.todoapp.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource//(path = "todos", collectionResourceRel = "todos")
public interface TaskRepository extends JpaRepository<Task, Integer> { //klasa służy do komunikacji z bazą danych

    @Override
    @RestResource(exported = false)
    void deleteById(Integer integer);

    @Override
    @RestResource(exported = false)
    void delete(Task task);

    @RestResource(path = "done", rel = "done")
    List<Task> findByDone(@Param("state") boolean done); //z racji tego, że interfejs ma adnotację @RepositoryRestResource, metoda ta będzie dostępna pod jakimś adresem


}