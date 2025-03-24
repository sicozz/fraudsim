package io.github.sicozz.fraudsim.domain.model.party;

import java.util.UUID;

public record Merchant(UUID id, String name, String mcc // Merchant Category code
    ) implements TransactionParty {
  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }
}
