package com.backbase.interview.course.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
