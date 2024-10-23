package com.tecylab.ms.students.app.infrastracture.adapters.output.restclient;

import com.tecylab.ms.students.app.infrastracture.adapters.output.restclient.client.CourseFeignClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CourseRestClientAdapterTest {

  @Mock
  private CourseFeignClient client;

  @InjectMocks
  private CourseRestClientAdapter restClientAdapter;

  @Test
  void testRemoStudentFromCollection() {
    doNothing().when(client).remoStudentFromCollection(anyLong());

    restClientAdapter.remoStudentFromCollection(1L);

    verify(client, times(1)).remoStudentFromCollection(1L);
  }
}