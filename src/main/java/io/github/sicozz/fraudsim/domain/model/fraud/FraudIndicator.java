package io.github.sicozz.fraudsim.domain.model.fraud;

public interface FraudIndicator {
  String getType();

  FraudSeverity getSeverity();

  String getDescription();

  enum FraudSeverity {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
  }
}
