package io.github.mat3e.todoapp.model;

import java.util.List;
import java.util.Optional;

public interface TaskGroupRepository {
    List<TaskGroup> findAll();

    Optional<TaskGroup> findById(Integer id);

    boolean existsByDoneIsFalseAndProject_Id(Integer projectId);

    TaskGroup save(TaskGroup entity);
}
