package io.github.sicozz.fraudsim.domain.model.money;

public enum Currency {
  USD("US Dollar", "$", 2),
  EUR("Euro", "€", 2),
  GBP("British Pound", "£", 2),
  JPY("Japanese Yen", "¥", 0),
  CAD("Canadian Dollar", "C$", 2),
  AUD("Australian Dollar", "A$", 2),
  CNY("Chinese Yuan", "¥", 2),
  INR("Indian Rupee", "₹", 2),
  BTC("Bitcoin", "₿", 8);

  private final String name;
  private final String symbol;
  private final int defaultFractionDigits;

  // TODO: try @AllArgsConstructor
  Currency(String name, String symbol, int defaultFractionDigits) {
    this.name = name;
    this.symbol = symbol;
    this.defaultFractionDigits = defaultFractionDigits;
  }

  // TODO: try @Getter
  public String getName() {
    return name;
  }

  public String getSymbol() {
    return symbol;
  }

  public int getDefaultFractionDigits() {
    return defaultFractionDigits;
  }
}
