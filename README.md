# Transaction Generator

A configurable transaction generator with fraud injection capabilities for testing and simulation.

## Features

- Core transaction generator creating legitimate patterns based on real-world statistics
- Configurable fraud scenario injector with parameterized fraud patterns
- Kafka producer integration for continuous stream generation
- Command-line interface for controlling volume, pattern distribution, and fraud rates
- Leverages Java 21 Virtual Threads for high-throughput performance

## Requirements

- Java 21 or newer
- Gradle 8.5+ (or use the provided Gradle wrapper)
- Kafka (for integration testing and production use)

## Getting Started

### Building the Project

```bash
./gradlew clean build