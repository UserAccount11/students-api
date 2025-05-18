package com.tecylab.ms.students.app.infrastructure.adapters.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecylab.ms.students.app.application.ports.input.StudentInputPort;
import com.tecylab.ms.students.app.domain.exceptions.StudentEmailAlreadyExistsException;
import com.tecylab.ms.students.app.domain.exceptions.StudentNotFoundException;
import com.tecylab.ms.students.app.domain.models.Student;
import com.tecylab.ms.students.app.infrastructure.adapters.input.rest.mapper.StudentRestMapper;
import com.tecylab.ms.students.app.infrastructure.adapters.input.rest.models.request.StudentCreateRequest;
import com.tecylab.ms.students.app.infrastructure.adapters.input.rest.models.response.ErrorResponse;
import com.tecylab.ms.students.app.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.tecylab.ms.students.app.infrastructure.adapters.input.rest.models.enums.ErrorType.FUNCTIONAL;
import static com.tecylab.ms.students.app.infrastructure.adapters.input.rest.models.enums.ErrorType.SYSTEM;
import static com.tecylab.ms.students.app.infrastructure.utils.ErrorCatalog.INTERNAL_SERVER_ERROR;
import static com.tecylab.ms.students.app.infrastructure.utils.ErrorCatalog.STUDENT_BAD_PARAMETERS;
import static com.tecylab.ms.students.app.infrastructure.utils.ErrorCatalog.STUDENT_EMAIL_ALREADY_EXISTS;
import static com.tecylab.ms.students.app.infrastructure.utils.ErrorCatalog.STUDENT_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ContextConfiguration(classes = {
    StudentRestAdapter.class,
    GlobalControllerAdvice.class
})
@WebMvcTest(controllers = { StudentRestAdapter.class })
class GlobalControllerAdviceTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private StudentInputPort inputPort;

  @MockitoBean
  private StudentRestMapper restMapper;

  @Test
  void whenThrowsStudentNotFoundException_thenReturnNotFound() throws Exception {
    when(inputPort.findById(anyLong()))
        .thenThrow(new StudentNotFoundException());

    mockMvc.perform(get("/students/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(result -> {
          ErrorResponse errorResponse = objectMapper.readValue(
              result.getResponse().getContentAsString(), ErrorResponse.class);
          assertAll(
              () -> assertEquals(STUDENT_NOT_FOUND.getCode(), errorResponse.getCode()),
              () -> assertEquals(FUNCTIONAL, errorResponse.getType()),
              () -> assertEquals(STUDENT_NOT_FOUND.getMessage(), errorResponse.getMessage()),
              () -> assertNotNull(errorResponse.getTimestamp()));
        })
        .andDo(print());
  }

  @Test
  void whenThrowsMethodArgumentNotValidException_thenReturnBadRequest() throws Exception {
    //    StudentCreateRequest invalidRequest = StudentCreateRequest.builder()
    //        .lastname("Lopez")
    //        .age(18)
    //        .email("pepito@email.com")
    //        .address("Calle 1")
    //        .build();

    mockMvc.perform(post("/students")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{}"))
        .andExpect(status().isBadRequest())
        .andExpect(result -> {
          ErrorResponse errorResponse = objectMapper.readValue(
              result.getResponse().getContentAsString(), ErrorResponse.class);
          assertAll(
              () -> assertThat(errorResponse.getCode()).isEqualTo(STUDENT_BAD_PARAMETERS.getCode()),
              () -> assertThat(errorResponse.getType()).isEqualTo(FUNCTIONAL),
              () -> assertThat(errorResponse.getMessage()).isEqualTo(STUDENT_BAD_PARAMETERS.getMessage()),
              // () -> assertThat(errorResponse.getDetails().size()).isEqualTo(1),
              () -> assertThat(errorResponse.getDetails()).isNotNull(),
              () -> assertThat(errorResponse.getTimestamp()).isNotNull()
          );
        })
        .andDo(print());
  }

  @Test
  void whenThrowsStudentEmailAlreadyExistsException_thenReturnBadRequest() throws Exception {
    StudentCreateRequest updateRequest = TestUtils.buildStudentCreateRequest();
    Student student = TestUtils.buildStudentMock();

    when(restMapper.toStudent(any(StudentCreateRequest.class)))
        .thenReturn(student);

    when(inputPort.update(anyLong(), any(Student.class)))
        .thenThrow(new StudentEmailAlreadyExistsException(updateRequest.getEmail()));

    mockMvc.perform(put("/students/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(result -> {
          ErrorResponse errorResponse = objectMapper.readValue(
              result.getResponse().getContentAsString(), ErrorResponse.class);
          assertEquals(STUDENT_EMAIL_ALREADY_EXISTS.getCode(), errorResponse.getCode());
          assertEquals(FUNCTIONAL, errorResponse.getType());
          assertEquals(STUDENT_EMAIL_ALREADY_EXISTS.getMessage(), errorResponse.getMessage());
          assertEquals("Student email: " + updateRequest.getEmail() + " already exists!", errorResponse.getDetails().get(0));
          assertNotNull(errorResponse.getTimestamp());
        })
        .andDo(print());
  }

  @Test
  void whenThrowsGenericException_thenReturnInternalServerErrorResponse() throws Exception {
    when(inputPort.findAll())
        .thenThrow(new RuntimeException("Generic error"));

    mockMvc.perform(get("/students")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError())
        .andExpect(result -> {
          ErrorResponse errorResponse = objectMapper.readValue(
              result.getResponse().getContentAsString(), ErrorResponse.class);
          assertThat(errorResponse.getCode()).isEqualTo(INTERNAL_SERVER_ERROR.getCode());
          assertThat(errorResponse.getType()).isEqualTo(SYSTEM);
          assertThat(errorResponse.getMessage()).isEqualTo(INTERNAL_SERVER_ERROR.getMessage());
          assertThat(errorResponse.getDetails().get(0)).isEqualTo("Generic error");
          assertThat(errorResponse.getTimestamp()).isNotNull();
        })
        .andDo(print());
  }

}