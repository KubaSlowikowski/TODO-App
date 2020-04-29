package io.github.mat3e.todoapp.model.projection;

import io.github.mat3e.todoapp.model.Project;
import io.github.mat3e.todoapp.model.TaskGroup;

import java.util.Set;
import java.util.stream.Collectors;

public class GroupWriteModel {
    private String description;
    private Set<GroupTaskWriteModel> tasks;

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Set<GroupTaskWriteModel> getTasks() {
        return tasks;
    }

    public void setTasks(final Set<GroupTaskWriteModel> tasks) {
        this.tasks = tasks;
    }

    public TaskGroup toGroup(final Project project) {
        var result = new TaskGroup();
        result.setDescription(description);
        result.setProject(project);
        result.setTasks(
                tasks.stream()
                        .map(taskWriteModel -> taskWriteModel.toTask(result))
                        .collect(Collectors.toSet())
        );
        return result;
    }
}