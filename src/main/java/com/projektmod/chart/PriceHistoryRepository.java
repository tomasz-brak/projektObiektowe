package com.projektmod.chart;

import static com.projektmod.projektMod.LOGGER;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import net.minecraft.resources.Identifier;
import net.neoforged.fml.loading.FMLPaths;

public class PriceHistoryRepository {
  public static final String FILE_NAME = "price_history.json";
  private static final Gson GSON = new Gson();
  private static final Type ENTRY_LIST_TYPE =
      new TypeToken<List<RawPriceHistoryEntry>>() {}.getType();

  private final List<Path> candidatePaths;

  public PriceHistoryRepository(List<Path> candidatePaths) {
    this.candidatePaths = List.copyOf(candidatePaths);
  }

  public static PriceHistoryRepository createDefault() {
    List<Path> paths = new ArrayList<>();
    Path gameDir = FMLPaths.GAMEDIR.get().toAbsolutePath().normalize();
    paths.add(gameDir.resolve(FILE_NAME));
    Path parentDir = gameDir.getParent();
    if (parentDir != null) {
      paths.add(parentDir.resolve(FILE_NAME));
    }
    paths.add(FMLPaths.CONFIGDIR.get().toAbsolutePath().normalize().resolve(FILE_NAME));
    return new PriceHistoryRepository(paths);
  }

  public Optional<PriceHistoryEntry> findByItemId(Identifier itemId) {
    Map<String, PriceHistoryEntry> entries = loadAll();
    return Optional.ofNullable(entries.get(itemId.toString().toLowerCase(Locale.ROOT)));
  }

  public List<String> getAvailableItemIds() {
    List<String> ids = new ArrayList<>(loadAll().keySet());
    Collections.sort(ids);
    return ids;
  }

  public Map<String, PriceHistoryEntry> loadAll() {
    for (Path path : candidatePaths) {
      if (Files.isRegularFile(path)) {
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
          return parseEntries(reader, path.toString());
        } catch (IOException | JsonParseException e) {
          LOGGER.error("Failed to read price history from {}", path, e);
          return Map.of();
        }
      }
    }

    try (InputStream resourceStream =
        PriceHistoryRepository.class.getResourceAsStream("/" + FILE_NAME)) {
      if (resourceStream != null) {
        try (Reader reader = new InputStreamReader(resourceStream, StandardCharsets.UTF_8)) {
          return parseEntries(reader, "classpath:/" + FILE_NAME);
        }
      }
    } catch (IOException | JsonParseException e) {
      LOGGER.error("Failed to read price history from classpath resource {}", FILE_NAME, e);
      return Map.of();
    }

    LOGGER.warn("Could not find {} in any of: {}", FILE_NAME, candidatePaths);
    return Map.of();
  }

  public List<Path> getCandidatePaths() {
    return candidatePaths;
  }

  private Map<String, PriceHistoryEntry> parseEntries(Reader reader, String sourceDescription) {
    List<RawPriceHistoryEntry> rawEntries = GSON.fromJson(reader, ENTRY_LIST_TYPE);
    if (rawEntries == null) {
      LOGGER.warn("Price history file {} was empty", sourceDescription);
      return Map.of();
    }

    Map<String, PriceHistoryEntry> result = new LinkedHashMap<>();
    for (RawPriceHistoryEntry raw : rawEntries) {
      if (raw == null || raw.itemName == null || raw.itemName.isBlank()) {
        continue;
      }
      String normalizedId = normalizeItemId(raw.itemName.trim());
      List<Double> prices =
          raw.prices == null
              ? List.of()
              : raw.prices.stream().filter(p -> p != null && Double.isFinite(p)).toList();
      result.put(normalizedId, new PriceHistoryEntry(normalizedId, prices));
    }
    return Collections.unmodifiableMap(result);
  }

  static String normalizeItemId(String rawName) {
    String lower = rawName.toLowerCase(Locale.ROOT);
    return lower.contains(":") ? lower : "minecraft:" + lower;
  }

  private static final class RawPriceHistoryEntry {
    @SerializedName("item_name")
    String itemName;

    @SerializedName("prices")
    List<Double> prices;
  }
}
