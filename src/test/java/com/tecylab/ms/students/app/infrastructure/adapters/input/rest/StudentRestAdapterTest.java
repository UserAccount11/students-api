package com.tecylab.ms.students.app.infrastructure.adapters.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecylab.ms.students.app.application.ports.input.StudentInputPort;
import com.tecylab.ms.students.app.domain.models.Student;
import com.tecylab.ms.students.app.infrastructure.adapters.input.rest.mapper.StudentRestMapper;
import com.tecylab.ms.students.app.infrastructure.adapters.input.rest.models.request.StudentCreateRequest;
import com.tecylab.ms.students.app.infrastructure.adapters.input.rest.models.response.StudentResponse;
import com.tecylab.ms.students.app.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentRestAdapter.class)
class StudentRestAdapterTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private StudentInputPort inputPort;

  @MockBean
  private StudentRestMapper restMapper;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
  }

  @Test
  @DisplayName("Test find by id when id exists")
  void testFindById() throws Exception {
    Student student = TestUtils.buildStudentMock();
    StudentResponse studentResponse = TestUtils.buildStudentResponseMock();

    when(inputPort.findById(anyLong()))
        .thenReturn(student);

    when(restMapper.toStudentResponse(any(Student.class)))
        .thenReturn(studentResponse);

    mockMvc
        .perform(get("/students/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(studentResponse)))
        .andDo(print());

    verify(inputPort, times(1)).findById(1L);
    verify(restMapper, times(1)).toStudentResponse(student);
  }

  @Test
  void testFindByIds() throws Exception {
    List<Student> students = Collections.singletonList(TestUtils.buildStudentMock());
    List<StudentResponse> responses = Collections.singletonList(TestUtils.buildStudentResponseMock());

    when(inputPort.findByIds(anyList()))
        .thenReturn(students);

    when(restMapper.toStudentResponses(anyList()))
        .thenReturn(responses);

    mockMvc.perform(get("/students/find-by-ids")
        .param("ids", "1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(responses)))
        .andDo(print());

    verify(inputPort, times(1)).findByIds(anyList());
    verify(restMapper, times(1)).toStudentResponses(anyList());
  }

  @Test
  void testFindAll() throws Exception {
    List<Student> students = Collections.singletonList(TestUtils.buildStudentMock());
    List<StudentResponse> responses = Collections.singletonList(TestUtils.buildStudentResponseMock());

    when(inputPort.findAll())
        .thenReturn(students);

    when(restMapper.toStudentResponses(anyList()))
        .thenReturn(responses);

    mockMvc.perform(get("/students")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(responses)))
        .andDo(print());

    verify(inputPort, times(1)).findAll();
    verify(restMapper, times(1)).toStudentResponses(anyList());
  }

  @Test
  void testSave() throws Exception {
    StudentCreateRequest request = TestUtils.buildStudentCreateRequest();
    Student student = TestUtils.buildStudentMock();
    StudentResponse response = TestUtils.buildStudentResponseMock();

    when(restMapper.toStudent(any(StudentCreateRequest.class)))
        .thenReturn(student);

    when(inputPort.save(any(Student.class)))
        .thenReturn(student);

    when(restMapper.toStudentResponse(any(Student.class)))
        .thenReturn(response);

    mockMvc.perform(post("/students")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/students/1"))
        .andExpect(content().json(objectMapper.writeValueAsString(response)))
        .andDo(print());

    verify(restMapper, times(1)).toStudent(any(StudentCreateRequest.class));
    verify(inputPort, times(1)).save(student);
    verify(restMapper, times(1)).toStudentResponse(student);
  }

  @Test
  void testUpdate() throws Exception {
    StudentCreateRequest request = TestUtils.buildStudentCreateRequest();
    Student student = TestUtils.buildStudentMock();
    StudentResponse response = TestUtils.buildStudentResponseMock();

    when(restMapper.toStudent(any(StudentCreateRequest.class)))
        .thenReturn(student);

    when(inputPort.update(anyLong(), any(Student.class)))
        .thenReturn(student);

    when(restMapper.toStudentResponse(any(Student.class)))
        .thenReturn(response);

    mockMvc.perform(put("/students/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(result -> {
          StudentResponse updatedStudent = objectMapper.readValue(
              result.getResponse().getContentAsString(), StudentResponse.class);
          assertEquals("Pepito", updatedStudent.getFirstname());
          assertThat(updatedStudent.getLastname()).isEqualTo("Lopez");
        })
        .andDo(print());

    verify(restMapper, times(1)).toStudent(any(StudentCreateRequest.class));
    verify(inputPort, times(1)).update(anyLong(), any(Student.class));
    verify(restMapper, times(1)).toStudentResponse(any(Student.class));
  }

  void testDelete() throws Exception {
    doNothing().when(inputPort).deleteById(anyLong());

    mockMvc.perform(delete("/students/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent())
        .andDo(print());

    verify(inputPort, times(1)).deleteById(1L);
  }

}
