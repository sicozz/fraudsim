package io.github.sicozz.fraudsim.domain.model;

import io.github.sicozz.fraudsim.domain.model.fraud.FraudIndicator;
import io.github.sicozz.fraudsim.domain.model.money.Money;
import io.github.sicozz.fraudsim.domain.model.party.TransactionParty;
import io.github.sicozz.fraudsim.domain.model.payment.PaymentMethod;
import io.github.sicozz.fraudsim.domain.model.status.TransactionStatus;
import io.github.sicozz.fraudsim.domain.model.type.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Immutable representation of a financial transaction. */
public record Transaction(
    @NotNull UUID id,
    @NotNull String referenceId,
    @NotNull @PastOrPresent Instant timestamp,
    @NotNull Money amount,
    @NotNull TransactionStatus status,
    @NotNull TransactionType type,
    @NotNull TransactionParty source,
    @NotNull TransactionParty destination,
    @NotNull PaymentMethod paymentMethod,
    TransactionMetadata metadata,
    List<FraudIndicator> fraudIndicators) {

  public Transaction {
    // Defensive copying
    fraudIndicators = fraudIndicators == null ? List.of() : List.copyOf(fraudIndicators);
  }

  public static Transaction create(
      String referenceId,
      Instant timestamp,
      Money amount,
      TransactionStatus status,
      TransactionType type,
      TransactionParty source,
      TransactionParty destination,
      PaymentMethod paymentMethod,
      TransactionMetadata metadata,
      List<FraudIndicator> fraudIndicators) {
    return new Transaction(
        UUID.randomUUID(),
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

  public Transaction withStatus(TransactionStatus newStatus) {
    return new Transaction(
        id,
        referenceId,
        timestamp,
        amount,
        newStatus,
        type,
        source,
        destination,
        paymentMethod,
        metadata,
        fraudIndicators);
  }

  public Transaction withFraudIndicator(FraudIndicator indicator) {
    var newIndicators = fraudIndicators;
    newIndicators.add(indicator);
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
        newIndicators);
  }

  public Optional<String> getMetadataValue(String key) {
    return Optional.ofNullable(metadata).flatMap(m -> Optional.ofNullable(m.values().get(key)));
  }

  public boolean hasFraudIndicators() {
    return fraudIndicators.isEmpty();
  }
}
