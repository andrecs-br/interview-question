package com.backbase.interview.course.api.model;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ParticipantCancel {

    @NotNull(message = "Cancel date is required")
    private LocalDate cancelDate;

    @Override
    public String toString() {
        return "Participant [cancelDate=" + getCancelDate() + "]";
    }
}
