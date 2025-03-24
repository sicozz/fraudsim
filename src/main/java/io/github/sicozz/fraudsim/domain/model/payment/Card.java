package io.github.sicozz.fraudsim.domain.model.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public record Card(
    @NotBlank String maskedNumber,
    @NotNull CardType type,
    @NotBlank String network,
    @NotBlank String cardholderName,
    @Pattern(regexp = "^(0[1-9]|1[0-2])/([0-9]{2})$") String expiryDate,
    @NotNull String billingPostalCode)
    implements PaymentMethod {
  public Card(
      String maskedNumber,
      CardType type,
      String network,
      String cardholderName,
      String expiryDate,
      String billingPostalCode) {
    // Remove any spaces from the masked number
    this.maskedNumber = maskedNumber.replaceAll("\\s+", "");
    this.type = type;
    this.network = network;
    this.cardholderName = cardholderName;
    this.expiryDate = expiryDate;
    this.billingPostalCode = billingPostalCode;
  }

  @Override
  public String getMethodType() {
    return "CARD";
  }

  @Override
  public String getMaskedIdentifier() {
    return this.maskedNumber;
  }

  @Override
  public String getAccountHolderName() {
    return this.getAccountHolderName();
  }

  @Override
  public boolean supportsRecurring() {
    return type().supportsRecurring();
  }

  @Override
  public boolean supportsRefunds() {
    return type().supportsRefunds();
  }

  public String getLastFourDigits() {
    if (maskedNumber.length() < 4) {
      return maskedNumber;
    }
    return maskedNumber.substring(maskedNumber.length() - 4);
  }

  public boolean isExpired() {
    try {
      YearMonth expiryYearMonth = YearMonth.parse(expiryDate, DateTimeFormatter.ofPattern("MM/yy"));
      return YearMonth.now().isAfter(expiryYearMonth);
    } catch (DateTimeParseException e) {
      // If the date can't be parsed, consider it expired for safety
      return true;
    }
  }
}
