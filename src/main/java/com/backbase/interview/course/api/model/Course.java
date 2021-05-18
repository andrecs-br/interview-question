package com.backbase.interview.course.api.model;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.backbase.interview.course.constraints.DateAfter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@DateAfter(startDate = "startDate", endDate = "endDate")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Course {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 50, message = "Title needs to have up to 50 characters")
    private String title;

    @Future(message = "Start date must be a future date")
    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @Min(value = 1, message = "Capacity must be greater than 0")
    private Integer capacity;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer remaining;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Participant> participants;

    @Override
    public String toString() {
        return "CourseEntity [id=" + id + ", startDate=" + startDate +
                        ", endDate=" + endDate + ", capacity=" + capacity +
                        ", remaining=" + remaining + "]";
    }

}
