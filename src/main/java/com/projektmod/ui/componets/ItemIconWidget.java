package com.projektmod.ui.componets;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public class ItemIconWidget extends AbstractWidget{
    ItemStack stack;
    public ItemIconWidget(int x, int y, int width, int height, @NonNull ItemStack itemStack) {
        super(x, y, width, height, Objects.requireNonNullElse(itemStack.getCustomName(), Component.empty()));
        stack = itemStack;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.item(this.stack, this.getX(), this.getY());
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {

    }
}
