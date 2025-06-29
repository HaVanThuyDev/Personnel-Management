package com.example.Personnel.Management;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class PersonnelManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersonnelManagementApplication.class, args);
	}

	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
		System.out.println("Time hiện tại: " + TimeZone.getDefault().getID());
	}
}

