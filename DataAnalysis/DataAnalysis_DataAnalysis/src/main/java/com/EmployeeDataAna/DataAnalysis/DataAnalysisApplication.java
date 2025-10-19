package com.EmployeeDataAna.DataAnalysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages= {"com.EmployeeDataAna.Controller","com.EmployeeDataAna.Services"})
public class DataAnalysisApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataAnalysisApplication.class, args);
	}
}