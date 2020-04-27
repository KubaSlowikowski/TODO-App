package io.github.mat3e.todoapp.model.projection;

import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskGroup;

import java.time.LocalDateTime;

public class GroupTaskWriteModel { //pozwala na przesylanie taska w bezpieczny sposób
    private String description;
    private LocalDateTime deadline;

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

    Task toTask(final TaskGroup taskGroup) {
        return new Task(description, deadline, taskGroup);
    }
}