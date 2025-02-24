package com.andre.course.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.andre.course.api.model.Course;
import com.andre.course.model.CourseEntity;

@Mapper
public interface CourseMapper {

    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    Course courseEntityToCourse(CourseEntity courseEntity);

    CourseEntity courseToCourseEntity(Course course);

}
