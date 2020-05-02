package io.github.mat3e.todoapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Project's description must not be null or empty")
    private String description;
    @Embedded
    private Audit audit = new Audit();
    @OneToMany(mappedBy = "project")
    private Set<TaskGroup> taskGroups;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private Set<ProjectStep> projectSteps;

    public Project() {
    }

    public Project(final String description, final Set<TaskGroup> taskGroups, final Set<ProjectStep> projectSteps) {
        this.description = description;
        this.taskGroups = taskGroups;
        this.projectSteps = projectSteps;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Set<TaskGroup> getTaskGroups() {
        return taskGroups;
    }

    public void setTaskGroups(final Set<TaskGroup> taskGroups) {
        this.taskGroups = taskGroups;
    }

    public Set<ProjectStep> getProjectSteps() {
        return projectSteps;
    }

    public void setProjectSteps(final Set<ProjectStep> projectSteps) {
        this.projectSteps = projectSteps;
    }
}