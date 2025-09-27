package ru.kukuruzvelt.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Application {
	public static String sourceBase = "B:\\src\\database_substitute.txt";

	public static void main(String[] args) {
		System.out.println("Application Started");
		SpringApplication.run(Application.class, args);
	}




}