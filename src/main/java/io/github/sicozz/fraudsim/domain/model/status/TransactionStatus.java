package io.github.sicozz.fraudsim.domain.model.status;

public enum TransactionStatus {
  INITIATED("Transaction has been initiated"),
  PENDING("Transaction is pending processing"),
  AUTHORIZED("Transaction has been authorized"),
  COMPLETED("Transaction has been completed successfully"),
  DECLINED("Transaction has been declined"),
  FAILED("Transaction processing has failed"),
  REVERSED("Transaction has been reversed"),
  REFUNDED("Transaction has been refunded");

  private final String description;

  TransactionStatus(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  /* Returns whether the status represents a final transaction state. */
  public boolean isFinal() {
    return switch (this) {
      case COMPLETED, DECLINED, FAILED, REVERSED, REFUNDED -> true;
      case INITIATED, PENDING, AUTHORIZED -> false;
    };
  }
}
