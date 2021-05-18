package com.backbase.interview.course.exception.handler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.backbase.interview.course.exception.BaseException;
import com.backbase.interview.course.exception.LateRegistrationException;
import com.backbase.interview.course.exception.ParticipantAlreadyExistsException;
import com.backbase.interview.course.exception.RecordNotFoundException;
import com.backbase.interview.course.exception.TooManyParticipantsException;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@Hidden
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(Exception exception, WebRequest request) {

        log.info("An exception will be throw: " + exception.getMessage());

        return exception.getMessage() != null ? exception.getMessage() : "Resource not found";
    }

    @ExceptionHandler(LateRegistrationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleLateRegistrationException(Exception exception, WebRequest request) {

        log.info("An exception will be throw: " + exception.getMessage());

        return "Late registration/cancellation to the course";
    }

    @ExceptionHandler(ParticipantAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleParticipantAlreadyExistsException(Exception exception, WebRequest request) {

        log.info("An exception will be throw: " + exception.getMessage());

        return "Participant already registered in the course";
    }

    @ExceptionHandler(TooManyParticipantsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleTooManyParticipantsException(Exception exception, WebRequest request) {

        log.info("An exception will be throw: " + exception.getMessage());

        return "Course if full";
    }

    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleGenericBaseException(Exception exception, WebRequest request) {

        log.info("An exception will be throw: " + exception.getMessage());

        return "Problem executing the operation. Check your data and send it again";
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<String> errorList = ex
            .getBindingResult()
            .getAllErrors()
            .stream()
            .map(error -> error.getDefaultMessage())
            .collect(Collectors.toList());

        return handleExceptionInternal(ex, errorList, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleAnyOtherException(Exception exception, WebRequest request) {

        log.info("An exception will be throw: " + exception.getMessage());

        return "Internal error executing operation. Try again latter.";
    }
}
