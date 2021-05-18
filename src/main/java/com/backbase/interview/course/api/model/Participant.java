package com.backbase.interview.course.api.model;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Participant {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Registration date is required")
    private LocalDate registrationDate;

    @Override
    public String toString() {
        return "Participant [name=" + getName() +
                        ", registrationDate=" + getRegistrationDate() + "]";
    }

}
