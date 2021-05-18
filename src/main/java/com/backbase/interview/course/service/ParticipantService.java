package com.backbase.interview.course.service;

import java.time.LocalDate;
import java.util.List;

import com.backbase.interview.course.api.model.Participant;
import com.backbase.interview.course.exception.BaseException;

public interface ParticipantService {

    /**
     * List the participants for course
     * @param courseId - The course to be listed
     * @return a list of participants or a list with size 0 wif no participants/course found
     */

    List<Participant> getParticipantsByCourse(Long courseId);

    /**
     * Create a participant for a course
     * @param courseId - the course for the participant
     * @param participant - Participant to be created
     * @return
     * @throws BaseException - in case of course does not exist, course is full, participant is already registered,
     * registration date is less than 3 days before the start of the course.
     */
    Participant create(Long courseId, Participant participant) throws BaseException;

    /**
     * Cancel a participation of a course
     * @param courseId - the course for the participant
     * @param participantName - name of the participant
     * @param cancelDate - date for the cancel
     * @throws BaseException - in case of course does not exist, participant not found for the course,
     * cancel date is less than 3 days before the start of the course.
     */
    void cancel(Long courseId, String participantName, LocalDate cancelDate) throws BaseException;
}
