package com.andre.course.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.andre.course.api.model.Participant;
import com.andre.course.model.ParticipantEntity;

@Mapper
public interface ParticipantMapper {

    ParticipantMapper INSTANCE = Mappers.getMapper(ParticipantMapper.class);

    Participant participantEntityToParticipant(ParticipantEntity participantEntity);

    ParticipantEntity participantToParticipantEntity(Participant participant);

}
