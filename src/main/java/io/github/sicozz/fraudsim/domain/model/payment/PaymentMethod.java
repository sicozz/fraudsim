package io.github.sicozz.fraudsim.domain.model.payment;

public sealed interface PaymentMethod permits Card, BankAccount {
  String getMethodType();

  /* Returns a masked or safe representation of the payment identifier. */
  String getMaskedIdentifier();

  String getAccountHolderName();

  /* Returns whether this payment method supports recurring payments. */
  boolean supportsRecurring();

  boolean supportsRefunds();
}
