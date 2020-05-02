package io.github.mat3e.todoapp.model;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "task_groups")
public class TaskGroup {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Tasks group's description must not be empty")
    private String description;
    private boolean done;
    @Valid
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group") //CascadeType.ALL - gdy usune grupe, usuwam wszystkie taski, mappedBy - wewnątrz każdego Taska ta grupa jest zmapowana jako 'group'
    private Set<Task> tasks; //w hibernate List nie zachowuje kolejnosci, wiec lepiej uzyc Set
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public TaskGroup() {
    }

    public int getId() {
        return id;
    }

    void setId(final int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(final Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(final Project project) {
        this.project = project;
    }
}