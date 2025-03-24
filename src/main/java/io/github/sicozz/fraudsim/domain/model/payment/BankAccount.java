package io.github.sicozz.fraudsim.domain.model.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record BankAccount(
    @NotBlank String accountNumber,
    @NotBlank String routingNumber,
    @NotBlank String accountHolderName,
    @NotBlank String bankName,
    @Pattern(regexp = "^(CHECKING|SAVINGS|MONEY_MARKET|CERTIFICATE_OF_DEPOSIT)$")
        String accountType,
    String maskedAccountNumber,
    String country)
    implements PaymentMethod {

  /* Compact constructor for validation and defaults. */
  public BankAccount {
    // Generate masked account number if not provided
    if (maskedAccountNumber == null || maskedAccountNumber.isBlank()) {
      maskedAccountNumber = maskAccountNumber(accountNumber);
    }

    // Default country to US if not provided
    if (country == null || country.isBlank()) {
      country = "US";
    }

    // Validate account type if not provided
    if (accountType == null || accountType.isBlank()) {
      accountType = "CHECKING";
    }
  }

  @Override
  public String getMethodType() {
    return "BANK_ACCOUNT";
  }

  @Override
  public String getMaskedIdentifier() {
    return maskedAccountNumber;
  }

  @Override
  public String getAccountHolderName() {
    return accountHolderName;
  }

  @Override
  public boolean supportsRecurring() {
    return true;
  }

  @Override
  public boolean supportsRefunds() {
    return true;
  }

  public boolean isChecking() {
    return "CHECKING".equals(accountType);
  }

  public boolean isSavings() {
    return "SAVINGS".equals(accountType);
  }

  /* Creates a masked version of the account number. */
  private static String maskAccountNumber(String accountNumber) {
    if (accountNumber == null || accountNumber.length() <= 4) {
      return accountNumber;
    }

    int lastFourStart = accountNumber.length() - 4;
    String lastFour = accountNumber.substring(lastFourStart);
    return "X".repeat(lastFourStart) + lastFour;
  }

  /* Factory method for creating a checking account. */
  public static BankAccount checking(
      String accountNumber, String routingNumber, String accountHolderName, String bankName) {
    return new BankAccount(
        accountNumber, routingNumber, accountHolderName, bankName, "CHECKING", null, "US");
  }

  /* Factory method for creating a savings account. */
  public static BankAccount savings(
      String accountNumber, String routingNumber, String accountHolderName, String bankName) {
    return new BankAccount(
        accountNumber, routingNumber, accountHolderName, bankName, "SAVINGS", null, "US");
  }
}
