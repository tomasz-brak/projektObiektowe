package com.projektmod.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class MarketList extends ObjectSelectionList<MarketList.MarketEntry> {
    public MarketList(Minecraft minecraft, int width, int height, int y, int itemHeight) {
    super(minecraft, width, height, y, itemHeight);
  }

  public void addListEntry(MarketEntry entry){
    this.addEntry(entry);
    this.setScrollAmount(0);
  }

  public static class MarketEntry extends ObjectSelectionList.Entry<MarketEntry> {
    private final ItemStack iconStack;
    public int buyCost;
    public int sellCost;

    public MarketEntry(ItemStack iconStack, int buyCost, int sellCost){
      this.buyCost=buyCost;
      this.sellCost=sellCost;

      this.iconStack = iconStack;
    }

    @Override
    public @NonNull Component getNarration() {
      return iconStack.getHoverName();
    }

    @Override
    public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
      int x1 = this.getX();
      int y1 = this.getY();
      int x2 = x1 + this.getWidth();
      int y2 = y1 + this.getHeight();
      int yCenter = y1 + (this.getHeight() - 8) / 2;

      if(hovered){
        graphics.fill(x1, y1, x2, y2, 0x40FFFFFF);
      }

      graphics.item(this.iconStack, x1+2, y1 + (this.getHeight() - 16) / 2);

      graphics.text(
              Minecraft.getInstance().font,
              iconStack.getHoverName(),
              x1 + 6 + 18,
              yCenter,
              0xFFFFFFFF
      );

      graphics.text(
              Minecraft.getInstance().font,
              Component.literal(String.valueOf(this.buyCost)),
              x1 + this.getWidth() - 32,
              yCenter,
              0xFFFFFFFF
      );

      graphics.text(
              Minecraft.getInstance().font,
              Component.literal(String.valueOf(this.sellCost)),
              x1 + this.getWidth() - 16,
              yCenter,
              0xFFFFFFFF
      );


      graphics.fill(x1, y2 - 1, x2, y2, 0xFF333333);
    }
  }
}
