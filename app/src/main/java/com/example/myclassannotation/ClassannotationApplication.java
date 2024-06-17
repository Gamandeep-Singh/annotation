package com.example.myclassannotation;

import com.example.myclassannotation.demo.DemoSSTLoggerAnnotation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClassannotationApplication {

	public static void main(String[] args) {

		SpringApplication.run(ClassannotationApplication.class, args);
		DemoSSTLoggerAnnotation d = new DemoSSTLoggerAnnotation();
		d.fun();
	}

}
