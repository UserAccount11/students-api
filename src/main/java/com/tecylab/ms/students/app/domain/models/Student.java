package com.tecylab.ms.students.app.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {

  private Long id;
  private String firstname;
  private String lastname;
  private Integer age;
  private String email;
  private String address;

}
