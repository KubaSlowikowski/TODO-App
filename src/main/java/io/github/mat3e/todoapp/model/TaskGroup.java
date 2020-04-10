package io.github.mat3e.todoapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "task_groups")
public class TaskGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Tasks group's description must not be empty")
    private String description;
    private boolean done;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group") //CascadeType.ALL - gdy usune grupe, usuwam wszystkie taski, mappedBy - wewnątrz każdego Taska ta grupa jest zmapowana jako 'group'
    private Set<Task> tasks; //w hibernate List nie zachowuje kolejnosci, wiec lepiej uzyc Set

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

    void setDescription(final String description) {
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

    void setTasks(final Set<Task> tasks) {
        this.tasks = tasks;
    }
}