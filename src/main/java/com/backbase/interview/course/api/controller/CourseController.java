package com.backbase.interview.course.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.backbase.interview.course.api.model.Course;
import com.backbase.interview.course.service.CourseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping(CourseController.BASE_URL)
public class CourseController {

    public static final String BASE_URL = "/courses";

    @Autowired
    private CourseService service;

    @Operation(summary = "Create a course")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Course created",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = Course.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid input (check the body returned)",
            content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error. Try again later",
            content = @Content) })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Course createCourse(@Valid @RequestBody Course course) {
        return service.create(course);
    }

    @Operation(summary = "Get detail of course and its participants by Id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Course found. Details and participants returned.",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = Course.class)) }),
        @ApiResponse(responseCode = "404", description = "Course not found",
            content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error. Try again later",
            content = @Content) })
    @GetMapping("/{idCourse}")
    @ResponseStatus(HttpStatus.OK)
    public Course getCourseById(@PathVariable Long idCourse) throws Exception {
        return service.getCourseById(idCourse);
    }

    @Operation(summary = "List courses by title (contains). Participants are not returned")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of courses without participants returned or blank if no courses were found.",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = Course.class)) }),
        @ApiResponse(responseCode = "500", description = "Internal server error. Try again later",
                content = @Content) })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Course> getCourseByTitle(@RequestParam(value = "q") String title) {
        return service.getCoursesByPartOfTitle(title);
    }
}
