package io.github.mat3e.todoapp.model;

import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Embeddable
class Audit {
    //@Transient //tego pola nie chcemy zapisać w bazie danych
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    @PrePersist
        //funkcja odpali sie tuż przed zapisem do bazy danych
    void prePersist() {
        createdOn = LocalDateTime.now();
    }

    @PreUpdate
    void preMerge() {
        updatedOn = LocalDateTime.now();
    }
}