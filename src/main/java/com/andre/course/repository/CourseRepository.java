package com.andre.course.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.andre.course.model.CourseEntity;

@Repository
public interface CourseRepository
        extends CrudRepository<CourseEntity, Long> {

    List<CourseEntity> findByTitleContainingIgnoreCase(String title);
 
}
