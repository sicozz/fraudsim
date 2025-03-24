package io.github.sicozz.fraudsim.domain.model.payment;

public enum CardType {
  CREDIT("Credit Card"),
  DEBIT("Debit Card"),
  PREPAID("Prepaid Card"),
  GIFT("Gift Card"),
  FLEET("Fleet Card"),
  HSA("Health Savings Account Card"),
  EBT("Electronic Benefit Transfer Card");

  private final String displayName;

  CardType(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }

  /* Return whether this card type is typically linked to a bank account */
  public boolean isLinkedToBankAccount() {
    return this == DEBIT || this == CREDIT;
  }

  /* Returns whether this card type can be used for recurring payments. */
  public boolean supportsRecurring() {
    return this == DEBIT || this == CREDIT;
  }

  /* Returns whether this card type can be used for refunds */
  public boolean supportsRefunds() {
    return this == DEBIT || this == CREDIT;
  }

  /* Returns whether this card type has credit functionality */
  public boolean hasCreditFunctionality() {
    return this == CREDIT;
  }
}
