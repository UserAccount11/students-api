package com.tecylab.ms.students.app.infrastructure.config;

import com.tecylab.ms.students.app.application.ports.input.StudentInputPort;
import com.tecylab.ms.students.app.application.ports.output.ExternalCoursesOutputPort;
import com.tecylab.ms.students.app.application.ports.output.StudentPersistencePort;
import com.tecylab.ms.students.app.application.services.StudentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class StudentServiceConfig {

  @Bean
  public StudentInputPort studentInputPort(
      StudentPersistencePort persistencePort, ExternalCoursesOutputPort coursesOutputPort) {
    return new StudentService(persistencePort, coursesOutputPort);
  }
}
