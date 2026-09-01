package com.sunrise.dentalclinic;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SunriseDentalClinicApplication {

	public static void main(String[] args) {
		SpringApplication.run(SunriseDentalClinicApplication.class, args);
	}

	@Bean
	CommandLineRunner createAdmin(UserRepository userRepository) {
		return args -> {


			if (userRepository.findByUsername("admin").isEmpty()) {

				User admin = new User();

				admin.setUsername("admin");
				admin.setPassword("admin123");

				userRepository.save(admin);

				System.out.println("Admin user created successfully.");
			}
		};
	}
}