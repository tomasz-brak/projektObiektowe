package com.projektmod.ui.componets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2fStack;

public class LargeSlot extends Slot {
    private final int size;
    private final float scale;
    public LargeSlot(Container container, int slot, int x, int y, int size, float scale) {
        super(container, slot, x, y);
        this.size = size;
        this.scale = scale;
    }

    public int getSize() {
        return this.size;
    }

    public float getScale() {
        return this.scale;
    }

    public static void render(GuiGraphicsExtractor graphicsExtractor, LargeSlot slot){
        ItemStack stack = slot.getItem();
        if (stack.isEmpty()) return;

        Matrix3x2fStack matrixStack = graphicsExtractor.pose();
        matrixStack.pushMatrix();

        matrixStack.translate(slot.x, slot.y);
        matrixStack.scale(slot.scale, slot.scale);

        graphicsExtractor.item(stack, 0, 0);
        graphicsExtractor.itemDecorations(Minecraft.getInstance().font, stack, 0, 0);

        matrixStack.popMatrix();
    }

    @Override
    public boolean isHighlightable() {
        return false;
    }
}
