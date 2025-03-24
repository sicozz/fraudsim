package io.github.sicozz.fraudsim.domain.model.money;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money(@NotNull @PositiveOrZero BigDecimal amount, @NotNull Currency currency) {
  public Money {
    // Ensure amount has the correct scale for the currency
    amount = amount.setScale(currency.getDefaultFractionDigits(), RoundingMode.HALF_UP);
  }

  public static Money of(double amount, Currency currency) {
    return new Money(BigDecimal.valueOf(amount), currency);
  }

  public static Money of(String amount, Currency currency) {
    return new Money(new BigDecimal(amount), currency);
  }

  public Money withAmount(BigDecimal newAmount) {
    return new Money(newAmount, this.currency);
  }

  public Money add(Money other) {
    if (!this.currency.equals(other.currency)) {
      throw new IllegalArgumentException("Cannot subtract different currencies");
    }
    return new Money(this.amount.add(other.amount), this.currency);
  }

  public String formatted() {
    return currency.getSymbol() + amount.toString();
  }
}
