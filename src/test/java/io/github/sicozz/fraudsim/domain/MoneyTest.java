package io.github.sicozz.fraudsim.domain;

import static org.junit.jupiter.api.Assertions.*;

import io.github.sicozz.fraudsim.domain.model.money.Currency;
import io.github.sicozz.fraudsim.domain.model.money.Money;
import java.math.BigDecimal;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Money Value Object Tests")
class MoneyTest {

  @Test
  @DisplayName("Should create money with correct scale for currency")
  void shouldCreateMoneyWithCorrectScaleForCurrency() {
    // When
    Money usd = Money.of(100.1234, Currency.USD);
    Money jpy = Money.of(100.1234, Currency.JPY);
    Money btc = Money.of(0.12345678, Currency.BTC);

    // Then
    assertEquals(2, usd.amount().scale());
    assertEquals(new BigDecimal("100.12"), usd.amount());

    assertEquals(0, jpy.amount().scale());
    assertEquals(new BigDecimal("100"), jpy.amount());

    assertEquals(8, btc.amount().scale());
    assertEquals(new BigDecimal("0.12345678"), btc.amount());
  }

  @Test
  @DisplayName("Should create money from string values")
  void shouldCreateMoneyFromStringValues() {
    // When
    Money usd = Money.of("123.45", Currency.USD);

    // Then
    assertEquals(new BigDecimal("123.45"), usd.amount());
    assertEquals(Currency.USD, usd.currency());
  }

  @Test
  @DisplayName("Should add money values correctly")
  void shouldAddMoneyValuesCorrectly() {
    // Given
    Money usd1 = Money.of(100.25, Currency.USD);
    Money usd2 = Money.of(200.50, Currency.USD);

    // When
    Money sum = usd1.add(usd2);

    // Then
    assertEquals(Currency.USD, sum.currency());
    assertEquals(new BigDecimal("300.75"), sum.amount());
  }

  @Test
  @DisplayName("Should subtract money values correctly")
  void shouldSubtractMoneyValuesCorrectly() {
    // Given
    Money usd1 = Money.of(200.75, Currency.USD);
    Money usd2 = Money.of(100.25, Currency.USD);

    // When
    Money difference = usd1.subtract(usd2);

    // Then
    assertEquals(Currency.USD, difference.currency());
    assertEquals(new BigDecimal("100.50"), difference.amount());
  }

  @Test
  @DisplayName("Should throw exception when adding different currencies")
  void shouldThrowExceptionWhenAddingDifferentCurrencies() {
    // Given
    Money usd = Money.of(100, Currency.USD);
    Money eur = Money.of(100, Currency.EUR);

    // Then
    assertThrows(IllegalArgumentException.class, () -> usd.add(eur));
  }

  @Test
  @DisplayName("Should throw exception when subtracting different currencies")
  void shouldThrowExceptionWhenSubtractingDifferentCurrencies() {
    // Given
    Money usd = Money.of(100, Currency.USD);
    Money eur = Money.of(100, Currency.EUR);

    // Then
    assertThrows(IllegalArgumentException.class, () -> usd.subtract(eur));
  }

  @Test
  @DisplayName("Should format money value correctly")
  void shouldFormatMoneyValueCorrectly() {
    // Given
    Money usd = Money.of(1234.56, Currency.USD);
    Money eur = Money.of(1234.56, Currency.EUR);
    Money gbp = Money.of(1234.56, Currency.GBP);

    // Then
    assertTrue(usd.formatted().contains("$"));
    assertTrue(usd.formatted().contains("1234.56"));

    assertTrue(eur.formatted().contains("€"));
    assertTrue(eur.formatted().contains("1234.56"));

    assertTrue(gbp.formatted().contains("£"));
    assertTrue(gbp.formatted().contains("1234.56"));
  }

  @ParameterizedTest
  @MethodSource("provideCurrenciesWithScales")
  @DisplayName("Should honor currency's fraction digits")
  void shouldHonorCurrencyFractionDigits(Currency currency, int expectedScale) {
    // Given
    Money money = Money.of(100.123456789, currency);

    // Then
    assertEquals(expectedScale, money.amount().scale());
  }

  private static Stream<Arguments> provideCurrenciesWithScales() {
    return Stream.of(
        Arguments.of(Currency.USD, 2),
        Arguments.of(Currency.EUR, 2),
        Arguments.of(Currency.JPY, 0),
        Arguments.of(Currency.BTC, 8));
  }
}
