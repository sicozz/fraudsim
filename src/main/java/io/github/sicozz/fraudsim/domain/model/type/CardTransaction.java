package io.github.sicozz.fraudsim.domain.model.type;

public record CardTransaction(
    String network, boolean isContactless, boolean isEcommerce, boolean isInternational)
    implements TransactionType {

  @Override
  public String getTypeCode() {
    return "CARD";
  }

  @Override
  public String getDisplayName() {
    if (isEcommerce) {
      return "Card Online Payment";
    } else if (isContactless) {
      return "Card Contactless Payment";
    } else {
      return "Card Payment";
    }
  }

  /* Factory method for creating a standard card transaction. */
  public static CardTransaction standard(String network) {
    return new CardTransaction(network, false, false, false);
  }

  /* Factory method for creating an online card transaction. */
  public static CardTransaction ecommerce(String network) {
    return new CardTransaction(network, false, true, false);
  }

  /* Factory method for creating a contactless card transaction. */
  public static CardTransaction contactless(String network) {
    return new CardTransaction(network, true, false, false);
  }
}
