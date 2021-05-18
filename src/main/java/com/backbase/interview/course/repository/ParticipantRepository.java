package com.backbase.interview.course.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.backbase.interview.course.model.CourseEntity;
import com.backbase.interview.course.model.ParticipantEntity;

@Repository
public interface ParticipantRepository
    extends CrudRepository<ParticipantEntity, Long> {

    ParticipantEntity findByNameIgnoreCaseAndCourseAndCancelDateIsNull(String name, CourseEntity course);

    List<ParticipantEntity> findByCourseAndCancelDateIsNull(CourseEntity course);
}
