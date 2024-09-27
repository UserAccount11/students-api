package com.tecylab.ms.students.app;

import com.tecylab.ms.students.app.infrastracture.adapters.output.persistence.models.StudentEntity;
import com.tecylab.ms.students.app.infrastracture.adapters.output.persistence.repository.StudentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
@RequiredArgsConstructor
public class StudentsServiceApplication implements CommandLineRunner {

	private final StudentJpaRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(StudentsServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		repository.saveAll(Arrays.asList(
				new StudentEntity(null, "Juan", "Lopez", 27, "juan@gmail.com", "Calle1"),
				new StudentEntity(null, "Pepe", "Hidalgo", 34, "pepe@gmail.com", "Calle2"),
				new StudentEntity(null, "Maria", "Guevara", 40, "maria@gmail.com", "Calle3")
		));
	}

}
