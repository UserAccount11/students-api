package com.tecylab.ms.students.app.infrastructure.adapters.output.restclient.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "courses-service", url = "http://localhost:8090")
public interface CourseFeignClient {

  @DeleteMapping("/courses/remove-student-from-collection/{studentId}")
  void remoStudentFromCollection(@PathVariable Long studentId);

}
