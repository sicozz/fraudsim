package io.github.sicozz.fraudsim.generator.distribution;

import io.github.sicozz.fraudsim.domain.model.money.Currency;
import io.github.sicozz.fraudsim.domain.model.money.Money;
import io.github.sicozz.fraudsim.domain.model.party.Merchant;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

/**
 * Specialized distribution for generating realistic transaction amounts. Models different patterns
 * based on merchant category.
 */
public class TransactionAmountDistribution implements Distribution<Money> {

  private final RandomGenerator random;
  private final Currency currency;
  private final Map<String, NumericDistribution> merchantCategoryDistributions;
  private final NumericDistribution defaultDistribution;

  /**
   * Creates a new transaction amount distribution for the given currency.
   *
   * @param currency The currency to use for the amounts
   */
  public TransactionAmountDistribution(Currency currency) {
    // Default distribution - a log-normal distribution that models general spending
    this(
        currency,
        new Well19937c(),
        LogNormalNumericDistribution.fromMeanAndStdDev(50.0, 75.0, 1.0, 5000.0));
  }

  /**
   * Creates a new transaction amount distribution with a custom random generator and default
   * distribution
   *
   * @param currency The currency to use for the amounts
   * @param random The random number generator to use
   */
  public TransactionAmountDistribution(
      Currency currency, RandomGenerator random, NumericDistribution defaultDistribution) {
    this.random = random;
    this.currency = currency;
    this.merchantCategoryDistributions = new HashMap<>();
    this.defaultDistribution = defaultDistribution;

    // Initialize with common merchant category distributions
    initializeDefaultCategoryDistributions();
  }

  /**
   * Sets up default distributions for common merchant categories. These model typical spending
   * patterns in different categories.
   */
  private void initializeDefaultCategoryDistributions() {
    // Grocery stores - clustered around smaller amounts
    merchantCategoryDistributions.put(
        "5411", LogNormalNumericDistribution.fromMeanAndStdDev(65.0, 40.0, 5.0, 500.0));

    // Restaurants - smaller average amounts
    merchantCategoryDistributions.put(
        "5812", LogNormalNumericDistribution.fromMeanAndStdDev(35.0, 25.0, 5.0, 300.0));

    // Gas stations - fairly uniform within a range
    merchantCategoryDistributions.put(
        "5541", new NormalNumericDistribution(45.0, 15.0, 10.0, 150.0));

    // Department stores - wider range
    merchantCategoryDistributions.put(
        "5311", LogNormalNumericDistribution.fromMeanAndStdDev(85.0, 100.0, 10.0, 1000.0));

    // Electronics - higher amounts
    merchantCategoryDistributions.put(
        "5732", LogNormalNumericDistribution.fromMeanAndStdDev(250.0, 300.0, 20.0, 5000.0));

    // Utility bills - fairly consistent
    merchantCategoryDistributions.put(
        "4900", new NormalNumericDistribution(120.0, 50.0, 20.0, 500.0));

    // Travel - high amounts with high variance
    merchantCategoryDistributions.put(
        "4722", LogNormalNumericDistribution.fromMeanAndStdDev(500.0, 700.0, 50.0, 10000.0));

    // Healthcare - moderate to high amounts
    merchantCategoryDistributions.put(
        "8099", LogNormalNumericDistribution.fromMeanAndStdDev(150.0, 200.0, 20.0, 3000.0));
  }

  /**
   * Sets a custom distribution for a specific merchant category.
   *
   * @param mcc The merchant category code
   * @param distribution The distribution to use for this category
   * @return This distribution, for method chaining
   */
  public TransactionAmountDistribution setMerchantCategoryDistribution(
      String mcc, NumericDistribution distribution) {
    merchantCategoryDistributions.put(mcc, distribution);
    return this;
  }

  /**
   * Samples a transaction amount based on the merchant.
   *
   * @param merchant The merchant for the transaction
   * @return A money value representing the transaction amount
   */
  public Money sample(Merchant merchant) {
    NumericDistribution distribution =
        merchantCategoryDistributions.getOrDefault(merchant.mcc(), defaultDistribution);

    double amount = distribution.sample();

    // Apply common price patterns (e.g., $XX.99)
    if (random.nextDouble() < 0.7) { // 70% chance of price ending in .99 or .95
      amount = Math.floor(amount);
      if (random.nextDouble() < 0.8) { // 80% chance of .99 vs .95
        amount += 0.99;
      } else {
        amount += 0.95;
      }
    }

    return Money.of(amount, currency);
  }

  @Override
  public Money sample() {
    double amount = defaultDistribution.sample();

    // Apply common price patterns
    if (random.nextDouble() < 0.7) {
      amount = Math.floor(amount);
      if (random.nextDouble() < 0.8) {
        amount += 0.99;
      } else {
        amount += 0.95;
      }
    }

    return Money.of(amount, currency);
  }

  @Override
  public String getName() {
    return "TransactionAmount";
  }

  @Override
  public String getDescription() {
    return String.format(
        "Transaction amount distribution in %s with %d merchant categories",
        currency, merchantCategoryDistributions.size());
  }
}
