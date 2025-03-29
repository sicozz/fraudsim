package io.github.sicozz.fraudsim.generator.distribution;

import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

public abstract class AbstractNumericDistribution implements NumericDistribution {
  protected final RandomGenerator random;
  protected final String name;

  /* Creates a distribution with the given name and a default random generator */
  protected AbstractNumericDistribution(String name) {
    this.name = name;
    this.random = new Well19937c();
  }

  protected AbstractNumericDistribution(String name, RandomGenerator random) {
    this.name = name;
    this.random = random;
  }

  @Override
  public String getName() {
    return name;
  }

  /* Enforces minimum and maximum bounds on a sampled value. */
  protected double clamToBounds(double value) {
    return Math.min(Math.max(value, getMinimum()), getMaximum());
  }

  @Override
  public String toString() {
    return getName() + " : " + getDescription();
  }
}
