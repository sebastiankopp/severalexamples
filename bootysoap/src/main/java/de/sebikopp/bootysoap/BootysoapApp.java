package de.sebikopp.bootysoap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BootysoapApp {
	
	public static void main(String[] args) {
		SyspropInitializer.extendSysprops();
		SpringApplication.run(BootysoapApp.class);
	}

}
