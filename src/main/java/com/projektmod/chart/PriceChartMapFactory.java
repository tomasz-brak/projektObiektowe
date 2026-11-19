package com.projektmod.chart;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class PriceChartMapFactory {
  private final PriceChartRenderer renderer;

  public PriceChartMapFactory(PriceChartRenderer renderer) {
    this.renderer = renderer;
  }

  public PriceChartMapFactory() {
    this(new PriceChartRenderer());
  }

  public CreatedChartMap createChartMap(ServerLevel level, PriceHistoryEntry entry) {
    MapCanvas canvas = renderer.render(entry);

    MapItemSavedData mapData =
        MapItemSavedData.createFresh(0, 0, (byte) 0, false, false, level.dimension()).locked();
    canvas.applyTo(mapData);
    mapData.setDirty();

    MapId mapId = level.getFreeMapId();
    level.setMapData(mapId, mapData);

    ItemStack mapStack = new ItemStack(Items.FILLED_MAP);
    mapStack.set(DataComponents.MAP_ID, mapId);
    mapStack.set(DataComponents.CUSTOM_NAME, buildCustomName(entry));
    mapStack.set(DataComponents.LORE, buildLore(entry));

    return new CreatedChartMap(mapStack, mapId, mapData);
  }

  private static Component buildCustomName(PriceHistoryEntry entry) {
    Component itemDisplayName = resolveItemDisplayName(entry);
    return Component.empty()
        .append(itemDisplayName)
        .append(Component.literal(" Price Chart"))
        .withStyle(style -> style.withColor(ChatFormatting.GOLD).withItalic(false));
  }

  private static Component resolveItemDisplayName(PriceHistoryEntry entry) {
    Identifier id = Identifier.tryParse(entry.itemName());
    if (id != null && BuiltInRegistries.ITEM.containsKey(id)) {
      Item item = BuiltInRegistries.ITEM.getValue(id);
      return item.getName(new ItemStack(item));
    }
    return Component.literal(entry.displayTitle());
  }

  private static ItemLore buildLore(PriceHistoryEntry entry) {
    if (entry.isEmpty()) {
      return new ItemLore(
          List.of(
              Component.literal("No price history available")
                  .withStyle(style -> style.withColor(ChatFormatting.GRAY).withItalic(false))));
    }

    String latest = PriceChartRenderer.formatPrice(entry.latestPrice());
    String min = PriceChartRenderer.formatPrice(entry.minPrice());
    String max = PriceChartRenderer.formatPrice(entry.maxPrice());

    return new ItemLore(
        List.of(
            Component.literal("Latest: " + latest)
                .withStyle(style -> style.withColor(ChatFormatting.WHITE).withItalic(false)),
            Component.literal("Min: " + min + " | Max: " + max)
                .withStyle(style -> style.withColor(ChatFormatting.GRAY).withItalic(false)),
            Component.literal("Samples: " + entry.prices().size())
                .withStyle(style -> style.withColor(ChatFormatting.DARK_GRAY).withItalic(false))));
  }

  public record CreatedChartMap(ItemStack stack, MapId mapId, MapItemSavedData mapData) {}
}
