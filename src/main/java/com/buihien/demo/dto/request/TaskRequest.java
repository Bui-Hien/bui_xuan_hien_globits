package com.buihien.demo.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest implements Serializable {
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotNull(message = "Start time is required")
    @FutureOrPresent(message = "Start time must be in the present or future")
    private LocalDate startTime;

    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    private LocalDate endTime;

    @NotBlank(message = "Priority is required")
    @Pattern(regexp = "LOW|MEDIUM|HIGH", message = "Priority must be one of: LOW, MEDIUM, HIGH")
    private String priority;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "NOT_STARTED|IN_PROGRESS|COMPLETED", message = "Status must be one of: NOT_STARTED, IN_PROGRESS, COMPLETED")
    private String status;

    @NotNull(message = "Project ID is required")
    @Positive(message = "Project ID must be a positive number")
    private Long projectId;

    @NotNull(message = "Person ID is required")
    @Positive(message = "Person ID must be a positive number")
    private Long personId;

}
