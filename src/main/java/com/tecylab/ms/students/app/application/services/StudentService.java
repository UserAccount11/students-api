package com.tecylab.ms.students.app.application.services;

import com.tecylab.ms.students.app.application.ports.input.StudentInputPort;
import com.tecylab.ms.students.app.application.ports.output.ExternalCoursesOutputPort;
import com.tecylab.ms.students.app.application.ports.output.StudentPersistencePort;
import com.tecylab.ms.students.app.domain.exceptions.StudentEmailAlreadyExistsException;
import com.tecylab.ms.students.app.domain.exceptions.StudentNotFoundException;
import com.tecylab.ms.students.app.domain.models.Student;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class StudentService implements StudentInputPort {

  private final StudentPersistencePort persistencePort;
  private final ExternalCoursesOutputPort coursesOutputPort;

  @Override
  public Student findById(Long id) {
    return persistencePort.findById(id)
        .orElseThrow(StudentNotFoundException::new);
  }

  @Override
  public List<Student> findByIds(Iterable<Long> ids) {
    return persistencePort.findByIds(ids);
  }

  @Override
  public List<Student> findAll() {
    return persistencePort.findAll();
  }

  @Override
  public Student save(Student student) {
    if (persistencePort.existsByEmail(student.getEmail())) {
      throw new StudentEmailAlreadyExistsException(student.getEmail());
    }
    return persistencePort.save(student);
  }

  @Override
  public Student update(Long id, Student student) {
    if (persistencePort.existsByEmail(student.getEmail())) {
      throw new StudentEmailAlreadyExistsException(student.getEmail());
    }

    return persistencePort.findById(id)
        .map(oldStudent -> {
          oldStudent.setFirstname(student.getFirstname());
          oldStudent.setLastname(student.getLastname());
          oldStudent.setAge(student.getAge());
          oldStudent.setEmail(student.getEmail());
          oldStudent.setAddress(student.getAddress());
          return persistencePort.save(oldStudent);
        }).orElseThrow(StudentNotFoundException::new);
  }

  @Override
  public void deleteById(Long id) {
//    if (persistencePort.findById(id).isPresent()) {
//      persistencePort.deleteById(id);
//    }
//    throw new StudentNotFoundException();
    if (persistencePort.findById(id).isEmpty()) {
      throw new StudentNotFoundException();
    }
    persistencePort.deleteById(id);
    coursesOutputPort.remoStudentFromCollection(id);
  }
}
