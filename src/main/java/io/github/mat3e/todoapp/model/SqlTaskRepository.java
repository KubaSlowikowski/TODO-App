package io.github.mat3e.todoapp.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> { //klasa służy do komunikacji z bazą danych
    // nie jest publiczny, tylko Spring ma do niego dostęp

    @Override
    @Query(nativeQuery = true, value = "select count(*) > 0 from tasks where id=:id") //nadpisujemy Springową logikę lepszym, bardziej wydajnym zapytaniem
    boolean existsById(@Param("id") Integer var1);
}