package io.github.sicozz.fraudsim.domain.model.type;

public sealed interface TransactionType permits CardTransaction, TransferTransaction {
  String getTypeCode();

  String getDisplayName();
}
