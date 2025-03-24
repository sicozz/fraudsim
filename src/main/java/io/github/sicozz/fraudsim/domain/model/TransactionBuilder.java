package io.github.sicozz.fraudsim.domain.model;

import io.github.sicozz.fraudsim.domain.model.fraud.FraudIndicator;
import io.github.sicozz.fraudsim.domain.model.money.Money;
import io.github.sicozz.fraudsim.domain.model.party.TransactionParty;
import io.github.sicozz.fraudsim.domain.model.payment.PaymentMethod;
import io.github.sicozz.fraudsim.domain.model.status.TransactionStatus;
import io.github.sicozz.fraudsim.domain.model.type.TransactionType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Builder for creating Transaction instances. Provides a fluent API for constructing complex
 * transactions.
 */
public class TransactionBuilder {
  private UUID id = UUID.randomUUID();
  private String referenceId;
  private Instant timestamp = Instant.now();
  private Money amount;
  private TransactionStatus status = TransactionStatus.PENDING;
  private TransactionType type;
  private TransactionParty source;
  private TransactionParty destination;
  private PaymentMethod paymentMethod;
  private TransactionMetadata metadata;
  private final List<FraudIndicator> fraudIndicators = new ArrayList<>();

  public TransactionBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public TransactionBuilder withReferenceId(String referenceId) {
    this.referenceId = referenceId;
    return this;
  }

  public TransactionBuilder withTimestamp(Instant timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  public TransactionBuilder withAmount(Money amount) {
    this.amount = amount;
    return this;
  }

  public TransactionBuilder withStatus(TransactionStatus status) {
    this.status = status;
    return this;
  }

  public TransactionBuilder withType(TransactionType type) {
    this.type = type;
    return this;
  }

  public TransactionBuilder withSource(TransactionParty source) {
    this.source = source;
    return this;
  }

  public TransactionBuilder withDestination(TransactionParty destination) {
    this.destination = destination;
    return this;
  }

  public TransactionBuilder withPaymentMethod(PaymentMethod paymentMethod) {
    this.paymentMethod = paymentMethod;
    return this;
  }

  public TransactionBuilder withMetadata(TransactionMetadata metadata) {
    this.metadata = metadata;
    return this;
  }

  public TransactionBuilder addFraudIndicator(FraudIndicator indicator) {
    this.fraudIndicators.add(indicator);
    return this;
  }

  public Transaction build() {
    if (referenceId == null) {
      referenceId = "TX-" + id.toString().substring(0, 8).toUpperCase(Locale.ROOT);
    }

    return new Transaction(
        id,
        referenceId,
        timestamp,
        amount,
        status,
        type,
        source,
        destination,
        paymentMethod,
        metadata,
        fraudIndicators);
  }

  public static TransactionBuilder builder() {
    return new TransactionBuilder();
  }
}
