package io.github.sicozz.fraudsim.generator.distribution;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;

/**
 * Implementation of a discrete distribution for categorical values. Useful for selecting from a
 * finite set of options with different probabilities, such as merchants, transaction types, or
 * payment methods.
 */
public class DiscreteDistribution<T> implements Distribution<T> {

  private final EnumeratedDistribution<T> distribution;
  private final String name;
  private final Map<T, Double> probabilities;

  /**
   * Creates a new discrete distribution from a map of values to probabilities.
   *
   * @param name The distribution name
   * @param probabilities Map of values to their probabilities
   */
  public DiscreteDistribution(String name, Map<T, Double> probabilities) {
    this.name = name;
    this.probabilities = Map.copyOf(probabilities);

    // PMF (Probability Mass Function): Convert to the format expected by EnumeratedDistribution
    List<Pair<T, Double>> pmf =
        probabilities.entrySet().stream()
            .map(entry -> new Pair<>(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());

    this.distribution = new EnumeratedDistribution<>(pmf);
  }

  /**
   * Creates a new discrete distribution with a custom random generator.
   *
   * @param name The distribution name
   * @param probabilities Map of values to their probabilities
   * @param random The random number generator to use
   */
  public DiscreteDistribution(String name, Map<T, Double> probabilities, RandomGenerator random) {
    this.name = name;
    this.probabilities = Map.copyOf(probabilities);

    // Convert to the format expected by EnumeratedDistribution
    List<Pair<T, Double>> pmf =
        probabilities.entrySet().stream()
            .map(entry -> new Pair<>(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());

    this.distribution = new EnumeratedDistribution<>(random, pmf);
  }

  @Override
  public T sample() {
    return distribution.sample();
  }

  @Override
  public String getName() {
    return name;
  }

  /**
   * Returns the probability of a specific value.
   *
   * @param value The value to check
   * @return The probability of the value
   */
  public double getProbability(T value) {
    return probabilities.getOrDefault(value, 0.0);
  }

  /**
   * Returns a map of all values to their probabilities.
   *
   * @return The probability map
   */
  public Map<T, Double> getProbabilities() {
    return probabilities;
  }

  @Override
  public String getDescription() {
    return "Discrete distribution with " + probabilities.size() + " possible values";
  }

  @Override
  public String toString() {
    return getName() + ": " + getDescription();
  }
}
