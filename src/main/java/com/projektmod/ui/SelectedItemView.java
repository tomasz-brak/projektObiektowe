package com.projektmod.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public class SelectedItemView extends LinearLayout {
    private MultiLineTextWidget placeholder;
    public SelectedItemView(int x, int y) {
        super(x, y, Orientation.VERTICAL);
        this.spacing(4);
        this.placeholder = new MultiLineTextWidget(Component.literal("Item Preview \n Area"), Minecraft.getInstance().font);
        this.addChild(this.placeholder);
    }
}
