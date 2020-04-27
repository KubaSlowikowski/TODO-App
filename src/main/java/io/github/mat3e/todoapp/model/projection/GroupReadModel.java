package io.github.mat3e.todoapp.model.projection;

import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskGroup;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupReadModel {
    private int id;
    private String description;
    /**
     * Deadline from the latest task in group
     */
    private LocalDateTime deadline;
    private Set<GroupTaskReadModel> tasks; //parametryzuję GroupTaskReadModel'em, bo potrzebuje tylko taskow do odczytu

    public GroupReadModel(TaskGroup source) {
        this.description = source.getDescription();
        source.getTasks()
                .stream()
                .map(Task::getDeadline)
                .max(LocalDateTime::compareTo)
                .ifPresent(date -> this.deadline = date);
        this.tasks = source.getTasks()
                .stream()
                .map(GroupTaskReadModel::new)
                .collect(Collectors.toSet());
        this.id = source.getId();
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

    public Set<GroupTaskReadModel> getTasks() {
        return tasks;
    }

    public void setTasks(final Set<GroupTaskReadModel> tasks) {
        this.tasks = tasks;
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }
}