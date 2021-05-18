package com.backbase.interview.course.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.backbase.interview.course.model.CourseEntity;

@Repository
public interface CourseRepository
        extends CrudRepository<CourseEntity, Long> {

    List<CourseEntity> findByTitleContainingIgnoreCase(String title);
 
}
