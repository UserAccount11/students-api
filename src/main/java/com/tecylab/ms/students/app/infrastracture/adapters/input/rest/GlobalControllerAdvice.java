package com.tecylab.ms.students.app.infrastracture.adapters.input.rest;

import com.tecylab.ms.students.app.domain.exceptions.StudentEmailAlreadyExistsException;
import com.tecylab.ms.students.app.domain.exceptions.StudentNotFoundException;
import com.tecylab.ms.students.app.infrastracture.adapters.input.rest.models.response.ErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.util.Collections;

import static com.tecylab.ms.students.app.infrastracture.adapters.input.rest.models.enums.ErrorType.FUNCTIONAL;
import static com.tecylab.ms.students.app.infrastracture.adapters.input.rest.models.enums.ErrorType.SYSTEM;
import static com.tecylab.ms.students.app.infrastracture.utils.ErrorCatalog.INTERNAL_SERVER_ERROR;
import static com.tecylab.ms.students.app.infrastracture.utils.ErrorCatalog.STUDENT_BAD_PARAMETERS;
import static com.tecylab.ms.students.app.infrastracture.utils.ErrorCatalog.STUDENT_EMAIL_ALREADY_EXISTS;
import static com.tecylab.ms.students.app.infrastracture.utils.ErrorCatalog.STUDENT_NOT_FOUND;

@RestControllerAdvice
public class GlobalControllerAdvice {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(StudentNotFoundException.class)
  public ErrorResponse handleStudentNotFoundException() {
    return ErrorResponse.builder()
        .code(STUDENT_NOT_FOUND.getCode())
        .type(FUNCTIONAL)
        .message(STUDENT_NOT_FOUND.getMessage())
        .timestamp(LocalDate.now().toString())
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorResponse handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    BindingResult bindingResult = e.getBindingResult();
    return ErrorResponse.builder()
        .code(STUDENT_BAD_PARAMETERS.getCode())
        .type(FUNCTIONAL)
        .message(STUDENT_BAD_PARAMETERS.getMessage())
        .details(bindingResult.getFieldErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .toList())
        .timestamp(LocalDate.now().toString())
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(StudentEmailAlreadyExistsException.class)
  public ErrorResponse handleStudentEmailAlreadyExistsException(
      StudentEmailAlreadyExistsException e) {
    return ErrorResponse.builder()
        .code(STUDENT_EMAIL_ALREADY_EXISTS.getCode())
        .type(FUNCTIONAL)
        .message(STUDENT_EMAIL_ALREADY_EXISTS.getMessage())
        .details(Collections.singletonList(e.getMessage()))
        .timestamp(LocalDate.now().toString())
        .build();
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public ErrorResponse handleException(Exception e) {
    return ErrorResponse.builder()
        .code(INTERNAL_SERVER_ERROR.getCode())
        .type(SYSTEM)
        .message(INTERNAL_SERVER_ERROR.getMessage())
        .details(Collections.singletonList(e.getMessage()))
        .timestamp(LocalDate.now().toString())
        .build();
  }

}
