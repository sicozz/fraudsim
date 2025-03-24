package io.github.sicozz.fraudsim.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.sicozz.fraudsim.domain.model.TransactionBuilder;
import io.github.sicozz.fraudsim.domain.model.TransactionMetadata;
import io.github.sicozz.fraudsim.domain.model.money.Currency;
import io.github.sicozz.fraudsim.domain.model.money.Money;
import io.github.sicozz.fraudsim.domain.model.party.Customer;
import io.github.sicozz.fraudsim.domain.model.party.Merchant;
import io.github.sicozz.fraudsim.domain.model.payment.Card;
import io.github.sicozz.fraudsim.domain.model.payment.CardType;
import io.github.sicozz.fraudsim.domain.model.status.TransactionStatus;
import io.github.sicozz.fraudsim.domain.model.type.CardTransaction;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Card Transaction Tests")
class CardTransactionTest {
  @Nested
  @DisplayName("Card Transaction Creation")
  class CardTransactionCreationTest {
    @Test
    @DisplayName("Should create a valid in-store card transaction")
    void shouldCreateValidInStoreCardTransaction() {
      // Given
      var customer = new Customer(UUID.randomUUID(), "Alice Smith", "alice@example.com");
      var merchant = new Merchant(UUID.randomUUID(), "Local Grocery", "5411");
      var card = new Card("4111XXXXXXXX1111", CardType.CREDIT, "VISA", "Alice Smith", "12/25", "");
      var money = Money.of(67.89, Currency.USD);
      var txType = CardTransaction.standard("VISA");

      // When
      var transaction =
          TransactionBuilder.builder()
              .withReferenceId("POS-12345")
              .withAmount(money)
              .withSource(customer)
              .withDestination(merchant)
              .withType(txType)
              .withPaymentMethod(card)
              .withStatus(TransactionStatus.COMPLETED)
              .withMetadata(TransactionMetadata.of("terminal_id", "TERM-123"))
              .build();

      // Then
      assertNotNull(transaction.id());
      assertNotNull(transaction.id());
      assertEquals("POS-12345", transaction.referenceId());
      assertEquals(money, transaction.amount());
      assertEquals(customer, transaction.source());
      assertEquals(merchant, transaction.destination());
      assertEquals(txType, transaction.type());
      assertEquals(card, transaction.paymentMethod());
      assertEquals(TransactionStatus.COMPLETED, transaction.status());
      assertTrue(transaction.getMetadataValue("terminal_id").isPresent());
      assertEquals("TERM-123", transaction.getMetadataValue("terminal_id").get());
      assertFalse(transaction.hasFraudIndicators());

      // Verify Card Transaction specific details
      assertFalse(txType.isContactless());
      assertFalse(txType.isEcommerce());
      assertEquals("VISA", txType.network());
      assertEquals("Card Payment", txType.getDisplayName());
    }
  }
}
