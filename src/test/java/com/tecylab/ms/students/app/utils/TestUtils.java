package com.tecylab.ms.students.app.utils;

import com.tecylab.ms.students.app.domain.models.Student;
import com.tecylab.ms.students.app.infrastructure.adapters.input.rest.models.request.StudentCreateRequest;
import com.tecylab.ms.students.app.infrastructure.adapters.input.rest.models.response.StudentResponse;
import com.tecylab.ms.students.app.infrastructure.adapters.output.persistence.models.StudentEntity;

import java.time.LocalDate;

public class TestUtils {

  public static Student buildStudentMock() {
    return Student.builder()
        .id(1L)
        .firstname("Pepito")
        .lastname("Lopez")
        .age(18)
        .email("pepito@email.com")
        .address("Calle 1")
        .build();
  }

  public static StudentEntity buildStudentEntityMock() {
    return StudentEntity.builder()
        .id(1L)
        .firstname("Pepito")
        .lastname("Lopez")
        .age(18)
        .email("pepito@email.com")
        .address("Calle 1")
        .build();
  }

  public static StudentResponse buildStudentResponseMock() {
    return StudentResponse.builder()
        .id(1L)
        .firstname("Pepito")
        .lastname("Lopez")
        .age(18)
        .email("pepito@email.com")
        .address("Calle 1")
        .timestamp(LocalDate.now().toString())
        .build();
  }

  public static StudentCreateRequest buildStudentCreateRequest() {
    return StudentCreateRequest.builder()
        .firstname("Pepito")
        .lastname("Lopez")
        .age(18)
        .email("pepito@email.com")
        .address("Calle 1")
        .build();
  }


}
