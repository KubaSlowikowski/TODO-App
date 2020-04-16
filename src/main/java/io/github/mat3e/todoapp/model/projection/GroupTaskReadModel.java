package io.github.mat3e.todoapp.model.projection;

import io.github.mat3e.todoapp.model.Task;

public class GroupTaskReadModel { //task czytany w obrębie grupy
    private boolean done;
    private String description;

    public GroupTaskReadModel(Task source) {
        this.description = source.getDescription();
        this.done = source.isDone();
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(final boolean done) {
        this.done = done;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
