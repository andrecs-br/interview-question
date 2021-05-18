package com.backbase.interview.course.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.backbase.interview.course.api.model.Participant;
import com.backbase.interview.course.model.ParticipantEntity;

@Mapper
public interface ParticipantMapper {

    ParticipantMapper INSTANCE = Mappers.getMapper(ParticipantMapper.class);

    Participant participantEntityToParticipant(ParticipantEntity participantEntity);

    ParticipantEntity participantToParticipantEntity(Participant participant);

}
