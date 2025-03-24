package io.github.sicozz.fraudsim.domain.model.fraud;

public record RiskScore(float score) implements FraudIndicator {
  @Override
  public String getType() {
    return "Risk Score";
  }

  @Override
  public FraudSeverity getSeverity() {
    return FraudSeverity.MEDIUM;
  }

  @Override
  public String getDescription() {
    return "Risk Score";
  }
}
