package com.tecylab.ms.students.app.infrastracture.adapters.input.rest.mapper;

import com.tecylab.ms.students.app.domain.models.Student;
import com.tecylab.ms.students.app.infrastracture.adapters.input.rest.models.request.StudentCreateRequest;
import com.tecylab.ms.students.app.infrastracture.adapters.input.rest.models.response.StudentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentRestMapper {

  Student toStudent(StudentCreateRequest request);

  @Mapping(target = "timestamp", expression = "java(mapTimestamp())")
  StudentResponse toStudentResponse(Student student);

  List<StudentResponse> toStudentResponses(List<Student> students);

  default String mapTimestamp() {
    return LocalDate.now().toString();
  }

}
