package com.projektmod.ui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.network.chat.Component;

import static com.projektmod.projektMod.MODID;

public class SelectedItemView extends LinearLayout {
    private MultiLineTextWidget placeholder;
    private StringWidget priceBuy;
    private final int forcedWidth=150;

    public SelectedItemView(int x, int y) {
        super(x, y, Orientation.VERTICAL);
        this.spacing(4);
        this.defaultCellSetting().alignHorizontallyCenter();
        this.addChild(SpacerElement.width(forcedWidth));
        this.placeholder = new MultiLineTextWidget(Component.literal("Item Preview Area"), Minecraft.getInstance().font);
        this.addChild(this.placeholder);
        this.priceBuy = new StringWidget(Component.translatable(MODID+".gui.market.selling", Component.literal(String.valueOf(67)).withStyle(ChatFormatting.GREEN)), Minecraft.getInstance().font);
        this.addChild(this.priceBuy);
        this.arrangeElements();
    }

    @Override
    public int getWidth() {
        return Math.max(this.forcedWidth, super.getWidth());
    }
}
