package com.andre.course.service;

import java.util.List;

import com.andre.course.api.model.Course;
import com.andre.course.exception.RecordNotFoundException;
import com.andre.course.exception.TooManyParticipantsException;

public interface CourseService {

    /**
     * Get a Course by its id
     * @param id - id to be selected
     * @return the course
     * @throws RecordNotFoundException if no course with the id was found
     */
    Course getCourseById(Long id) throws RecordNotFoundException;

    /**
     * List courses by part of the title provided. This is a like %title% search
     * @param title the part of the title to execute the search
     * @return a lits of courses whithout participants that corresponds to the criteria
     */
    List<Course> getCoursesByPartOfTitle(String title);

    /**
     * Create the course in the repository
     * @param course
     * @return
     */
    Course create(Course course);

    /**
     * Decrement the remaining column for the course provided
     * @param courseId - course to be decremented
     * @throws RecordNotFoundException - course doesn't exist
     * @throws TooManyParticipantsException - If the course is full
     */
    void increaseParticipation(Long courseId) throws RecordNotFoundException, TooManyParticipantsException;

    /**
     * Increment the remaining column for the course provided
     * @param courseId - course to be decremented
     * @throws RecordNotFoundException - course doesn't exist
     */
    void decreaseParticipation(Long courseId) throws RecordNotFoundException;
}
