package com.tecylab.ms.students.app.infrastracture.adapters.input.rest.models.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class StudentCreateRequest {

  @NotBlank(message = "Field firstname cannot be null or blank")
  private String firstname;

  @NotBlank(message = "Field lastname cannot be null or blank")
  private String lastname;

  @NotNull(message = "Field age cannot be null")
  @Min(value = 1, message = "Field age must be greater than zero")
  private Integer age;

  @NotBlank(message = "Field email cannot be null or blank")
  @Email(message = "Field email invalid format")
  private String email;

  @NotBlank(message = "Field address cannot be null or blank")
  private String address;

}
