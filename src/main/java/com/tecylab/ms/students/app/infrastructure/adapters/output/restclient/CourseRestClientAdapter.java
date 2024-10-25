package com.tecylab.ms.students.app.infrastructure.adapters.output.restclient;

import com.tecylab.ms.students.app.application.ports.output.ExternalCoursesOutputPort;
import com.tecylab.ms.students.app.infrastructure.adapters.output.restclient.client.CourseFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseRestClientAdapter implements ExternalCoursesOutputPort {

  private final CourseFeignClient client;

  @Override
  public void remoStudentFromCollection(Long studentId) {
    client.remoStudentFromCollection(studentId);
  }

}
