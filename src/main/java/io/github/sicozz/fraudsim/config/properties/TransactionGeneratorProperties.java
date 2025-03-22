package io.github.sicozz.fraudsim.config.properties;

import io.github.sicozz.fraudsim.config.properties.TransactionGeneratorProperties.Output.Fraud;
import io.github.sicozz.fraudsim.config.properties.TransactionGeneratorProperties.Output.Transaction;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "transaction-generator")
public class TransactionGeneratorProperties {

  private Output output = new Output();
  private Transaction transaction = new Transaction();
  private Fraud fraud = new Fraud();

  @Data
  public static class Output {
    private Kafka kafka = new Kafka();
    private File file = new File();

    @Data
    public static class Kafka {
      private boolean enabled = true;
      private String topic = "transactions";
    }

    @Data
    public static class File {
      private boolean enabled = false;
      private String path = "./output";
      private String format = "json";
    }

    @Data
    public static class Transaction {
      private int defaultVolume = 100;
      private int defaultTps = 10;
    }

    @Data
    public static class Fraud {
      private double defaultRate = 0.05;
      private List<String> enabledPatterns = List.of("amountSpike", "merchantAnomaly");
    }
  }
}
