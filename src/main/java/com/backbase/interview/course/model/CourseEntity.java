package com.backbase.interview.course.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
