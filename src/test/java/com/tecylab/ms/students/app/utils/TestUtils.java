package com.tecylab.ms.students.app.utils;

import com.tecylab.ms.students.app.domain.models.Student;

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


}
