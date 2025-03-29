package io.github.sicozz.fraudsim.generator.distribution;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.RandomGenerator;

/**
 * Implementation of a normal (Gaussian) distribution. Useful for modeling transaction amounts, time
 * intervals, etc.
 */
public class NormalNumericDistribution extends AbstractNumericDistribution {

  private final NormalDistribution distribution;
  private final double mean;
  private final double standardDeviation;
  private final double minimum;
  private final double maximum;

  /**
   * Creates a new normal distribution with default random generator
   *
   * @param mean The mean (average) value
   * @param standardDeviation The standard deviation
   * @param minimum The minimum allowed value (truncates the distribution)
   * @param maximum The maximum allowed value (truncates the distribution)
   */
  public NormalNumericDistribution(
      double mean, double standardDeviation, double minimum, double maximum) {
    super("Normal");
    this.mean = mean;
    this.standardDeviation = standardDeviation;
    this.minimum = minimum;
    this.maximum = maximum;
    this.distribution = new NormalDistribution(random, mean, standardDeviation);
  }

  /**
   * Creates a new normal distribution with custom random generator
   *
   * @param mean The mean (average) value
   * @param standardDeviation The standard deviation
   * @param minimum The minimum allowed value (truncates the distribution)
   * @param maximum The maximum allowed value (truncates the distribution)
   * @param random The random number generator to use
   */
  public NormalNumericDistribution(
      double mean,
      double standardDeviation,
      double minimum,
      double maximum,
      RandomGenerator random) {
    super("Normal");
    this.mean = mean;
    this.standardDeviation = standardDeviation;
    this.minimum = minimum;
    this.maximum = maximum;
    this.distribution = new NormalDistribution(random, mean, standardDeviation);
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
    return standardDeviation;
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
    return String.format(
        "mean=%.2f, stdDev=%.2f, min=%.2f, max=%.2f", mean, standardDeviation, minimum, maximum);
  }
}
