package com.projektmod.ui;

import static com.projektmod.projektMod.MODID;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class SelectedItemView extends LinearLayout {
  private final StringWidget priceBuy;
  private final EditBox amountInput;
  private final LinearLayout buttonHolder;
  private final Button sellButton;
  private final Button buyButton;
  private MultiLineTextWidget placeholder;
  private StringWidget priceSell;
  private final int forcedWidth = 150;

  public SelectedItemView(int x, int y, ItemData data) {
    super(x, y, Orientation.VERTICAL);
    this.spacing(4);
    this.defaultCellSetting().alignHorizontallyLeft();
    this.addChild(SpacerElement.width(forcedWidth));
    this.placeholder =
        new MultiLineTextWidget(
            Component.literal("Item Preview Area"), Minecraft.getInstance().font);
    this.addChild(this.placeholder);

    this.priceSell =
        new StringWidget(
            Component.translatable(
                MODID + ".gui.market.selling",
                Component.literal(String.valueOf(data.priceSell)).withStyle(ChatFormatting.GREEN)),
            Minecraft.getInstance().font);
    this.addChild(this.priceSell);

    this.priceBuy =
        new StringWidget(
            Component.translatable(
                MODID + ".gui.market.buying",
                Component.literal(String.valueOf(data.priceBuy)).withStyle(ChatFormatting.RED)),
            Minecraft.getInstance().font);
    this.addChild(this.priceBuy);

    this.amountInput =
        new EditBox(
            Minecraft.getInstance().font,
            80,
            20,
            Component.translatable(MODID + ".gui.market.selectAmount"));
    this.amountInput.setHint(Component.translatable(MODID + ".gui.market.selectAmount"));
    this.addChild(this.amountInput);

    this.buttonHolder = new LinearLayout(0, 0, Orientation.HORIZONTAL);

    this.sellButton =
        new Button.Builder(Component.translatable(MODID + ".gui.market.sell"), event -> {})
            .bounds(0, 0, 40, 20)
            .build();
    this.buttonHolder.addChild(this.sellButton);

    this.buyButton =
        new Button.Builder(Component.translatable(MODID + ".gui.market.buy"), event -> {})
            .bounds(0, 0, 40, 20)
            .build();
    this.buttonHolder.addChild(this.buyButton);

    this.buttonHolder.arrangeElements();
    this.addChild(buttonHolder);

    this.arrangeElements();
  }

  @Override
  public int getWidth() {
    return Math.max(this.forcedWidth, super.getWidth());
  }

  public record ItemData(int priceSell, int priceBuy, int amount, ItemStack stack) {}
}
