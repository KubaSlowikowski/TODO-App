package io.github.mat3e.todoapp.reports;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface PersistedTaskEventRepository extends JpaRepository<PersistedTaskEvent, Integer> {
    List<PersistedTaskEvent> findByTaskId(int taskId);
    List<PersistedTaskEvent> findByNameEqualsAndTaskIdEquals(String name, int taskId);
}
