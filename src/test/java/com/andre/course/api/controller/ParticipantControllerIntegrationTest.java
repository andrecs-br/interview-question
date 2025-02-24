package com.andre.course.api.controller;

import java.time.LocalDate;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.andre.CoursesApplication;
import com.andre.course.api.model.Participant;
import com.andre.course.api.model.ParticipantCancel;
import com.andre.course.model.CourseEntity;
import com.andre.course.repository.CourseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {CoursesApplication.class})
public class ParticipantControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testCreateParicipantSuccess() throws Exception {
        CourseEntity courseEntity = generateCourseEntity("title");
        generateParticipantAndValidate(courseEntity);
    }

    @Test
    public void testCreateParticipantWithoutRegistrationDate() throws Exception {
        CourseEntity courseEntity = generateCourseEntity("title");
        Participant participant = generateParticipant();
        participant.setRegistrationDate(null);
        mockMvc.perform( MockMvcRequestBuilders
            .post("/courses/" + courseEntity.getId() + "/participants")
            .content(asJsonString(participant))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testCreateParticipantWithoutName() throws Exception {
        CourseEntity courseEntity = generateCourseEntity("title");
        Participant participant = generateParticipant();
        participant.setName(null);
        mockMvc.perform( MockMvcRequestBuilders
            .post("/courses/" + courseEntity.getId() + "/participants")
            .content(asJsonString(participant))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

        participant.setName("");
        mockMvc.perform( MockMvcRequestBuilders
            .post("/courses/" + courseEntity.getId() + "/participants")
            .content(asJsonString(participant))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testCreateParticipantInvalidCourse() throws Exception {
        Participant participant = generateParticipant();
        mockMvc.perform( MockMvcRequestBuilders
            .post("/courses/" + Integer.MAX_VALUE + "/participants")
            .content(asJsonString(participant))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testCreateParticipantRegistrationDateLessThan3DaysStart() throws Exception {
        CourseEntity courseEntity = generateCourseEntity("title");
        Participant participant = generateParticipant();
        participant.setRegistrationDate(LocalDate.now().plusDays(8));
        mockMvc.perform( MockMvcRequestBuilders
            .post("/courses/" + courseEntity.getId() + "/participants")
            .content(asJsonString(participant))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testCreateParicipantNameAlreadyEnrolled() throws Exception {
        CourseEntity courseEntity = generateCourseEntity("title");
        generateParticipantAndValidate(courseEntity);

        mockMvc.perform( MockMvcRequestBuilders
            .post("/courses/" + courseEntity.getId() + "/participants")
            .content(asJsonString(generateParticipant()))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testCreateParicipantCourseIsFull() throws Exception {
        CourseEntity courseEntity = generateCourseEntity("title", 0);

        mockMvc.perform( MockMvcRequestBuilders
            .post("/courses/" + courseEntity.getId() + "/participants")
            .content(asJsonString(generateParticipant()))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testRemoveParticipantSuccess() throws Exception {
        CourseEntity courseEntity = generateCourseEntity("title");

        generateParticipantAndValidate(courseEntity);

        mockMvc.perform( MockMvcRequestBuilders
            .patch("/courses/" + courseEntity.getId() + "/participants/participant test")
            .content(asJsonString(generateParticipantCancel()))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.participants").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.participants").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.participants", Matchers.hasSize(0)));
    }

    @Test
    public void testRemoveParticipantInvalidCancelDate() throws Exception {
        CourseEntity courseEntity = generateCourseEntity("title");

        generateParticipantAndValidate(courseEntity);

        ParticipantCancel cancel = new ParticipantCancel();
        cancel.setCancelDate(LocalDate.now().plusDays(8));

        mockMvc.perform( MockMvcRequestBuilders
            .patch("/courses/" + courseEntity.getId() + "/participants/participant test")
            .content(asJsonString(cancel))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testRemoveParticipantNullCancelDate() throws Exception {
        CourseEntity courseEntity = generateCourseEntity("title");

        generateParticipantAndValidate(courseEntity);

        ParticipantCancel cancel = new ParticipantCancel();
        cancel.setCancelDate(null);

        mockMvc.perform( MockMvcRequestBuilders
                        .patch("/courses/" + courseEntity.getId() + "/participants/participant test")
                        .content(asJsonString(cancel))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testRemoveParticipantInvalidParticipant() throws Exception {
        CourseEntity courseEntity = generateCourseEntity("title", 0);

        mockMvc.perform( MockMvcRequestBuilders
            .patch("/courses/" + courseEntity.getId() + "/participants/asdfg")
            .content(asJsonString(generateParticipantCancel()))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testRemoveParticipantInvalidCourse() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
            .patch("/courses/" + Integer.MAX_VALUE + "/participants/asdfg")
            .content(asJsonString(generateParticipantCancel()))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    private String asJsonString(final Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Participant generateParticipant() {
        Participant participant = new Participant();
        participant.setName("participant test");
        participant.setRegistrationDate(LocalDate.now().plusDays(5));
        return participant;
    }

    private ParticipantCancel generateParticipantCancel() {
        ParticipantCancel participantCancel = new ParticipantCancel();
        participantCancel.setCancelDate(LocalDate.now().plusDays(5));
        return participantCancel;
    }

    private CourseEntity generateCourseEntity(String title) {
        return generateCourseEntity(title, 10);
    }

    private CourseEntity generateCourseEntity(String title, int remaining) {
        CourseEntity course = new CourseEntity();
        course.setCapacity(10);
        course.setRemaining(remaining);
        course.setTitle(title);
        course.setStartDate(LocalDate.now().plusDays(10));
        course.setEndDate(LocalDate.now().plusDays(20));
        course = courseRepository.save(course);
        return course;
    }

    private void generateParticipantAndValidate(CourseEntity courseEntity) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/courses/" + courseEntity.getId() + "/participants")
            .content(asJsonString(generateParticipant()))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.participants").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.participants").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.participants", Matchers.hasSize(1)));
    }
}
