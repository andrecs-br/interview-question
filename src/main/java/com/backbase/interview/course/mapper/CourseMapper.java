package com.backbase.interview.course.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.backbase.interview.course.api.model.Course;
import com.backbase.interview.course.model.CourseEntity;

@Mapper
public interface CourseMapper {

    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    Course courseEntityToCourse(CourseEntity courseEntity);

    CourseEntity courseToCourseEntity(Course course);

}
