package com.andre.course.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="TBL_PARTICIPANTS")
public class ParticipantEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="name", nullable = false, length=50)
    private String name;

    @Column(name="registration_date", nullable = false)
    private LocalDate registrationDate;

    @Column(name="cancel_date")
    private LocalDate cancelDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="course_id")
    private CourseEntity course;

    @Override
    public String toString() {
        return "ParticipantEntity [id=" + id + ", name=" + name +
                        ", registrationDate=" + registrationDate + "]";
    }

}
