package com.andre.course.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.andre.course.model.CourseEntity;
import com.andre.course.model.ParticipantEntity;

@Repository
public interface ParticipantRepository
    extends CrudRepository<ParticipantEntity, Long> {

    ParticipantEntity findByNameIgnoreCaseAndCourseAndCancelDateIsNull(String name, CourseEntity course);

    List<ParticipantEntity> findByCourseAndCancelDateIsNull(CourseEntity course);
}
