package com.backbase.interview.course.api.controller;

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

import com.backbase.interview.CoursesApplication;
import com.backbase.interview.course.api.model.Course;
import com.backbase.interview.course.model.CourseEntity;
import com.backbase.interview.course.repository.CourseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {CoursesApplication.class})
public class CourseControllerIntegrationTest {

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
    public void testCreateCourseSuccess() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
            .post("/courses")
            .content(asJsonString(generateCourse()))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.participants").doesNotExist());
    }

    @Test
    public void testCreateCourseEndDateBeforeStartDate() throws Exception {
        Course course = generateCourse();
        course.setEndDate(LocalDate.now().plusDays(8));
        mockMvc.perform( MockMvcRequestBuilders
            .post("/courses")
            .content(asJsonString(course))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testCreateCourseWithoutTitle() throws Exception {
        Course course = generateCourse();
        course.setTitle(null);
        mockMvc.perform(MockMvcRequestBuilders
            .post("/courses")
            .content(asJsonString(course))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

        course.setTitle("");
        mockMvc.perform(MockMvcRequestBuilders
            .post("/courses")
            .content(asJsonString(course))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testCreateCourseWithoutStartDate() throws Exception {
        Course course = generateCourse();
        course.setStartDate(null);
        mockMvc.perform(MockMvcRequestBuilders
            .post("/courses")
            .content(asJsonString(course))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testCreateCourseWithoutEndDate() throws Exception {
        Course course = generateCourse();
        course.setEndDate(null);
        mockMvc.perform(MockMvcRequestBuilders
            .post("/courses")
            .content(asJsonString(course))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testCreateCourseStartDateBeforeToday() throws Exception {
        Course course = generateCourse();
        course.setStartDate(LocalDate.now().plusDays(-2));
        mockMvc.perform(MockMvcRequestBuilders
            .post("/courses")
            .content(asJsonString(course))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testGetCourseByIdNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .get("/courses/" + Integer.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testGetCourseById() throws Exception {
        CourseEntity courseEntity = generateCourseEntity("title");

        mockMvc.perform(MockMvcRequestBuilders
            .get("/courses/" + courseEntity.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.participants").exists());
    }

    @Test
    public void testSearchCourseByTitle() throws Exception {
        generateCourseEntity("title to search");
        generateCourseEntity("title to search 2");

        mockMvc.perform(MockMvcRequestBuilders
            .get("/courses?q=search")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
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

    private Course generateCourse() {
        Course course = new Course();
        course.setCapacity(10);
        course.setTitle("title");
        course.setStartDate(LocalDate.now().plusDays(10));
        course.setEndDate(LocalDate.now().plusDays(20));
        return course;
    }

    private CourseEntity generateCourseEntity(String title) {
        CourseEntity course = new CourseEntity();
        course.setCapacity(10);
        course.setRemaining(10);
        course.setTitle(title);
        course.setStartDate(LocalDate.now().plusDays(10));
        course.setEndDate(LocalDate.now().plusDays(20));
        course = courseRepository.save(course);
        return course;
    }
}
