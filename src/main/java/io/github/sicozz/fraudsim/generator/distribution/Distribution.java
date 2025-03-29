package io.github.sicozz.fraudsim.generator.distribution;

public interface Distribution<T> {
  /* Samples a single value from this distribution. */
  T sample();

  String getName();

  String getDescription();
}
