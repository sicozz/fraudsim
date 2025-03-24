package io.github.sicozz.fraudsim.domain.model.party;

import java.util.UUID;

public record Customer(UUID id, String name, String email) implements TransactionParty {
  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }
}
