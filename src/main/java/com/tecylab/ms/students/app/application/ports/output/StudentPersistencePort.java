package com.tecylab.ms.students.app.application.ports.output;

import com.tecylab.ms.students.app.domain.models.Student;

import java.util.List;
import java.util.Optional;

public interface StudentPersistencePort {

  Optional<Student> findById(Long id);
  List<Student> findAll();
  Student save(Student student);
  void deleteById(Long id);

}
