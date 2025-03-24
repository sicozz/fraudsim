package io.github.sicozz.fraudsim.domain;

import static org.junit.jupiter.api.Assertions.*;

import io.github.sicozz.fraudsim.domain.model.TransactionBuilder;
import io.github.sicozz.fraudsim.domain.model.TransactionMetadata;
import io.github.sicozz.fraudsim.domain.model.fraud.FraudIndicator;
import io.github.sicozz.fraudsim.domain.model.money.Currency;
import io.github.sicozz.fraudsim.domain.model.money.Money;
import io.github.sicozz.fraudsim.domain.model.party.Customer;
import io.github.sicozz.fraudsim.domain.model.party.Merchant;
import io.github.sicozz.fraudsim.domain.model.payment.Card;
import io.github.sicozz.fraudsim.domain.model.payment.CardType;
import io.github.sicozz.fraudsim.domain.model.status.TransactionStatus;
import io.github.sicozz.fraudsim.domain.model.type.CardTransaction;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Transaction Builder Tests")
public class TransactionBuilderTest {

  @Test
  @DisplayName("Should build a complete transaction with all attributes")
  void shouldBuildCompleteTransaction() {
    // Given
    var id = UUID.randomUUID();
    var referenceId = "TEST-12345";
    var timestamp = Instant.now();
    var amount = Money.of(123.45, Currency.USD);
    var status = TransactionStatus.COMPLETED;
    var type = CardTransaction.ecommerce("VISA");
    var source = new Customer(UUID.randomUUID(), "John Smith", "john@example.com");
    var destination = new Merchant(UUID.randomUUID(), "Online Shop", "5999");
    var paymentMethod =
        new Card("4111XXXXXXXX1111", CardType.CREDIT, "VISA", "John Smith", "12/25", "");
    var metadata = TransactionMetadata.of("order_id", "ORDER-123");

    // When
    var transaction =
        TransactionBuilder.builder()
            .withId(id)
            .withReferenceId(referenceId)
            .withTimestamp(timestamp)
            .withAmount(amount)
            .withStatus(status)
            .withType(type)
            .withSource(source)
            .withDestination(destination)
            .withPaymentMethod(paymentMethod)
            .withMetadata(metadata)
            .build();

    // Then
    assertEquals(id, transaction.id());
    assertEquals(referenceId, transaction.referenceId());
    assertEquals(timestamp, transaction.timestamp());
    assertEquals(amount, transaction.amount());
    assertEquals(status, transaction.status());
    assertEquals(type, transaction.type());
    assertEquals(source, transaction.source());
    assertEquals(destination, transaction.destination());
    assertEquals(paymentMethod, transaction.paymentMethod());
    assertEquals(metadata, transaction.metadata());
  }

  @Test
  @DisplayName("Should generate reference ID if not provided")
  void shouldGenerateReferenceIdIfNotProvided() {
    // When
    var transaction =
        TransactionBuilder.builder()
            .withAmount(Money.of(100, Currency.USD))
            .withType(CardTransaction.standard("VISA"))
            .withSource(new Customer(UUID.randomUUID(), "Jane Doe", "jane@example.com"))
            .withDestination(new Merchant(UUID.randomUUID(), "Store", "5999"))
            .withPaymentMethod(
                new Card("4111XXXXXXXX1111", CardType.CREDIT, "VISA", "Jane Doe", "12/25", ""))
            .build();

    // Then
    assertNotNull(transaction.referenceId());
    assertTrue(transaction.referenceId().startsWith("TX-"));
  }

  @Test
  @DisplayName("Should set default timestamp if not provided")
  void shouldSetDefaultTimestampIfNotProvided() {
    // Given
    Instant before = Instant.now();

    // When
    var transaction =
        TransactionBuilder.builder()
            .withAmount(Money.of(100, Currency.USD))
            .withType(CardTransaction.standard("VISA"))
            .withSource(new Customer(UUID.randomUUID(), "Jane Doe", "jane@example.com"))
            .withDestination(new Merchant(UUID.randomUUID(), "Store", "5999"))
            .withPaymentMethod(
                new Card("4111XXXXXXXX1111", CardType.CREDIT, "VISA", "Jane Doe", "12/25", ""))
            .build();

    Instant after = Instant.now();

    // Then
    assertNotNull(transaction.timestamp());
    assertFalse(transaction.timestamp().isBefore(before));
    assertFalse(transaction.timestamp().isAfter(after));
  }

  @Test
  @DisplayName("Should set pending status by default")
  void shouldSetPendingStatusByDefault() {
    // When
    var transaction =
        TransactionBuilder.builder()
            .withAmount(Money.of(100, Currency.USD))
            .withType(CardTransaction.standard("VISA"))
            .withSource(new Customer(UUID.randomUUID(), "Jane Doe", "jane@example.com"))
            .withDestination(new Merchant(UUID.randomUUID(), "Store", "5999"))
            .withPaymentMethod(
                new Card("4111XXXXXXXX1111", CardType.CREDIT, "VISA", "Jane Doe", "12/25", ""))
            .build();

    // Then
    assertEquals(TransactionStatus.PENDING, transaction.status());
  }

  @Test
  @DisplayName("Should add multiple fraud indicators")
  void shouldAddMultipleFraudIndicators() {
    // Given
    FraudIndicator indicator1 =
        createFraudIndicator("UNUSUAL_AMOUNT", FraudIndicator.FraudSeverity.MEDIUM);
    FraudIndicator indicator2 =
        createFraudIndicator("LOCATION_MISMATCH", FraudIndicator.FraudSeverity.HIGH);

    // When
    var transaction =
        TransactionBuilder.builder()
            .withAmount(Money.of(100, Currency.USD))
            .withType(CardTransaction.standard("VISA"))
            .withSource(new Customer(UUID.randomUUID(), "Jane Doe", "jane@example.com"))
            .withDestination(new Merchant(UUID.randomUUID(), "Store", "5999"))
            .withPaymentMethod(
                new Card("4111XXXXXXXX1111", CardType.CREDIT, "VISA", "Jane Doe", "12/25", ""))
            .addFraudIndicator(indicator1)
            .addFraudIndicator(indicator2)
            .build();

    // Then
    assertTrue(transaction.hasFraudIndicators());
    assertEquals(2, transaction.fraudIndicators().size());
    assertEquals("UNUSUAL_AMOUNT", transaction.fraudIndicators().get(0).getType());
    assertEquals("LOCATION_MISMATCH", transaction.fraudIndicators().get(1).getType());
  }

  // Helper method to create fraud indicators
  private FraudIndicator createFraudIndicator(String type, FraudIndicator.FraudSeverity severity) {
    return new FraudIndicator() {
      @Override
      public String getType() {
        return type;
      }

      @Override
      public FraudSeverity getSeverity() {
        return severity;
      }

      @Override
      public String getDescription() {
        return "Test fraud indicator";
      }
    };
  }
}
