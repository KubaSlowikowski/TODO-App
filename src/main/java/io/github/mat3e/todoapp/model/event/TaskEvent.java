package io.github.mat3e.todoapp.model.event;

import io.github.mat3e.todoapp.model.Task;

import java.time.Clock;
import java.time.Instant;

public abstract class TaskEvent {
    public static TaskEvent changed(Task source) {
        return source.isDone() ? new TaskDone(source) : new TaskUndone(source);
    }

    private int taskId;
    private Instant occurrence; // klasa Instant będzie najlepsza do odwzorowania punktu w czasie

    TaskEvent(final int taskId, Clock clock) {
        this.taskId = taskId;
        occurrence = Instant.now(clock);
    }

    public int getTaskId() {
        return taskId;
    }

    public Instant getOccurrence() {
        return occurrence;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "TaskEvent{" +
                "taskId=" + taskId +
                ", occurrence=" + occurrence +
                '}';
    }
}