package com.tecylab.ms.students.app.infrastracture.adapters.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecylab.ms.students.app.application.ports.input.StudentInputPort;
import com.tecylab.ms.students.app.domain.models.Student;
import com.tecylab.ms.students.app.infrastracture.adapters.input.rest.mapper.StudentRestMapper;
import com.tecylab.ms.students.app.infrastracture.adapters.input.rest.models.response.StudentResponse;
import com.tecylab.ms.students.app.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudentRestAdapter.class)
class StudentRestAdapterTest {

  @Autowired
  private MockMvc mockMvc;

  @Mock
  private StudentInputPort inputPort;

  @Mock
  private StudentRestMapper restMapper;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
  }

  //@Test
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
        .andExpect(content().json(objectMapper.writeValueAsString(studentResponse)));

    verify(inputPort, times(1)).findById(1L);
    verify(restMapper, times(1)).toStudentResponse(student);
  }

}
