package com.projektmod.ui;

import com.projektmod.ui.componets.ScrollableTableElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class MarketList extends ObjectSelectionList<MarketList.MarketEntry> {
    public MarketList(Minecraft minecraft, int width, int height, int y, int itemHeight) {
    super(minecraft, width, height, y, itemHeight);
  }

  public void addListEntry(MarketEntry entry){
    this.addEntry(entry);
    this.setScrollAmount(0);
  }

  public static class MarketEntry extends ScrollableTableElement<MarketEntry> {
    private final ItemStack iconStack;
    public int buyCost;
    public int sellCost;

    private final StringWidget nameWidget;
    private final StringWidget buyCostWidget;
    private final StringWidget sellCostWidget;

    public MarketEntry(ItemStack iconStack, int buyCost, int sellCost) {
      super(new Columns(List.of(80, 30, 30)));
      this.buyCost = buyCost;
      this.sellCost = sellCost;
      this.iconStack = iconStack;

      Font font = Minecraft.getInstance().font;

      this.nameWidget = new StringWidget(iconStack.getHoverName(), font);
      this.addElement(this.nameWidget);

      this.buyCostWidget = new StringWidget(Component.literal(String.valueOf(this.buyCost)), font);

      this.addElement(this.buyCostWidget);

      this.sellCostWidget = new StringWidget(Component.literal(String.valueOf(this.sellCost)), font);
      this.addElement(this.sellCostWidget);
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

      if (hovered) {
        graphics.fill(x1, y1, x2, y2, 0x40FFFFFF);
      }

      graphics.item(this.iconStack, x1 + 2, y1 + (this.getHeight() - 16) / 2);

      super.extractContent(graphics, mouseX, mouseY, hovered, a);

      graphics.fill(x1, y2 - 1, x2, y2, 0xFF333333);
    }
  }
}
