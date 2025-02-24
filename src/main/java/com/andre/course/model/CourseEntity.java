package com.andre.course.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="TBL_COURSES")
public class CourseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="title", nullable = false, length=50)
    private String title;

    @Column(name="start_date", nullable = false)
    private LocalDate startDate;

    @Column(name="end_date", nullable = false)
    private LocalDate endDate;

    @Column(name="capacity", nullable=false)
    private Integer capacity;

    /**
     * Persisting the remaining places so that we don't need to count participants on every
     * get call to return remaining
     */
    @Column(name="remaining", nullable=false)
    private Integer remaining;

    @Override
    public String toString() {
        return "CourseEntity [id=" + id + ", startDate=" + startDate +
                        ", endDate=" + endDate + ", capacity=" + capacity +
                        ", remaining=" + remaining + "]";
    }

}
