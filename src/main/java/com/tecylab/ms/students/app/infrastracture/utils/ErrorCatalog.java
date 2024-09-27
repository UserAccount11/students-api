package com.tecylab.ms.students.app.infrastracture.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCatalog {

  STUDENT_NOT_FOUND("STUDENT_MS_001", "Student not found."),
  STUDENT_BAD_PARAMETERS("STUDENT_MS_002", "Invalid parameters for creation student."),
  INTERNAL_SERVER_ERROR("STUDENT_MS_003", "Internal server error.");

  private final String code;
  private final String message;

}
