package io.github.sicozz.fraudsim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ConfigurationPropertiesScan("io.github.sicozz.fraudsim.config.properties")
@EnableAsync
public class TransactionGeneratorApplication {

  public static void main(String[] args) {
    SpringApplication.run(TransactionGeneratorApplication.class, args);
  }
}
