package com.projektmod.chart;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.MapColor.Brightness;

public class PriceChartRenderer {
  private static final byte COLOR_BG = MapColor.COLOR_BLACK.getPackedId(Brightness.HIGH);
  private static final byte COLOR_HEADER_BG = MapColor.COLOR_BLACK.getPackedId(Brightness.LOW);
  private static final byte COLOR_PLOT_BG = MapColor.COLOR_BLACK.getPackedId(Brightness.NORMAL);
  private static final byte COLOR_BORDER = MapColor.COLOR_GRAY.getPackedId(Brightness.HIGH);
  private static final byte COLOR_DIVIDER = MapColor.COLOR_GRAY.getPackedId(Brightness.NORMAL);
  private static final byte COLOR_GRID = MapColor.COLOR_GRAY.getPackedId(Brightness.LOWEST);
  private static final byte COLOR_AXIS = MapColor.COLOR_LIGHT_GRAY.getPackedId(Brightness.NORMAL);
  private static final byte COLOR_TEXT_PRIMARY = MapColor.SNOW.getPackedId(Brightness.HIGH);
  private static final byte COLOR_TEXT_MUTED =
      MapColor.COLOR_LIGHT_GRAY.getPackedId(Brightness.HIGH);

  private static final byte COLOR_UP_LINE = MapColor.EMERALD.getPackedId(Brightness.HIGH);
  private static final byte COLOR_UP_FILL = MapColor.PLANT.getPackedId(Brightness.LOWEST);
  private static final byte COLOR_DOWN_LINE = MapColor.FIRE.getPackedId(Brightness.HIGH);
  private static final byte COLOR_DOWN_FILL = MapColor.NETHER.getPackedId(Brightness.LOWEST);
  private static final byte COLOR_FLAT_LINE = MapColor.DIAMOND.getPackedId(Brightness.HIGH);
  private static final byte COLOR_FLAT_FILL = MapColor.WARPED_NYLIUM.getPackedId(Brightness.LOWEST);

  private static final int HEADER_HEIGHT = 14;
  private static final int PLOT_TOP = 19;
  private static final int PLOT_BOTTOM = 110;
  private static final int PLOT_RIGHT = 123;
  private static final int FOOTER_Y = 117;

  public MapCanvas render(PriceHistoryEntry entry) {
    MapCanvas canvas = new MapCanvas();
    canvas.fill(COLOR_BG);

    // Header bar
    canvas.fillRect(1, 1, MapCanvas.WIDTH - 2, HEADER_HEIGHT - 1, COLOR_HEADER_BG);
    canvas.drawHorizontalLine(1, MapCanvas.WIDTH - 2, HEADER_HEIGHT, COLOR_DIVIDER);

    // Footer separator
    canvas.drawHorizontalLine(1, MapCanvas.WIDTH - 2, 114, COLOR_DIVIDER);

    // Outer frame
    canvas.drawRect(0, 0, MapCanvas.WIDTH, MapCanvas.HEIGHT, COLOR_BORDER);

    if (entry.isEmpty()) {
      String title = PixelFont.truncateToWidth(entry.displayTitle(), MapCanvas.WIDTH - 10);
      PixelFont.draw(canvas, 4, 5, title, COLOR_TEXT_PRIMARY);
      PixelFont.drawCentered(canvas, MapCanvas.WIDTH / 2, 60, "NO PRICE DATA", COLOR_TEXT_MUTED);
      return canvas;
    }

    List<Double> prices = entry.prices();
    double firstPrice = entry.firstPrice();
    double latestPrice = entry.latestPrice();
    double minPrice = entry.minPrice();
    double maxPrice = entry.maxPrice();

    byte lineColor;
    byte fillColor;
    if (prices.size() == 1 || Math.abs(latestPrice - firstPrice) < 1e-9) {
      lineColor = COLOR_FLAT_LINE;
      fillColor = COLOR_FLAT_FILL;
    } else if (latestPrice > firstPrice) {
      lineColor = COLOR_UP_LINE;
      fillColor = COLOR_UP_FILL;
    } else {
      lineColor = COLOR_DOWN_LINE;
      fillColor = COLOR_DOWN_FILL;
    }

    drawHeader(canvas, entry, firstPrice, latestPrice, lineColor);
    drawPlot(canvas, prices, minPrice, maxPrice, lineColor, fillColor);
    drawFooter(canvas, minPrice, maxPrice, prices.size());

    return canvas;
  }

