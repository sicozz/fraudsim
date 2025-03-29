package io.github.sicozz.fraudsim.generator.distribution;

import io.github.sicozz.fraudsim.domain.model.money.Currency;
import io.github.sicozz.fraudsim.domain.model.payment.CardType;
import io.github.sicozz.fraudsim.domain.model.type.CardTransaction;
import io.github.sicozz.fraudsim.domain.model.type.TransactionType;
import io.github.sicozz.fraudsim.domain.model.type.TransferTransaction;
import java.time.LocalDate;
import java.util.Map;

/* Factory class for creating common distributions used in transaction generation. */
public class DistributionFactory {

  /**
   * Creates a default transaction amount distribution for the given currency.
   *
   * @param currency The currency to use
   * @return A transaction amount distribution
   */
  public static TransactionAmountDistribution createAmountDistribution(Currency currency) {
    return new TransactionAmountDistribution(currency);
  }

  /**
   * Creates a distribution for transaction types.
   *
   * @return A discrete distribution of transaction types
   */
  public static DiscreteDistribution<TransactionType> createTransactionTypeDistribution() {
    // Default distribution: 80% card transactions, 20% transfers
    Map<TransactionType, Double> typeWeights =
        Map.of(
            CardTransaction.ecommerce("VISA"), 0.3,
            CardTransaction.contactless("VISA"), 0.3,
            CardTransaction.standard("VISA"), 0.2,
            TransferTransaction.ach(""), 0.15,
            TransferTransaction.wire(false, "PAYMENT"), 0.05);

    return new DiscreteDistribution<>("TransactionType", typeWeights);
  }

  /**
   * Creates a distribution for card networks.
   *
   * @return A discrete distribution of card networks
   */
  public static DiscreteDistribution<String> createCardNetworkDistribution() {
    // Default distribution based on market share
    Map<String, Double> networkWeights =
        Map.of(
            "VISA", 0.35,
            "MASTERCARD", 0.30,
            "AMEX", 0.15,
            "DISCOVER", 0.10,
            "JCB", 0.05,
            "UNIONPAY", 0.05);

    return new DiscreteDistribution<>("CardNetwork", networkWeights);
  }

  /**
   * Creates a distribution for card types.
   *
   * @return A discrete distribution of card types
   */
  public static DiscreteDistribution<CardType> createCardTypeDistribution() {
    // Default distribution based on typical usage
    Map<CardType, Double> typeWeights =
        Map.of(
            CardType.CREDIT, 0.50,
            CardType.DEBIT, 0.40,
            CardType.PREPAID, 0.08,
            CardType.GIFT, 0.02);

    return new DiscreteDistribution<>("CardType", typeWeights);
  }

  /**
   * Creates a transaction time distribution for the given date range.
   *
   * @param startDate The start date (inclusive)
   * @param endDate The end date (inclusive)
   * @return A transaction time distribution
   */
  public static TransactionTimeDistribution createTimeDistribution(
      LocalDate startDate, LocalDate endDate) {
    return new TransactionTimeDistribution(startDate, endDate);
  }

  /**
   * Creates a log-normal distribution with the given parameters.
   *
   * @param mean The desired mean
   * @param stdDev The desired standard deviation
   * @param min The minimum value
   * @param max The maximum value
   * @return A log-normal distribution
   */
  public static NumericDistribution createLogNormalNumericDistribution(
      double mean, double stdDev, double min, double max) {
    return LogNormalNumericDistribution.fromMeanAndStdDev(mean, stdDev, min, max);
  }

  /**
   * Creates a normal (Gaussian) distribution with the given parameters.
   *
   * @param mean The mean value
   * @param stdDev The standard deviation
   * @param min The minimum value
   * @param max The maximum value
   * @return A normal distribution
   */
  public static NumericDistribution createNormalDistribution(
      double mean, double stdDev, double min, double max) {
    return new NormalNumericDistribution(mean, stdDev, min, max);
  }

  /**
   * Creates an exponential distribution with the given parameters.
   *
   * @param mean The mean value
   * @param min The minimum value
   * @return An exponential distribution
   */
  public static NumericDistribution createExponentialDistribution(double mean, double min) {
    return new ExponentialNumericDistribution(mean, min);
  }

  /**
   * Creates a Pareto distribution with the given parameters.
   *
   * @param min The minimum value (scale parameter)
   * @param shape The shape parameter
   * @param max The maximum value
   * @return A Pareto distribution
   */
  public static NumericDistribution createParetoDistribution(double min, double shape, double max) {
    return new ParetoNumericDistribution(min, shape, max);
  }
}
