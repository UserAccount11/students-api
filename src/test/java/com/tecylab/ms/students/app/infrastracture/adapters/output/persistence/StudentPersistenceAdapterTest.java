package com.tecylab.ms.students.app.infrastracture.adapters.output.persistence;

import com.tecylab.ms.students.app.domain.models.Student;
import com.tecylab.ms.students.app.infrastracture.adapters.output.persistence.mapper.StudentPersistenceMapper;
import com.tecylab.ms.students.app.infrastracture.adapters.output.persistence.models.StudentEntity;
import com.tecylab.ms.students.app.infrastracture.adapters.output.persistence.repository.StudentJpaRepository;
import com.tecylab.ms.students.app.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentPersistenceAdapterTest {

  @Mock
  private StudentJpaRepository jpaRepository;

  @Mock
  private StudentPersistenceMapper mapper;

  @InjectMocks
  private StudentPersistenceAdapter persistenceAdapter;

  @Test
  void testFindById_Success() {
    Student studentExpected = TestUtils.buildStudentMock();
    StudentEntity entity = TestUtils.buildStudentEntityMock();

    when(jpaRepository.findById(anyLong()))
        .thenReturn(Optional.of(entity));

    when(mapper.toStudent(any(StudentEntity.class)))
        .thenReturn(studentExpected);

    Optional<Student> studentFounded = persistenceAdapter.findById(1L);

//    assertTrue(studentFounded.isPresent());
//    assertEquals(studentExpected, studentFounded.get());
//    assertEquals(2L, studentFounded.get().getId());
//    assertEquals("Juan", studentFounded.get().getFirstname());

    assertAll(
        () -> assertTrue(studentFounded.isPresent()),
        () -> assertEquals(studentExpected, studentFounded.get()),
        () -> assertEquals(1L, studentFounded.get().getId()),
        () -> assertEquals("Pepito", studentFounded.get().getFirstname()));

    verify(jpaRepository, times(1)).findById(1L);
    verify(mapper, times(1)).toStudent(entity);
  }

  @Test
  void testFindById_NotFound() {
    when(jpaRepository.findById(anyLong()))
        .thenReturn(Optional.empty());

    Optional<Student> studentFounded = persistenceAdapter.findById(1L);

    assertAll(
        () -> assertFalse(studentFounded.isPresent()),
        () -> assertTrue(studentFounded.isEmpty()));

    verify(jpaRepository, times(1)).findById(1L);
    verify(mapper, times(0)).toStudent(any());
  }

  @Test
  void testFindByIds_Success() {
    List<StudentEntity> entities = Collections.singletonList(TestUtils.buildStudentEntityMock());
    List<Student> students = Collections.singletonList(TestUtils.buildStudentMock());

    when(jpaRepository.findAllById(anyList()))
        .thenReturn(entities);

    when(mapper.toStudents(anyList()))
        .thenReturn(students);

    List<Student> studentsFounded = persistenceAdapter.findByIds(Collections.singletonList(1L));

    assertAll(
        () -> assertFalse(studentsFounded.isEmpty()),
        () -> assertEquals(1, studentsFounded.size()));

    verify(jpaRepository, times(1)).findAllById(anyList());
    verify(mapper, times(1)).toStudents(anyList());
  }

  @Test
  void testFindByAll_Success() {
    List<StudentEntity> entities = Collections.singletonList(TestUtils.buildStudentEntityMock());
    List<Student> students = Collections.singletonList(TestUtils.buildStudentMock());

    when(jpaRepository.findAll())
        .thenReturn(entities);

    when(mapper.toStudents(anyList()))
        .thenReturn(students);

    List<Student> studentsFounded = persistenceAdapter.findAll();

    assertAll(
        () -> assertFalse(studentsFounded.isEmpty()),
        () -> assertEquals(1, studentsFounded.size()));

    verify(jpaRepository, times(1)).findAll();
    verify(mapper, times(1)).toStudents(anyList());
  }

  @Test
  void testExistsByEmail_True() {
    when(jpaRepository.existsByEmailIgnoreCase(anyString()))
        .thenReturn(Boolean.TRUE);

    boolean isExists = persistenceAdapter.existsByEmail("jhon.doe@gmail.com");

    assertTrue(isExists);

    verify(jpaRepository, times(1)).existsByEmailIgnoreCase(anyString());
  }

  @Test
  void testSave() {
    Student student = TestUtils.buildStudentMock();
    StudentEntity entity = TestUtils.buildStudentEntityMock();

    when(mapper.toStudentEntity(any(Student.class)))
        .thenReturn(entity);

    when(jpaRepository.save(any(StudentEntity.class)))
        .thenReturn(entity);

    when(mapper.toStudent(any(StudentEntity.class)))
        .thenReturn(student);

    Student savedStudent = persistenceAdapter.save(student);

    assertNotNull(savedStudent);
    assertEquals(student, savedStudent);

    verify(mapper, times(1)).toStudentEntity(student);
    verify(jpaRepository, times(1)).save(entity);
    verify(mapper, times(1)).toStudent(entity);
  }

  @Test
  void testDeleteById() {
    doNothing().when(jpaRepository).deleteById(anyLong());

    persistenceAdapter.deleteById(1L);

    verify(jpaRepository, times(1)).deleteById(1L);
  }

}