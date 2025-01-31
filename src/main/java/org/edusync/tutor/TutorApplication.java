package org.edusync.tutor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "org.edusync")
@EntityScan("org.edusync.entity")
@EnableJpaRepositories("org.edusync.repository")
public class TutorApplication {

	public static void main(String[] args) {
		SpringApplication.run(TutorApplication.class, args);
	}

}
