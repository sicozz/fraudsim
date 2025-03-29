package io.github.sicozz.fraudsim.generator.distribution;

import org.apache.commons.math3.distribution.LogNormalDistribution;

/**
 * Implementation of a log-normal distribution. Particularly useful for modeling transaction amounts
 * where values are skewed.
 */
public class LogNormalNumericDistribution extends AbstractNumericDistribution {
  private final LogNormalDistribution distribution;
  private final double scale;
  private final double shape;
  private final double calculatedMean;
  private final double calculatedStdDev;
  private final double minimum;
  private final double maximum;

  public LogNormalNumericDistribution(double scale, double shape, double minimum, double maximum) {
    super("LogNormal");
    this.scale = scale;
    this.shape = shape;
    this.minimum = minimum;
    this.maximum = maximum;
    this.distribution = new LogNormalDistribution(random, scale, shape);

    // Calculate the actual mean and standard deviation
    this.calculatedMean = Math.exp(scale + (shape * shape) / 2);
    this.calculatedStdDev =
        Math.sqrt((Math.exp(shape * shape) - 1) * Math.exp(2 * scale + shape * shape));
  }

  /**
   * Creates a new log-normal distribution with unbounded min/max.
   *
   * @param scale The scale parameter (mu)
   * @param shape The shape parameter (sigma)
   */
  public LogNormalNumericDistribution(double scale, double shape) {
    this(scale, shape, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
  }

  /**
   * Creates a new log-normal distribution from desired mean and standard deviation.
   *
   * @param mean The desired mean
   * @param stdDev The desired standard deviation
   * @param minimum The minimum allowed value
   * @param maximum The maximum allowed value
   * @return A log-normal distribution with parameters calculated to match the desired mean and
   *     standard deviation
   */
  public static LogNormalNumericDistribution fromMeanAndStdDev(
      double mean, double stdDev, double minimum, double maximum) {
    double variance = stdDev * stdDev;
    double shape = Math.sqrt(Math.log(variance / (mean * mean) + 1));
    double scale = Math.log(mean) - shape * shape / 2;

    return new LogNormalNumericDistribution(scale, shape, minimum, maximum);
  }

  @Override
  public Double sample() {
    double value = distribution.sample();
    do {
      value = distribution.sample();
    } while (value < minimum || value > maximum);
    // TODO: This loop condition seems wrong, verify its working

    return value;
  }

  @Override
  public double getMean() {
    return calculatedMean;
  }

  @Override
  public double getStandardDeviation() {
    return calculatedStdDev;
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
        "scale=%.2f, shape=%.2f, mean=%.2f, stdDev=%.2f, min=%.2f, max=%.2f",
        scale, shape, calculatedMean, calculatedStdDev, minimum, maximum);
  }
}
