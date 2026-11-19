package com.projektmod.chart;

import java.util.Arrays;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class MapCanvas {
  public static final int WIDTH = 128;
  public static final int HEIGHT = 128;

  private final byte[] pixels = new byte[WIDTH * HEIGHT];

  public void fill(byte color) {
    Arrays.fill(pixels, color);
  }

  public void setPixel(int x, int y, byte color) {
    if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
      pixels[x + y * WIDTH] = color;
    }
  }

  public byte getPixel(int x, int y) {
    if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
      return pixels[x + y * WIDTH];
    }
    return 0;
  }

  public void fillRect(int x, int y, int width, int height, byte color) {
    int xStart = Math.max(0, x);
    int yStart = Math.max(0, y);
    int xEnd = Math.min(WIDTH, x + width);
    int yEnd = Math.min(HEIGHT, y + height);
    for (int py = yStart; py < yEnd; py++) {
      int rowOffset = py * WIDTH;
      for (int px = xStart; px < xEnd; px++) {
        pixels[rowOffset + px] = color;
      }
    }
  }

  public void drawRect(int x, int y, int width, int height, byte color) {
    if (width <= 0 || height <= 0) {
      return;
    }
    drawHorizontalLine(x, x + width - 1, y, color);
    drawHorizontalLine(x, x + width - 1, y + height - 1, color);
    drawVerticalLine(x, y, y + height - 1, color);
    drawVerticalLine(x + width - 1, y, y + height - 1, color);
  }

  public void drawHorizontalLine(int x1, int x2, int y, byte color) {
    if (y < 0 || y >= HEIGHT) {
      return;
    }
    int start = Math.max(0, Math.min(x1, x2));
    int end = Math.min(WIDTH - 1, Math.max(x1, x2));
    int rowOffset = y * WIDTH;
    for (int x = start; x <= end; x++) {
      pixels[rowOffset + x] = color;
    }
  }

  public void drawDashedHorizontalLine(
      int x1, int x2, int y, int dashLength, int gapLength, byte color) {
    if (y < 0 || y >= HEIGHT || dashLength <= 0) {
      return;
    }
    int start = Math.max(0, Math.min(x1, x2));
    int end = Math.min(WIDTH - 1, Math.max(x1, x2));
    int period = dashLength + Math.max(0, gapLength);
    int rowOffset = y * WIDTH;
    for (int x = start; x <= end; x++) {
      if ((x - start) % period < dashLength) {
        pixels[rowOffset + x] = color;
      }
    }
  }

  public void drawVerticalLine(int x, int y1, int y2, byte color) {
    if (x < 0 || x >= WIDTH) {
      return;
    }
    int start = Math.max(0, Math.min(y1, y2));
    int end = Math.min(HEIGHT - 1, Math.max(y1, y2));
    for (int y = start; y <= end; y++) {
      pixels[x + y * WIDTH] = color;
    }
  }

  public void drawLine(int x0, int y0, int x1, int y1, byte color) {
    int dx = Math.abs(x1 - x0);
    int dy = Math.abs(y1 - y0);
    int sx = x0 < x1 ? 1 : -1;
    int sy = y0 < y1 ? 1 : -1;
    int err = dx - dy;

    int x = x0;
    int y = y0;
    while (true) {
      setPixel(x, y, color);
      if (x == x1 && y == y1) {
        break;
      }
      int e2 = 2 * err;
      if (e2 > -dy) {
        err -= dy;
        x += sx;
      }
      if (e2 < dx) {
        err += dx;
        y += sy;
      }
    }
  }

  public void drawThickLine(int x0, int y0, int x1, int y1, byte color) {
    drawLine(x0, y0, x1, y1, color);
    if (Math.abs(x1 - x0) >= Math.abs(y1 - y0)) {
      drawLine(x0, y0 - 1, x1, y1 - 1, color);
    } else {
      drawLine(x0 + 1, y0, x1 + 1, y1, color);
    }
  }

  public void fillAreaUnderSegment(int x0, int y0, int x1, int y1, int bottomY, byte color) {
    if (x0 == x1) {
      int top = Math.min(y0, y1) + 1;
      if (top <= bottomY) {
        drawVerticalLine(x0, top, bottomY, color);
      }
      return;
    }
    int startX = x0;
    int endX = x1;
    int startY = y0;
    int endY = y1;
    if (startX > endX) {
      startX = x1;
      endX = x0;
      startY = y1;
      endY = y0;
    }
    int span = endX - startX;
    for (int x = startX; x <= endX; x++) {
      double t = (double) (x - startX) / span;
      int lineY = (int) Math.round(startY + t * (endY - startY));
      int top = lineY + 1;
      if (top <= bottomY) {
        drawVerticalLine(x, top, bottomY, color);
      }
    }
  }

  public void applyTo(MapItemSavedData data) {
    for (int y = 0; y < HEIGHT; y++) {
      int rowOffset = y * WIDTH;
      for (int x = 0; x < WIDTH; x++) {
        data.setColor(x, y, pixels[rowOffset + x]);
      }
    }
  }

  public byte[] copyPixels() {
    return Arrays.copyOf(pixels, pixels.length);
  }
}
