package io.github.mat3e.todoapp.model.projection;

import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskGroup;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupReadModel {
    private int id;
    @NotBlank(message = "Tasks group's description must not be empty")
    private String description;
    /**
     * Deadline from the latest task in group
     */
    private LocalDateTime deadline;
    @Valid
    private Set<GroupTaskReadModel> tasks; //parametryzuję GroupTaskReadModel'em, bo potrzebuje tylko taskow do odczytu

    public GroupReadModel(TaskGroup source) {
        this.id = source.getId();
        this.description = source.getDescription();
        source.getTasks()
                .stream()
                .map(Task::getDeadline)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .ifPresent(date -> this.deadline = date);
        this.tasks = source.getTasks()
                .stream()
                .map(GroupTaskReadModel::new)
                .collect(Collectors.toSet());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(final LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public List<GroupTaskReadModel> getTasks() {
        return new ArrayList<>(tasks);
    }

    public void setTasks(final Set<GroupTaskReadModel> tasks) {
        this.tasks = tasks;
    }
}