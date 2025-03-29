package io.github.sicozz.fraudsim.generator.distribution;

public interface NumericDistribution extends Distribution<Double> {
  double getMean();

  double getStandardDeviation();

  /**
   * Returns the minimum possible value that can be sampled. May return Double.NEGATIVE_INFINITY for
   * unbounded distributions.
   */
  double getMinimum();

  /**
   * Returns the minimum possible value that can be sampled. May return Double.POSITIVE_INFINITY for
   * unbounded distributions.
   */
  double getMaximum();

  /* Returns the probability density at the given value. */
  double getProbabilityDensity(double value);
}
