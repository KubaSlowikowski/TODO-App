package io.github.mat3e.todoapp.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> { //klasa służy do komunikacji z bazą danych

 //   @RestResource(path = "done", rel = "done")
    List<Task> findByDone(@Param("state") boolean done); //z racji tego, że interfejs ma adnotację @RepositoryRestResource, metoda ta będzie dostępna pod jakimś adresem


}