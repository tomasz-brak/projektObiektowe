package com.projektmod.chart;

import java.util.List;
import java.util.Locale;

public record PriceHistoryEntry(String itemName, List<Double> prices) {
  public PriceHistoryEntry {
    prices = prices == null ? List.of() : List.copyOf(prices);
  }

  public boolean isEmpty() {
    return prices.isEmpty();
  }

  public double minPrice() {
    return prices.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
  }

  public double maxPrice() {
    return prices.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
  }

  public double firstPrice() {
    return prices.isEmpty() ? 0.0 : prices.getFirst();
  }

  public double latestPrice() {
    return prices.isEmpty() ? 0.0 : prices.getLast();
  }

  public String displayTitle() {
    int colonIndex = itemName.indexOf(':');
    String path = colonIndex >= 0 ? itemName.substring(colonIndex + 1) : itemName;
    return path.replace('_', ' ').toUpperCase(Locale.ROOT);
  }
}
