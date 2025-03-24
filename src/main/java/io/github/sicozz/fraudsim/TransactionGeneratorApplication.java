package io.github.sicozz.fraudsim;

import io.github.sicozz.fraudsim.domain.model.Transaction;
import io.github.sicozz.fraudsim.service.impl.TransactionModelDemoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ConfigurationPropertiesScan("io.github.sicozz.fraudsim.config.properties")
@EnableAsync
public class TransactionGeneratorApplication {

  public static void main(String[] args) {
    SpringApplication.run(TransactionGeneratorApplication.class, args);
  }

  @Bean
  public CommandLineRunner demoTransactionModel(TransactionModelDemoService demoService) {
    return args -> {
      System.out.println("=== Transaction Model Demo ===");
      Transaction transaction = demoService.createSimpleTransaction();
      System.out.println("Transaction ID: " + transaction.id());
      System.out.println("Reference: " + transaction.referenceId());
      System.out.println("Amount: " + transaction.amount().formatted());
      System.out.println("From: " + transaction.source().getName());
      System.out.println("To: " + transaction.destination().getName());
      System.out.println("Status: " + transaction.status());
      System.out.println("Type: " + transaction.type().getDisplayName());
      System.out.println("==============================");
    };
  }
}
