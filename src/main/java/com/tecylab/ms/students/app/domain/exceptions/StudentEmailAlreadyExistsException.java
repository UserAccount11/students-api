package com.tecylab.ms.students.app.domain.exceptions;

public class StudentEmailAlreadyExistsException extends RuntimeException {

  public StudentEmailAlreadyExistsException(String email) {
    super("Student email: " + email + " already exists!");
  }
}
