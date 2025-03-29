package io.github.sicozz.fraudsim.generator.distribution;

import org.apache.commons.math3.distribution.ParetoDistribution;

/**
 * Implementation of a Pareto distribution. Useful for modeling quantities where the "80-20 rule"
 * applies, such as high-value transactions or customer spending patterns.
 */
public class ParetoNumericDistribution extends AbstractNumericDistribution {

  private final ParetoDistribution distribution;
  private final double scale;
  private final double shape;
  private final double minimum;
  private final double maximum;

  /**
   * Creates a new Pareto distribution.
   *
   * @param scale The scale parameter (minimum value)
   * @param shape The shape parameter (tail index)
   * @param maximum The maximum allowed value
   */
  public ParetoNumericDistribution(double scale, double shape, double maximum) {
    super("Pareto");
    this.scale = scale;
    this.shape = shape;
    this.minimum = scale; // In Pareto, minimum = scale
    this.maximum = maximum;
    this.distribution = new ParetoDistribution(random, scale, shape);
  }

  /**
   * Creates a new Pareto distribution with unbounded maximum.
   *
   * @param scale The scale parameter (minimum value)
   * @param shape The shape parameter (tail index)
   */
  public ParetoNumericDistribution(double scale, double shape) {
    this(scale, shape, Double.POSITIVE_INFINITY);
  }

  @Override
  public Double sample() {
    double value;
    do {
      value = distribution.sample();
    } while (value > maximum);
    // TODO: This loop condition seems wrong, verify its working

    return value;
  }

  @Override
  public double getMean() {
    if (shape <= 1) {
      return Double.POSITIVE_INFINITY; // Undefined mean for shape <= 1
    }
    return (shape * scale) / (shape - 1);
  }

  @Override
  public double getStandardDeviation() {
    if (shape <= 2) {
      return Double.POSITIVE_INFINITY; // Undefined variance for shape <= 2
    }
    double variance = (scale * scale * shape) / (Math.pow(shape - 1, 2) * (shape - 2));
    return Math.sqrt(variance);
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
        "scale=%.2f, shape=%.2f, min=%.2f, max=%.2f", scale, shape, minimum, maximum);
  }
}
