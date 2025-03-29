package io.github.sicozz.fraudsim.generator.distribution;

import org.apache.commons.math3.distribution.ExponentialDistribution;

/**
 * Implementation of an exponential distribution. Useful for modeling time intervals between events,
 * such as the time between consecutive transactions.
 */
public class ExponentialNumericDistribution extends AbstractNumericDistribution {

  private final ExponentialDistribution distribution;
  private final double mean;
  private final double minimum;
  private final double maximum;

  /**
   * Creates a new exponential distribution.
   *
   * @param mean The mean value
   * @param minimum The minimum allowed value
   * @param maximum The maximum allowed value
   */
  public ExponentialNumericDistribution(double mean, double minimum, double maximum) {
    super("Exponential");
    this.mean = mean;
    this.minimum = minimum;
    this.maximum = maximum;
    this.distribution = new ExponentialDistribution(random, mean);
  }

  /**
   * Creates a new exponential distribution with no upper bound.
   *
   * @param mean The mean value
   * @param minimum The minimum allowed value
   */
  public ExponentialNumericDistribution(double mean, double minimum) {
    this(mean, minimum, Double.POSITIVE_INFINITY);
  }

  /**
   * Creates a new exponential distribution with default minimum of 0.
   *
   * @param mean The mean value
   */
  public ExponentialNumericDistribution(double mean) {
    this(mean, 0, Double.POSITIVE_INFINITY);
  }

  @Override
  public Double sample() {
    double value;
    do {
      value = distribution.sample();
    } while (value < minimum || value > maximum);
    // TODO: This loop condition seems wrong, verify its working

    return value;
  }

  @Override
  public double getMean() {
    return mean;
  }

  @Override
  public double getStandardDeviation() {
    return mean; // For exponential distribution, std dev = mean
  }

  @Override
  public double getMinimum() {
    return minimum;
  }

  @Override
  public double getMaximum() {
    return maximum;
  }

  @Override
  public double getProbabilityDensity(double value) {
    return distribution.density(value);
  }

  @Override
  public String getDescription() {
    return String.format("mean=%.2f, min=%.2f, max=%.2f", mean, minimum, maximum);
  }
}
