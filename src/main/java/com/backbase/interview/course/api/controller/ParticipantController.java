package com.backbase.interview.course.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.backbase.interview.course.api.model.Course;
import com.backbase.interview.course.api.model.Participant;
import com.backbase.interview.course.api.model.ParticipantCancel;
import com.backbase.interview.course.service.CourseService;
import com.backbase.interview.course.service.ParticipantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping(ParticipantController.BASE_URL)
public class ParticipantController {

    public static final String BASE_URL = "/courses/{idCourse}/participants";

    @Autowired
    private CourseService courseService;

    @Autowired
    private ParticipantService participantService;

    @Operation(summary = "Register a participant in a course")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Participant registered",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = Participant.class)) }),
        @ApiResponse(responseCode = "400",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(name = "400-1", description = "Invalid input (check the body returned)"),
                    @ExampleObject(name = "400-2", description = "Participant name already registered"),
                    @ExampleObject(name = "400-3", description = "Registration date is 3 days before start of course of after"),
                    @ExampleObject(name = "400-4", description = "Course is full")
            })),
        @ApiResponse(responseCode = "404", description = "Course does not exist",
            content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error. Try again later",
            content = @Content) })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Course createParticipant(@PathVariable Long idCourse, @Valid @RequestBody Participant participant) throws Exception {
        participantService.create(idCourse, participant);
        return courseService.getCourseById(idCourse);
    }

    @Operation(summary = "Cancel a participation in a course")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Participant unregistered",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = Participant.class)) }),
        @ApiResponse(responseCode = "400",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(name = "400-1", description = "Invalid input (check the body returned)"),
                    @ExampleObject(name = "400-2", description = "Cancel date is 3 days before start of course of after")
                })),
        @ApiResponse(responseCode = "404", description = "Course does not exist or user is not enrolled",
            content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error. Try again later",
            content = @Content) })
    @PatchMapping("/{nmParticipant}")
    @ResponseStatus(HttpStatus.OK)
    public Course cancelParticipant(@PathVariable Long idCourse,
            @PathVariable(name = "nmParticipant") String participantName,
            @Valid @RequestBody ParticipantCancel participantCancel) throws Exception {
        participantService.cancel(idCourse, participantName, participantCancel.getCancelDate());
        return courseService.getCourseById(idCourse);
    }
}
