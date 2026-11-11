package com.projektmod.ui;

import com.projektmod.ui.componets.ItemIconWidget;
import com.projektmod.ui.componets.ScrollableTableElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.List;

import static java.lang.Math.max;

public class MarketList extends ObjectSelectionList<MarketList.MarketEntry> {
    private static final ScrollableTableElement.Columns columns = new ScrollableTableElement.Columns(List.of(20, 80, 30, 30, 30));

    public MarketList(Minecraft minecraft, int width, int height, int y, int itemHeight) {
    super(minecraft, max(columns.getAggregateWidth(), width), height, y, itemHeight);
  }

  public void addListEntry(MarketEntry entry){
    this.addEntry(entry);
    this.setScrollAmount(0);
  }

  @Override
  public int getRowWidth() {
    return columns.getAggregateWidth();
  }


  public static class MarketEntry extends ScrollableTableElement<MarketEntry> {
    private final ItemStack iconStack;
    public String buyCost;
    public String sellCost;
    public String amount;

    private final ItemIconWidget emptyIcon;
    private final StringWidget nameWidget;
    private final StringWidget buyCostWidget;
    private final StringWidget sellCostWidget;
    private final StringWidget amountWidget;

    public MarketEntry(ItemStack iconStack, String buyCost, String sellCost, String amount) {
      super(columns);
      this.buyCost = buyCost;
      this.sellCost = sellCost;
      this.iconStack = iconStack;
      this.amount = amount;

        Font font = Minecraft.getInstance().font;

      this.emptyIcon = new ItemIconWidget(0, 0, 16, 16, this.iconStack);
      this.addElement(this.emptyIcon);

      this.nameWidget = new StringWidget(iconStack.getHoverName(), font);
      this.addElement(this.nameWidget);

      this.buyCostWidget = new StringWidget(Component.literal(String.valueOf(this.buyCost)).withColor(0xFF00DD00), font);
      this.addElement(this.buyCostWidget);

      this.sellCostWidget = new StringWidget(Component.literal(String.valueOf(this.sellCost)).withColor(0xFFDD0000), font);
      this.addElement(this.sellCostWidget);

      this.amountWidget = new StringWidget(Component.literal(String.valueOf(this.amount)), font);
      this.addElement(this.amountWidget);
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
        graphics.fill(x1, y1, x2, y2, 0x40FF00FF);
      }

      super.extractContent(graphics, mouseX, mouseY, hovered, a);

      graphics.fill(x1, y2 - 1, x2, y2, 0xFF333333);
    }


  }
}
