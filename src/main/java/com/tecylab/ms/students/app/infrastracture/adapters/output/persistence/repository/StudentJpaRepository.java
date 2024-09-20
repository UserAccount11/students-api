package com.tecylab.ms.students.app.infrastracture.adapters.output.persistence.repository;

import com.tecylab.ms.students.app.infrastracture.adapters.output.persistence.models.StudentEntity;
import org.springframework.data.repository.CrudRepository;

public interface StudentJpaRepository extends CrudRepository<StudentEntity, Long> {
}
