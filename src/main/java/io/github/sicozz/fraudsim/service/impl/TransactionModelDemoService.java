package io.github.sicozz.fraudsim.service.impl;

import io.github.sicozz.fraudsim.domain.model.Transaction;
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
import org.springframework.stereotype.Service;

@Service
public class TransactionModelDemoService {
  public Transaction createSimpleTransaction() {
    return TransactionBuilder.builder()
        .withReferenceId("TX-DEMO-123")
        .withAmount(Money.of(123.45, Currency.USD))
        .withType(CardTransaction.ecommerce("VISA"))
        .withSource(new Customer(UUID.randomUUID(), "John Carmack", "johncarmack@id.com"))
        .withDestination(new Merchant(UUID.randomUUID(), "Example Online Store", "5999"))
        .withPaymentMethod(
            new Card("4111XXXXXXXX1111", CardType.CREDIT, "VISA", "John Doe", "12/25", ""))
        .withStatus(TransactionStatus.COMPLETED)
        .withMetadata(TransactionMetadata.of("channel", "web"))
        .build();
  }
}
