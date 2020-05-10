package io.github.mat3e.todoapp.reports;


import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reports")
class ReportController {
    private final TaskRepository taskRepository;
    private final PersistedTaskEventRepository eventRepository;

    ReportController(final TaskRepository taskRepository, final PersistedTaskEventRepository eventRepository) {
        this.taskRepository = taskRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping("/count/{id}")
    ResponseEntity<TaskWithChangesCount> readTaskWithCount(@PathVariable int id) {
        return taskRepository.findById(id)
                .map(task -> new TaskWithChangesCount(task, eventRepository.findByTaskId(id)))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

//    @GetMapping("/doneBeforeDeadline")
//    ResponseEntity<List<TaskDoneBeforeDeadline>> readTasksDoneBeforeDeadline() {
//        var list = taskRepository.findByDone(true)
//                .stream()
//                .map(task -> new TaskDoneBeforeDeadline(task, eventRepository.findByNameEqualsAndTaskIdEquals("TaskDone", task.getId())))
//                .filter(taskDoneBeforeDeadline -> taskDoneBeforeDeadline.doneBeforeDeadline)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(list);
//    }

    private static class TaskWithChangesCount {
        public String description;
        public boolean done;
        public int changesCount;

        TaskWithChangesCount(final Task task, final List<PersistedTaskEvent> events) {
            description = task.getDescription();
            done = task.isDone();
            changesCount = events.size();
        }
    }

//    private static class TaskDoneBeforeDeadline {
//        public String description;
//        public boolean doneBeforeDeadline;
//
//        TaskDoneBeforeDeadline(final Task task, final List<PersistedTaskEvent> events) {
//            description = task.getDescription();
//            if (task.getDeadline() == null) {
//                doneBeforeDeadline = true;
//            } else {
//                events.stream()
//                        .
//                        .filter(event -> event.occurrence.isBefore(task.getDeadline()))
//            }
//        }
//    }
}