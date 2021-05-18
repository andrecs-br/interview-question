package com.backbase.interview.course.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backbase.interview.course.api.model.Participant;
import com.backbase.interview.course.exception.BaseException;
import com.backbase.interview.course.exception.LateRegistrationException;
import com.backbase.interview.course.exception.ParticipantAlreadyExistsException;
import com.backbase.interview.course.exception.RecordNotFoundException;
import com.backbase.interview.course.mapper.ParticipantMapper;
import com.backbase.interview.course.model.CourseEntity;
import com.backbase.interview.course.model.ParticipantEntity;
import com.backbase.interview.course.repository.ParticipantRepository;

@Service
public class ParticipantServiceImpl implements ParticipantService {

    @Autowired
    private ParticipantRepository repository;
    @Autowired
    private CourseService courseService;
    @Autowired
    private ParticipantMapper mapper;


    @Override
    public List<Participant> getParticipantsByCourse(Long courseId) {

        CourseEntity course = new CourseEntity();
        course.setId(courseId);

        return repository
            .findByCourseAndCancelDateIsNull(course)
            .stream()
            .map(mapper::participantEntityToParticipant)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public Participant create(Long courseId, Participant participant) throws BaseException {

        validateCreation(courseId, participant);

        courseService.increaseParticipation(courseId);

        ParticipantEntity participantEntity = mapper.participantToParticipantEntity(participant);

        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setId(courseId);

        participantEntity.setCourse(courseEntity);

        return mapper.participantEntityToParticipant(repository.save(participantEntity));
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void cancel(Long courseId, String participantName, LocalDate cancelDate) throws BaseException {

        ParticipantEntity entity = validateRemoval(courseId, participantName, cancelDate);

        entity.setCancelDate(cancelDate);

        courseService.decreaseParticipation(courseId);

        repository.save(entity);
    }

    private void validateCreation(Long courseId, Participant participant)
            throws ParticipantAlreadyExistsException, RecordNotFoundException, LateRegistrationException {

        validateDates(courseId, participant.getRegistrationDate());

        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setId(courseId);

        ParticipantEntity participantEntity = repository.findByNameIgnoreCaseAndCourseAndCancelDateIsNull(participant.getName(), courseEntity);
        if (participantEntity != null) {
            throw new ParticipantAlreadyExistsException("Participant already exists");
        }
    }

    private ParticipantEntity validateRemoval(Long courseId, String participantName, LocalDate cancelDate)
            throws RecordNotFoundException, LateRegistrationException {

        validateDates(courseId, cancelDate);

        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setId(courseId);

        ParticipantEntity participantEntity = repository.findByNameIgnoreCaseAndCourseAndCancelDateIsNull(participantName, courseEntity);
        if (participantEntity == null) {
            throw new RecordNotFoundException("Participant not found");
        }
        return participantEntity;
    }

    private void validateDates(Long courseId, LocalDate dateToCompare) throws RecordNotFoundException, LateRegistrationException {
        LocalDate courseStartDate = courseService.getCourseById(courseId).getStartDate();

        long daysBetweenDates = ChronoUnit.DAYS.between(dateToCompare, courseStartDate);

        if (daysBetweenDates <= 3) {
            throw new LateRegistrationException("Date should be more than 3 days before course start");
        }
    }
}
