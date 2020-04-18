package io.github.mat3e.todoapp.logic;

import io.github.mat3e.todoapp.TaskConfigurationProperties;
import io.github.mat3e.todoapp.model.*;
import io.github.mat3e.todoapp.model.projection.GroupReadModel;
import io.github.mat3e.todoapp.model.projection.GroupWriteModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private ProjectRepository repository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties config;

    public ProjectService(final ProjectRepository repository, final TaskGroupRepository taskGroupRepository, final TaskConfigurationProperties config) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.config = config;
    }

    public List<Project> findAll() {
        return repository.findAll();
    }

    public Project create(final String description, final Set<GroupWriteModel> groupWriteModels, final Set<ProjectStep> steps) {
        Project result = new Project(
                description,
                groupWriteModels
                        .stream()
                        .map(GroupWriteModel::toGroup)
                        .collect(Collectors.toSet()),
                steps);
        repository.save(result);
        return result;
    }

    public Project save(Project toSave) {
        return repository.save(toSave);
    }

    public GroupReadModel createGroup(final int projectId, final LocalDateTime deadline) {
        if (!config.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        TaskGroup result = repository.findById(projectId)
                .map(project -> {
                    var targetGroup = new TaskGroup();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setProject(project);
                    targetGroup.setTasks(project.getProjectSteps().stream()
                            .map(projectStep -> new Task(
                                    projectStep.getDescription(), deadline.plusDays(projectStep.getDaysToDeadline())))
                            .collect(Collectors.toSet())
                    );
                    targetGroup.setProject(project);
                    return taskGroupRepository.save(targetGroup);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
        return new GroupReadModel(result);
//        var optional = repository.findById(projectId);
//        optional.orElseThrow(() -> new IllegalArgumentException("Project with provided id does not exist"));
//        Project project = optional.get();
//        Set<Task> tasks = null;
//        for (ProjectStep step : project.getProjectSteps()) {
//            tasks.add(new Task(step.getDescription(), deadline.plusDays(step.getDaysToDeadline())));
//        }
//        TaskGroup taskGroup = new TaskGroup();
//        taskGroup.setDescription(project.getDescription());
//        taskGroup.setProject(project);
//        taskGroup.setTasks(tasks);
//        taskGroupRepository.save(taskGroup);
//
//        return new GroupReadModel(taskGroup);
    }
}