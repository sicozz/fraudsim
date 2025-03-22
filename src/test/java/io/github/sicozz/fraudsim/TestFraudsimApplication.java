package io.github.sicozz.fraudsim;

import org.springframework.boot.SpringApplication;

public class TestFraudsimApplication {

	public static void main(String[] args) {
		SpringApplication.from(TransactionGeneratorApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