  private void drawHeader(
      MapCanvas canvas,
      PriceHistoryEntry entry,
      double firstPrice,
      double latestPrice,
      byte trendColor) {
    String rightLabel = formatHeaderMetric(firstPrice, latestPrice, entry.prices().size());
    int rightWidth = PixelFont.measureWidth(rightLabel);
    PixelFont.drawRightAligned(canvas, MapCanvas.WIDTH - 5, 5, rightLabel, trendColor);

    int maxTitleWidth = MapCanvas.WIDTH - 12 - rightWidth;
    String title = PixelFont.truncateToWidth(entry.displayTitle(), maxTitleWidth);
    PixelFont.draw(canvas, 4, 5, title, COLOR_TEXT_PRIMARY);
  }

  private void drawPlot(
      MapCanvas canvas,
      List<Double> prices,
      double minPrice,
      double maxPrice,
      byte lineColor,
      byte fillColor) {
    double yMin = minPrice;
    double yMax = maxPrice;
    if (Math.abs(yMax - yMin) < 1e-9) {
      double pad = Math.max(1.0, Math.round(Math.abs(minPrice) * 0.1));
      yMin = Math.max(0.0, minPrice - pad);
      yMax = maxPrice + pad;
      if (Math.abs(yMax - yMin) < 1e-9) {
        yMax = yMin + 2.0;
      }
    }

    List<Double> ticks = computeTicks(yMin, yMax);
    int maxLabelWidth = 0;
    for (double tick : ticks) {
      maxLabelWidth = Math.max(maxLabelWidth, PixelFont.measureWidth(formatPrice(tick)));
    }

    int plotLeft = Math.min(40, Math.max(14, maxLabelWidth + 6));
    int dataLeft = plotLeft + 3;
    int dataRight = PLOT_RIGHT - 3;
    int dataTop = PLOT_TOP + 2;
    int dataBottom = PLOT_BOTTOM - 2;

    // Inner plot background
    canvas.fillRect(
        plotLeft + 1, PLOT_TOP, PLOT_RIGHT - plotLeft, PLOT_BOTTOM - PLOT_TOP, COLOR_PLOT_BG);

    // Horizontal grid lines and Y-axis labels
    for (double tick : ticks) {
      int tickY = mapPriceToY(tick, yMin, yMax, dataTop, dataBottom);
      canvas.drawDashedHorizontalLine(plotLeft + 1, PLOT_RIGHT, tickY, 2, 2, COLOR_GRID);
      canvas.drawHorizontalLine(plotLeft - 1, plotLeft, tickY, COLOR_AXIS);
      PixelFont.drawRightAligned(
          canvas,
          plotLeft - 3,
          tickY - PixelFont.GLYPH_HEIGHT / 2,
          formatPrice(tick),
          COLOR_TEXT_MUTED);
    }

    // Compute pixel coordinates for each price point
    int count = prices.size();
    int[] xs = new int[count];
    int[] ys = new int[count];
    for (int i = 0; i < count; i++) {
      xs[i] =
          count == 1
              ? (dataLeft + dataRight) / 2
              : dataLeft + (int) Math.round((double) i * (dataRight - dataLeft) / (count - 1));
      ys[i] = mapPriceToY(prices.get(i), yMin, yMax, dataTop, dataBottom);
    }

    // Soft shaded area under line chart
    if (count == 1) {
      canvas.fillAreaUnderSegment(dataLeft, ys[0], dataRight, ys[0], PLOT_BOTTOM - 1, fillColor);
    } else {
      for (int i = 0; i < count - 1; i++) {
        canvas.fillAreaUnderSegment(xs[i], ys[i], xs[i + 1], ys[i + 1], PLOT_BOTTOM - 1, fillColor);
      }
    }

    // Re-draw subtle grid dots on top of shaded area for readability
    for (double tick : ticks) {
      int tickY = mapPriceToY(tick, yMin, yMax, dataTop, dataBottom);
      canvas.drawDashedHorizontalLine(plotLeft + 1, PLOT_RIGHT, tickY, 1, 3, COLOR_GRID);
    }

    // Price trend line
    if (count == 1) {
      canvas.drawThickLine(dataLeft, ys[0], dataRight, ys[0], lineColor);
    } else {
      for (int i = 0; i < count - 1; i++) {
        canvas.drawThickLine(xs[i], ys[i], xs[i + 1], ys[i + 1], lineColor);
      }
    }

    // Sample point markers when points are not overly dense
    if (count <= 32) {
      for (int i = 0; i < count - 1; i++) {
        canvas.setPixel(xs[i], ys[i], COLOR_TEXT_PRIMARY);
      }
    }

    // Highlight latest data point
    int lastX = xs[count - 1];
    int lastY = ys[count - 1];
    canvas.fillRect(lastX - 1, lastY - 1, 3, 3, lineColor);
    canvas.setPixel(lastX, lastY, COLOR_TEXT_PRIMARY);

    // Axes drawn cleanly on top
    canvas.drawVerticalLine(plotLeft, PLOT_TOP, PLOT_BOTTOM, COLOR_AXIS);
    canvas.drawHorizontalLine(plotLeft, PLOT_RIGHT, PLOT_BOTTOM, COLOR_AXIS);
  }

