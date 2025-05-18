package com.tecylab.ms.students.app.application.services;

import com.tecylab.ms.students.app.application.ports.output.ExternalCoursesOutputPort;
import com.tecylab.ms.students.app.application.ports.output.StudentPersistencePort;
import com.tecylab.ms.students.app.domain.exceptions.StudentEmailAlreadyExistsException;
import com.tecylab.ms.students.app.domain.exceptions.StudentNotFoundException;
import com.tecylab.ms.students.app.domain.models.Student;
import com.tecylab.ms.students.app.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentPersistencePort persistencePort;

  @Mock
  private ExternalCoursesOutputPort coursesOutputPort;

  @InjectMocks
  private StudentService studentService;

  @Test
  void givenExistingStudent_whenFindById_thenReturnStudent() {
    // Inicialización
    when(persistencePort.findById(anyLong()))
        .thenReturn(Optional.of(TestUtils.buildStudentMock()));

    // Evaluación del comportamiento
    Student student = studentService.findById(1L);

    // Comprobaciones o aserciones
    assertNotNull(student);
    assertEquals(1L, student.getId());
    assertEquals("Pepito", student.getFirstname());

    Mockito.verify(persistencePort, times(1)).findById(1L);
  }

  @Test
  void givenNonExistingStudent_whenFindById_thenThrowStudentNotFoundException() {
    when(persistencePort.findById(anyLong()))
        .thenReturn(Optional.empty());

    assertThrows(StudentNotFoundException.class, () -> studentService.findById(1L));

    Mockito.verify(persistencePort, times(1)).findById(1L);
  }

  @Test
  void givenValidStudentIds_whenFindByIds_thenReturnListOfStudents() {
    List<Long> ids = Collections.singletonList(1L);

    when(persistencePort.findByIds(anyCollection()))
        .thenReturn(Collections.singletonList(TestUtils.buildStudentMock()));

    List<Student> list = studentService.findByIds(ids);

    assertFalse(list.isEmpty());
    assertEquals(1, list.size());
    assertEquals(1L, list.getFirst().getId());
    assertEquals("Pepito", list.getFirst().getFirstname());

    Mockito.verify(persistencePort, times(1)).findByIds(ids);
  }

  @Test
  void givenStudentsExist_whenFindAll_thenReturnAllStudents() {
    when(persistencePort.findAll())
        .thenReturn(Collections.singletonList(TestUtils.buildStudentMock()));

    List<Student> list = studentService.findAll();

    assertFalse(list.isEmpty());
    assertEquals(1, list.size());
    assertEquals(1L, list.getFirst().getId());
    assertEquals("Pepito", list.getFirst().getFirstname());

    Mockito.verify(persistencePort, times(1)).findAll();
  }

  @Test
  void givenNewStudentWithUniqueEmail_whenSave_thenPersistAndReturnStudent() {
    Student studentToSave = Student.builder()
        .id(1L)
        .firstname("Pepito")
        .lastname("Lopez")
        .age(18)
        .email("pepito@email.com")
        .address("Calle 1")
        .build();

    when(persistencePort.existsByEmail(anyString()))
        .thenReturn(Boolean.FALSE);

    when(persistencePort.save(any(Student.class)))
        .thenReturn(TestUtils.buildStudentMock());

    Student student = studentService.save(studentToSave);

    assertNotNull(student);
    assertEquals(1L, student.getId());
    assertEquals("Pepito", student.getFirstname());
    assertEquals("Lopez", student.getLastname());
    assertEquals(18, student.getAge());
    assertEquals("pepito@email.com", student.getEmail());
    assertEquals("Calle 1", student.getAddress());

    Mockito.verify(persistencePort, times(1)).existsByEmail("pepito@email.com");
    Mockito.verify(persistencePort, times(1)).save(studentToSave);
  }

  @Test
  void givenStudentWithExistingEmail_whenSave_thenThrowEmailAlreadyExistsException() {
    Student studentToSave = Student.builder()
        .id(1L)
        .firstname("Pepito")
        .lastname("Lopez")
        .age(18)
        .email("pepito@email.com")
        .address("Calle 1")
        .build();

    when(persistencePort.existsByEmail(anyString()))
        .thenReturn(Boolean.TRUE);

    assertThrows(StudentEmailAlreadyExistsException.class, () -> studentService.save(studentToSave));

    Mockito.verify(persistencePort, times(1)).existsByEmail("pepito@email.com");
    Mockito.verify(persistencePort, times(0)).save(studentToSave);
  }

  @Test
  void givenValidUpdateAndUniqueEmail_whenUpdate_thenUpdateAndReturnStudent() {
    Student studentToUpdate = Student.builder()
        .firstname("Pepito")
        .lastname("Lopez")
        .age(18)
        .email("pepito@email.com")
        .address("Calle 1")
        .build();

    when(persistencePort.existsByEmail(anyString()))
        .thenReturn(Boolean.FALSE);

    when(persistencePort.findById(anyLong()))
        .thenReturn(Optional.of(studentToUpdate));

    when(persistencePort.save(any(Student.class)))
        .thenReturn(studentToUpdate);

    Student student = studentService.update(1L, studentToUpdate);

    assertNotNull(student);
    assertEquals("Pepito", student.getFirstname());
    assertEquals("Lopez", student.getLastname());
    assertEquals(18, student.getAge());
    assertEquals("pepito@email.com", student.getEmail());
    assertEquals("Calle 1", student.getAddress());

    Mockito.verify(persistencePort, times(1)).existsByEmail("pepito@email.com");
    Mockito.verify(persistencePort, times(1)).findById(1L);
    Mockito.verify(persistencePort, times(1)).save(studentToUpdate);
  }

  @Test
  void givenExistingEmail_whenUpdate_thenThrowEmailAlreadyExistsException() {
    Student studentToUpdate = Student.builder()
        .firstname("Pepito")
        .lastname("Lopez")
        .age(18)
        .email("pepito@email.com")
        .address("Calle 1")
        .build();

    when(persistencePort.existsByEmail(anyString()))
        .thenReturn(Boolean.TRUE);

    assertThrows(StudentEmailAlreadyExistsException.class, () -> studentService.update(1L, studentToUpdate));

    Mockito.verify(persistencePort, times(1)).existsByEmail("pepito@email.com");
    Mockito.verify(persistencePort, times(0)).findById(1L);
    Mockito.verify(persistencePort, times(0)).save(studentToUpdate);
  }

  @Test
  void givenNonExistingStudent_whenUpdate_thenThrowStudentNotFoundException() {
    Student studentToUpdate = Student.builder()
        .firstname("Pepito")
        .lastname("Lopez")
        .age(18)
        .email("pepito@email.com")
        .address("Calle 1")
        .build();

    when(persistencePort.existsByEmail(anyString()))
        .thenReturn(Boolean.FALSE);

    when(persistencePort.findById(anyLong()))
        .thenReturn(Optional.empty());

    assertThrows(StudentNotFoundException.class, () -> studentService.update(1L, studentToUpdate));

    Mockito.verify(persistencePort, times(1)).existsByEmail("pepito@email.com");
    Mockito.verify(persistencePort, times(1)).findById(1L);
    Mockito.verify(persistencePort, times(0)).save(studentToUpdate);
  }

  @Test
  void givenExistingStudent_whenDeleteById_thenDeleteAndRemoveFromCourses() {
    when(persistencePort.findById(anyLong()))
        .thenReturn(Optional.of(TestUtils.buildStudentMock()));

    studentService.deleteById(1L);

    Mockito.verify(persistencePort, times(1)).deleteById(1L);
    Mockito.verify(coursesOutputPort, times(1)).remoStudentFromCollection(1L);
  }

  @Test
  void givenNonExistingStudent_whenDeleteById_thenThrowStudentNotFoundException() {
    when(persistencePort.findById(anyLong()))
        .thenReturn(Optional.empty());

    assertThrows(StudentNotFoundException.class, () -> studentService.deleteById(1L));

    Mockito.verify(persistencePort, times(0)).deleteById(1L);
    Mockito.verify(coursesOutputPort, times(0)).remoStudentFromCollection(1L);
  }

}