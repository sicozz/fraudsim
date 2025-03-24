package io.github.sicozz.fraudsim.domain.model.type;

import jakarta.validation.constraints.NotBlank;

public record TransferTransaction(
    @NotBlank String transferMethod,
    boolean isInternational,
    String purposeCode,
    String referenceMessage,
    boolean isRecurring,
    boolean isScheduled,
    String correspondingBankCode)
    implements TransactionType {

  /** Compact constructor for validation and defaults. */
  public TransferTransaction {
    // Default values if not provided
    if (purposeCode == null) {
      purposeCode = "OTHER";
    }

    if (referenceMessage == null) {
      referenceMessage = "";
    }

    if (correspondingBankCode == null) {
      correspondingBankCode = "";
    }
  }

  @Override
  public String getTypeCode() {
    return "TRANSFER";
  }

  @Override
  public String getDisplayName() {
    if (isInternational) {
      return "International " + transferMethod;
    } else {
      return transferMethod;
    }
  }

  /** Returns whether this is a wire transfer. */
  public boolean isWireTransfer() {
    return "WIRE".equals(transferMethod);
  }

  /** Returns whether this is an ACH transfer. */
  public boolean isAchTransfer() {
    return "ACH".equals(transferMethod);
  }

  /** Returns whether this is a SEPA transfer (Single Euro Payments Area). */
  public boolean isSepaTransfer() {
    return "SEPA".equals(transferMethod);
  }

  /** Factory method for creating a domestic ACH transfer. */
  public static TransferTransaction ach(String referenceMessage) {
    return new TransferTransaction("ACH", false, "PAYMENT", referenceMessage, false, false, "");
  }

  /** Factory method for creating a wire transfer. */
  public static TransferTransaction wire(boolean isInternational, String purposeCode) {
    return new TransferTransaction("WIRE", isInternational, purposeCode, "", false, false, "");
  }

  /** Factory method for creating a SEPA transfer (European payments). */
  public static TransferTransaction sepa(String referenceMessage) {
    return new TransferTransaction("SEPA", false, "PAYMENT", referenceMessage, false, false, "");
  }

  /** Factory method for creating a recurring ACH transfer. */
  public static TransferTransaction recurringAch(String referenceMessage) {
    return new TransferTransaction(
        "ACH", false, "RECURRING_PAYMENT", referenceMessage, true, true, "");
  }

  /** Factory method for creating an internal bank transfer. */
  public static TransferTransaction internalTransfer(String referenceMessage) {
    return new TransferTransaction(
        "INTERNAL", false, "TRANSFER", referenceMessage, false, false, "");
  }
}