  private void drawFooter(MapCanvas canvas, double minPrice, double maxPrice, int count) {
    String footer =
        "MIN:" + formatPrice(minPrice) + " MAX:" + formatPrice(maxPrice) + " N:" + count;
    if (PixelFont.measureWidth(footer) > MapCanvas.WIDTH - 8) {
      footer = "LO:" + formatPrice(minPrice) + " HI:" + formatPrice(maxPrice);
    }
    footer = PixelFont.truncateToWidth(footer, MapCanvas.WIDTH - 8);
    PixelFont.drawCentered(canvas, MapCanvas.WIDTH / 2, FOOTER_Y, footer, COLOR_TEXT_MUTED);
  }

  private static int mapPriceToY(
      double price, double yMin, double yMax, int dataTop, int dataBottom) {
    double ratio = (price - yMin) / (yMax - yMin);
    int y = dataBottom - (int) Math.round(ratio * (dataBottom - dataTop));
    return Math.clamp(y, dataTop, dataBottom);
  }

  private static List<Double> computeTicks(double yMin, double yMax) {
    double span = yMax - yMin;
    boolean wholeEndpoints = isWholeNumber(yMin) && isWholeNumber(yMax);
    int tickCount = 5;
    if (wholeEndpoints && span >= 1.0 && span <= 4.0) {
      tickCount = (int) Math.round(span) + 1;
    }

    List<Double> ticks = new ArrayList<>(tickCount);
    for (int i = 0; i < tickCount; i++) {
      double value = yMin + span * i / (tickCount - 1);
      ticks.add(value);
    }
    return ticks;
  }

  private static String formatHeaderMetric(double firstPrice, double latestPrice, int count) {
    String priceStr = formatPrice(latestPrice);
    if (count < 2 || Math.abs(firstPrice) < 1e-9) {
      return priceStr;
    }
    double pct = ((latestPrice - firstPrice) / firstPrice) * 100.0;
    long roundedPct = Math.round(pct);
    if (roundedPct > 0) {
      return priceStr + " +" + roundedPct + "%";
    }
    if (roundedPct < 0) {
      return priceStr + " " + roundedPct + "%";
    }
    return priceStr + " 0%";
  }

  public static String formatPrice(double value) {
    double abs = Math.abs(value);
    if (abs >= 1_000_000) {
      return formatCompact(value / 1_000_000.0, "M");
    }
    if (abs >= 10_000) {
      return formatCompact(value / 1_000.0, "K");
    }
    return formatOneDecimal(value);
  }

  private static String formatCompact(double scaled, String suffix) {
    return formatOneDecimal(scaled) + suffix;
  }

  private static String formatOneDecimal(double value) {
    String formatted = String.format(Locale.ROOT, "%.1f", value);
    if (formatted.endsWith(".0")) {
      return formatted.substring(0, formatted.length() - 2);
    }
    return formatted;
  }

  private static boolean isWholeNumber(double value) {
    return Math.abs(value - Math.rint(value)) < 1e-6;
  }
}
