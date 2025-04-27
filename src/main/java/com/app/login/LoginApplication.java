package com.app.login;

import com.app.login.entity.User;
import com.app.login.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

@SpringBootApplication
public class LoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginApplication.class, args);
	}

	@Bean
	public CommandLineRunner initAdmin(
			UserService userService,
			BCryptPasswordEncoder passwordEncoder
	) {
		return args -> {
			if (!userService.existsByUsername("admin")) {
				User admin = new User();
				admin.setUserName("admin");
				admin.setPassword("admin");
				admin.setRole("ADMIN");
				admin.setName("Alex");
				admin.setEmail("admin@example.com");
				admin.setBirth(LocalDate.of(1990, 1, 1));
				userService.create(admin);
			}
		};
	}

}
