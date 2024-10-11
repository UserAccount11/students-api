package com.tecylab.ms.students.app.infrastracture.adapters.output.persistence;

import com.tecylab.ms.students.app.application.ports.output.StudentPersistencePort;
import com.tecylab.ms.students.app.domain.models.Student;
import com.tecylab.ms.students.app.infrastracture.adapters.output.persistence.mapper.StudentPersistenceMapper;
import com.tecylab.ms.students.app.infrastracture.adapters.output.persistence.models.StudentEntity;
import com.tecylab.ms.students.app.infrastracture.adapters.output.persistence.repository.StudentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StudentPersistenceAdapter implements StudentPersistencePort {

  private final StudentJpaRepository jpaRepository;
  private final StudentPersistenceMapper mapper;

  @Override
  public Optional<Student> findById(Long id) {
    return jpaRepository.findById(id)
        .map(mapper::toStudent);
  }

  @Override
  public List<Student> findByIds(Iterable<Long> ids) {
    return mapper.toStudents(
        (List<StudentEntity>) jpaRepository.findAllById(ids));
  }

  @Override
  public List<Student> findAll() {
    return mapper.toStudents(
        (List<StudentEntity>) jpaRepository.findAll());
  }

  @Override
  public boolean existsByEmail(String email) {
    return jpaRepository.existsByEmailIgnoreCase(email);
  }

  @Override
  public Student save(Student student) {
    return mapper.toStudent(
        jpaRepository.save(mapper.toStudentEntity(student)));
  }

  @Override
  public void deleteById(Long id) {
    jpaRepository.deleteById(id);
  }

}
