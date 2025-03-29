package io.github.sicozz.fraudsim.generator.distribution;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

/**
 * Distribution for generating realistic transaction timestamps.
 * Models daily and weekly patterns seen in real-world transaction data.
 */
public class TransactionTimeDistribution implements Distribution<LocalDateTime> {

  private final RandomGenerator random;
  private final Map<DayOfWeek, double[]> hourlyWeights;
  private final LocalDate startDate;
  private final LocalDate endDate;

  /**
   * Creates a new transaction time distribution with the given date range.
   *
   * @param startDate The start date (inclusive)
   * @param endDate The end date (inclusive)
   */
  public TransactionTimeDistribution(LocalDate startDate, LocalDate endDate) {
    this(startDate, endDate, new Well19937c());
  }

  /**
   * Creates a new transaction time distribution with the given date range and random generator.
   *
   * @param startDate The start date (inclusive)
   * @param endDate The end date (inclusive)
   * @param random The random number generator to use
   */
  public TransactionTimeDistribution(LocalDate startDate, LocalDate endDate, RandomGenerator random) {
    this.random = random;
    this.startDate = startDate;
    this.endDate = endDate;
    this.hourlyWeights = createDefaultHourlyWeights();
  }

  /**
   * Creates default hourly weights for each day of the week.
   * These model typical patterns where transactions peak during business hours,
   * with different patterns on weekends.
   *
   * @return A map of day of week to hourly weights
   */
  private Map<DayOfWeek, double[]> createDefaultHourlyWeights() {
    Map<DayOfWeek, double[]> weights = new HashMap<>();

    // Weekday pattern (Monday to Friday)
    // Low overnight, increasing during morning, peak at lunch, high afternoon, tapering in evening
    double[] weekdayWeights = {
        0.2, 0.1, 0.1, 0.1, 0.2, 0.5, 1.0, 2.0, // 12am-8am
        3.0, 3.5, 4.0, 4.5, 5.0, 4.5, 4.0, 3.5, // 8am-4pm
        3.0, 3.0, 2.5, 2.0, 1.5, 1.0, 0.5, 0.3  // 4pm-12am
    };

    // Weekend pattern (Saturday and Sunday)
    // Later start, more even distribution, peak in afternoon
    double[] weekendWeights = {
        0.3, 0.2, 0.1, 0.1, 0.1, 0.2, 0.5, 1.0, // 12am-8am
        1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.0, 3.5, // 8am-4pm
        3.0, 2.5, 2.0, 1.5, 1.0, 0.8, 0.5, 0.4  // 4pm-12am
    };

    // Assign patterns to days
    for (DayOfWeek day : DayOfWeek.values()) {
      if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
        weights.put(day, weekendWeights);
      } else {
        weights.put(day, weekdayWeights);
      }
    }

    return weights;
  }

  /**
   * Sets custom hourly weights for a specific day of the week.
   *
   * @param dayOfWeek The day of the week
   * @param hourlyWeights Array of 24 weights, one for each hour of the day
   * @return This distribution, for method chaining
   */
  public TransactionTimeDistribution setHourlyWeights(DayOfWeek dayOfWeek, double[] hourlyWeights) {
    if (hourlyWeights.length != 24) {
      throw new IllegalArgumentException("Hourly weights must have exactly 24 values");
    }

    this.hourlyWeights.put(dayOfWeek, hourlyWeights.clone());
    return this;
  }

  @Override
  public LocalDateTime sample() {
    // First, select a random date in the range
    long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
    long randomDay = (long) (random.nextDouble() * days);
    LocalDate date = startDate.plusDays(randomDay);

    // Then, select a time based on the hourly weights for this day
    DayOfWeek dayOfWeek = date.getDayOfWeek();
    double[] weights = hourlyWeights.get(dayOfWeek);

    /* Random Weighted Selection Algorithm */
    // Calculate total weight for the day
    double totalWeight = 0;
    for (double weight : weights) {
      totalWeight += weight;
    }

    // Select a random hour based on weights
    double randomValue = random.nextDouble() * totalWeight;
    double cumulativeWeight = 0;
    int selectedHour = 0;

    for (int hour = 0; hour < weights.length; hour++) {
      cumulativeWeight += weights[hour];
      if (randomValue < cumulativeWeight) {
        selectedHour = hour;
        break;
      }
    }

    // Add random minutes and seconds
    int minutes = random.nextInt(60);
    int seconds = random.nextInt(60);

    return LocalDateTime.of(date, LocalTime.of(selectedHour, minutes, seconds));
  }

  @Override
  public String getName() {
    return "TransactionTime";
  }

  @Override
  public String getDescription() {
    return String.format("Time distribution from %s to %s with daily patterns",
        startDate, endDate);
  }
}