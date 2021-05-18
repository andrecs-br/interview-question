package com.backbase.interview.course.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backbase.interview.course.api.model.Course;
import com.backbase.interview.course.api.model.Participant;
import com.backbase.interview.course.exception.RecordNotFoundException;
import com.backbase.interview.course.exception.TooManyParticipantsException;
import com.backbase.interview.course.mapper.CourseMapper;
import com.backbase.interview.course.model.CourseEntity;
import com.backbase.interview.course.repository.CourseRepository;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository repository;
    @Autowired
    private ParticipantService participantService;
    @Autowired
    private CourseMapper mapper;

    @Override
    public Course getCourseById(Long id) throws RecordNotFoundException {

        CourseEntity courseEntity = getCourseEntityById(id);

        List<Participant> participants = participantService.getParticipantsByCourse(id);

        Course course = mapper.courseEntityToCourse(courseEntity);

        course.setParticipants(participants);

        return course;
    }

    @Override
    public List<Course> getCoursesByPartOfTitle(String title) {

        return repository
            .findByTitleContainingIgnoreCase(title)
            .stream()
            .map(mapper::courseEntityToCourse)
            .collect(Collectors.toList());

    }

    @Override
    public Course create(Course course) {

        CourseEntity entity = mapper.courseToCourseEntity(course);
        entity.setRemaining(entity.getCapacity());
        entity = repository.save(entity);

        return mapper.courseEntityToCourse(entity);
    }

    @Override
    public void increaseParticipation(Long courseId) throws RecordNotFoundException, TooManyParticipantsException {
        CourseEntity entity = getCourseEntityById(courseId);
        if (entity.getRemaining() == 0) {
            throw new TooManyParticipantsException("Course is full");
        }
        entity.setRemaining(entity.getRemaining() - 1);
        repository.save(entity);
    }

    @Override
    public void decreaseParticipation(Long courseId) throws RecordNotFoundException {
        CourseEntity entity = getCourseEntityById(courseId);
        entity.setRemaining(entity.getRemaining() + 1);
        repository.save(entity);
    }

    private CourseEntity getCourseEntityById(Long id) throws RecordNotFoundException {
        CourseEntity courseEntity =  repository
            .findById(id)
            .orElseThrow(RecordNotFoundException::new);
        return courseEntity;
    }
}
