package com.andre.course.api.model;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
