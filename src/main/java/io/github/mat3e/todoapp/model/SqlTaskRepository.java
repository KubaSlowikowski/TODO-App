package io.github.mat3e.todoapp.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> { //klasa służy do komunikacji z bazą danych
    // nie jest publiczny, tylko Spring ma do niego dostęp
}