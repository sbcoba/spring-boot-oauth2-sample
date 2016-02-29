package com.example;

import org.h2.server.web.DbStarter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class H2Application {

	@Bean
	public DbStarter dbStarter() {
		return new DbStarter();
	}

	@Bean
	public ServletContextInitializer initializer() {
		return sc -> {
			sc.setInitParameter("db.user", "sa");
			sc.setInitParameter("db.password", "");
			sc.setInitParameter("db.tcpServer", "-tcpAllowOthers");
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(H2Application.class, args);
	}
}