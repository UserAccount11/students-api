package com.tecylab.ms.students.app.infrastructure.adapters.output.persistence.repository;

import com.tecylab.ms.students.app.infrastructure.adapters.output.persistence.models.StudentEntity;
import org.springframework.data.repository.CrudRepository;

public interface StudentJpaRepository extends CrudRepository<StudentEntity, Long> {

  boolean existsByEmailIgnoreCase(String email);

}
