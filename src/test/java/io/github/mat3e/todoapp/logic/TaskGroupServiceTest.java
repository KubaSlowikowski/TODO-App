package io.github.mat3e.todoapp.logic;

import io.github.mat3e.todoapp.model.TaskGroup;
import io.github.mat3e.todoapp.model.TaskGroupRepository;
import io.github.mat3e.todoapp.model.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    @DisplayName(value = "should throw IllegalStateException when group contains undone tasks.")
    void toogleGroup_groupContainingUndoneTasks_throwsIllegalStateException() {
        //given
        TaskRepository mockTaskRepository = taskRepositoryReturning(true);

        //system under test
        var toTest = new TaskGroupService(null, mockTaskRepository);

        //when
        var exception = catchThrowable(() -> toTest.toogleGroup(anyInt()));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Group contains undone tasks");

    }

    @Test
    @DisplayName(value = "should throw IllegalArgumentException when there are no taskGroups for given id ")
    void toogleGroup_noUndoneTasksExist_noTaskGroup_throwsIllegalArgumentException() {
        //given
        TaskRepository mockTaskRepository = taskRepositoryReturning(false);
        //and
        TaskGroupRepository mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.empty());
        //system under test
        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);

        //when
        var exception = catchThrowable(() -> toTest.toogleGroup(anyInt()));
        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("TaskGroup with given id not found");

    }

    @Test
    @DisplayName("should save changed taskGroup in repository when group does not contain undone tasks and taskGroup with given id exists")
    void toogleGroup_providedIdIsCorrect_changesAndSavesTaskGroup() {
        //given
        TaskRepository mockTaskRepository = taskRepositoryReturning(false);

        //and
        var group = new TaskGroup();
        var beforeToogle = group.isDone();
        //and
        TaskGroupRepository mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.of(group));

        //system under test
        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);

        //when
        toTest.toogleGroup(anyInt());

        //then
        assertThat(group.isDone()).isEqualTo(!beforeToogle);

    }

    private TaskRepository taskRepositoryReturning(final boolean result) {
        TaskRepository mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(result);
        return mockTaskRepository;
    }
}