package io.github.sicozz.fraudsim.domain.model;

import java.util.HashMap;
import java.util.Map;

public record TransactionMetadata(Map<String, String> values) {
  public TransactionMetadata {
    // Defensive copying
    values = values == null ? Map.of() : Map.copyOf(values);
  }

  public static TransactionMetadata of(String key, String value) {
    return new TransactionMetadata(Map.of(key, value));
  }

  public static TransactionMetadata empty() {
    return new TransactionMetadata(Map.of());
  }

  public TransactionMetadata with(String key, String value) {
    Map<String, String> newValues = new HashMap<>(values);
    newValues.put(key, value);
    return new TransactionMetadata(newValues);
  }

  public TransactionMetadata without(String key) {
    Map<String, String> newValues = new HashMap<>(values);
    newValues.remove(key);
    return new TransactionMetadata(newValues);
  }
}
