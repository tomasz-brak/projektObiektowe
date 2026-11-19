package com.projektmod.chart;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class PixelFont {
  public static final int GLYPH_HEIGHT = 5;
  private static final int LETTER_SPACING = 1;
  private static final Map<Character, String[]> GLYPHS = createGlyphs();

  private PixelFont() {}

  public static int measureWidth(String text) {
    if (text == null || text.isEmpty()) {
      return 0;
    }
    String upper = text.toUpperCase(Locale.ROOT);
    int width = 0;
    for (int i = 0; i < upper.length(); i++) {
      String[] glyph = getGlyph(upper.charAt(i));
      width += glyph[0].length();
      if (i + 1 < upper.length()) {
        width += LETTER_SPACING;
      }
    }
    return width;
  }

  public static String truncateToWidth(String text, int maxWidth) {
    if (text == null || text.isEmpty() || maxWidth <= 0) {
      return "";
    }
    if (measureWidth(text) <= maxWidth) {
      return text;
    }
    String ellipsis = "..";
    int ellipsisWidth = measureWidth(ellipsis);
    if (maxWidth <= ellipsisWidth) {
      return "";
    }
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < text.length(); i++) {
      String candidate = builder.toString() + text.charAt(i);
      if (measureWidth(candidate) + LETTER_SPACING + ellipsisWidth > maxWidth) {
        break;
      }
      builder.append(text.charAt(i));
    }
    return builder.toString().stripTrailing() + ellipsis;
  }

  public static void draw(MapCanvas canvas, int x, int y, String text, byte color) {
    if (text == null || text.isEmpty()) {
      return;
    }
    String upper = text.toUpperCase(Locale.ROOT);
    int cursorX = x;
    for (int i = 0; i < upper.length(); i++) {
      String[] glyph = getGlyph(upper.charAt(i));
      for (int row = 0; row < glyph.length; row++) {
        String line = glyph[row];
        for (int col = 0; col < line.length(); col++) {
          if (line.charAt(col) == '#') {
            canvas.setPixel(cursorX + col, y + row, color);
          }
        }
      }
      cursorX += glyph[0].length() + LETTER_SPACING;
    }
  }

  public static void drawRightAligned(
      MapCanvas canvas, int rightX, int y, String text, byte color) {
    int width = measureWidth(text);
    draw(canvas, rightX - width + 1, y, text, color);
  }

  public static void drawCentered(MapCanvas canvas, int centerX, int y, String text, byte color) {
    int width = measureWidth(text);
    draw(canvas, centerX - width / 2, y, text, color);
  }

  private static String[] getGlyph(char c) {
    return GLYPHS.getOrDefault(c, GLYPHS.get('?'));
  }

  private static Map<Character, String[]> createGlyphs() {
    Map<Character, String[]> map = new HashMap<>();

    // Digits 0-9 (3x5)
    map.put('0', new String[] {"###", "#.#", "#.#", "#.#", "###"});
    map.put('1', new String[] {".#.", "##.", ".#.", ".#.", "###"});
    map.put('2', new String[] {"###", "..#", "###", "#..", "###"});
    map.put('3', new String[] {"###", "..#", "###", "..#", "###"});
    map.put('4', new String[] {"#.#", "#.#", "###", "..#", "..#"});
    map.put('5', new String[] {"###", "#..", "###", "..#", "###"});
    map.put('6', new String[] {"###", "#..", "###", "#.#", "###"});
    map.put('7', new String[] {"###", "..#", "..#", ".#.", ".#."});
    map.put('8', new String[] {"###", "#.#", "###", "#.#", "###"});
    map.put('9', new String[] {"###", "#.#", "###", "..#", "###"});

    // Uppercase letters A-Z
    map.put('A', new String[] {".#.", "#.#", "###", "#.#", "#.#"});
    map.put('B', new String[] {"##.", "#.#", "##.", "#.#", "##."});
    map.put('C', new String[] {".##", "#..", "#..", "#..", ".##"});
    map.put('D', new String[] {"##.", "#.#", "#.#", "#.#", "##."});
    map.put('E', new String[] {"###", "#..", "##.", "#..", "###"});
    map.put('F', new String[] {"###", "#..", "##.", "#..", "#.."});
    map.put('G', new String[] {".##", "#..", "#.#", "#.#", ".##"});
    map.put('H', new String[] {"#.#", "#.#", "###", "#.#", "#.#"});
    map.put('I', new String[] {"###", ".#.", ".#.", ".#.", "###"});
    map.put('J', new String[] {"..#", "..#", "..#", "#.#", ".#."});
    map.put('K', new String[] {"#.#", "##.", "#..", "##.", "#.#"});
    map.put('L', new String[] {"#..", "#..", "#..", "#..", "###"});
    map.put('M', new String[] {"#...#", "##.##", "#.#.#", "#...#", "#...#"});
    map.put('N', new String[] {"#..#", "##.#", "#.##", "#..#", "#..#"});
    map.put('O', new String[] {".#.", "#.#", "#.#", "#.#", ".#."});
    map.put('P', new String[] {"##.", "#.#", "##.", "#..", "#.."});
    map.put('Q', new String[] {".#.", "#.#", "#.#", ".##", "..#"});
    map.put('R', new String[] {"##.", "#.#", "##.", "#.#", "#.#"});
    map.put('S', new String[] {".##", "#..", ".#.", "..#", "##."});
    map.put('T', new String[] {"###", ".#.", ".#.", ".#.", ".#."});
    map.put('U', new String[] {"#.#", "#.#", "#.#", "#.#", "###"});
    map.put('V', new String[] {"#.#", "#.#", "#.#", "#.#", ".#."});
    map.put('W', new String[] {"#...#", "#...#", "#.#.#", "##.##", "#...#"});
    map.put('X', new String[] {"#.#", "#.#", ".#.", "#.#", "#.#"});
    map.put('Y', new String[] {"#.#", "#.#", ".#.", ".#.", ".#."});
    map.put('Z', new String[] {"###", "..#", ".#.", "#..", "###"});

    // Punctuation & symbols
    map.put(' ', new String[] {"..", "..", "..", "..", ".."});
    map.put('.', new String[] {".", ".", ".", ".", "#"});
    map.put(',', new String[] {".", ".", ".", "#", "#"});
    map.put(':', new String[] {".", "#", ".", "#", "."});
    map.put('-', new String[] {"...", "...", "###", "...", "..."});
    map.put('+', new String[] {"...", ".#.", "###", ".#.", "..."});
    map.put('/', new String[] {"..#", "..#", ".#.", "#..", "#.."});
    map.put('%', new String[] {"#.#", "..#", ".#.", "#..", "#.#"});
    map.put('$', new String[] {".##", "#..", ".#.", "..#", "##."});
    map.put('(', new String[] {".#", "#.", "#.", "#.", ".#"});
    map.put(')', new String[] {"#.", ".#", ".#", ".#", "#."});
    map.put('_', new String[] {"...", "...", "...", "...", "###"});
    map.put('!', new String[] {"#", "#", "#", ".", "#"});
    map.put('?', new String[] {"##.", "..#", ".#.", "...", ".#."});

    return map;
  }
}
