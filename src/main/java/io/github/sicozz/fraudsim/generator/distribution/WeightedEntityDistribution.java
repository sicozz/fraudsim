package io.github.sicozz.fraudsim.generator.distribution;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Function;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

/**
 * Distribution for selecting entities based on weighted probabilities. This is a specialized
 * version of discrete distribution optimized for selecting entities like merchants, card types,
 * etc.
 */
public class WeightedEntityDistribution<T> implements Distribution<T> {

  private final NavigableMap<Double, T> weightMap = new TreeMap<>();
  private final RandomGenerator random;
  private final String name;
  private final double totalWeight;
  private final Function<T, String> labelFunction;

  /**
   * Creates a new weighted entity distribution.
   *
   * @param name The distribution name
   * @param weights Map of entities to their weights
   * @param labelFunction Function to convert an entity to a display string
   */
  public WeightedEntityDistribution(
      String name, Map<T, Double> weights, Function<T, String> labelFunction) {
    this(name, weights, labelFunction, new Well19937c());
  }

  /**
   * Creates a new weighted entity distribution with a custom random generator.
   *
   * @param name The distribution name
   * @param weights Map of entities to their weights
   * @param labelFunction Function to convert an entity to a display string
   * @param random The random number generator to use
   */
  public WeightedEntityDistribution(
      String name,
      Map<T, Double> weights,
      Function<T, String> labelFunction,
      RandomGenerator random) {
    this.name = name;
    this.random = random;
    this.labelFunction = labelFunction;

    // Calculate the cumulative weights
    double runningSum = 0;
    for (Map.Entry<T, Double> entry : weights.entrySet()) {
      runningSum += entry.getValue();
      weightMap.put(runningSum, entry.getKey());
    }

    this.totalWeight = runningSum;
  }

  @Override
  public T sample() {
    double value = random.nextDouble() * totalWeight;
    return weightMap.higherEntry(value).getValue();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDescription() {
    return "Weighted distribution with " + weightMap.size() + " possible entities";
  }

  /**
   * Returns a detailed description of this distribution, including entity labels and weights.
   *
   * @return A string representation of this distribution
   */
  public String getDetailedDescription() {
    StringBuilder sb = new StringBuilder(getDescription());
    sb.append(":\n");

    // Track the previous weight to get the individual weights
    double prevWeight = 0;
    for (Map.Entry<Double, T> entry : weightMap.entrySet()) {
      double weight = entry.getKey() - prevWeight;
      double percentage = (weight / totalWeight) * 100;
      sb.append(
          String.format(
              "  - %s: %.2f (%.2f%%)\n",
              labelFunction.apply(entry.getValue()), weight, percentage));
      prevWeight = entry.getKey();
    }

    return sb.toString();
  }
}
