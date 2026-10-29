package com.projektmod.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import static com.projektmod.projektMod.MODID;

public class ExampleScreen extends Screen {

  public ExampleScreen() {
    super(Component.translatable("screen.examplemod.my_screen"));
  }

  @Override
  protected void init() {
    super.init();

    int buttonWidth = 100;
    int buttonHeight = 20;
    int centerX = (this.width - buttonWidth) / 2;
    int centerY = (this.height - buttonHeight) / 2;

    this.addRenderableWidget(Button.builder(
                    Component.translatable(MODID+".gui.close"),
                    button -> this.onClose()
            )
            .bounds(centerX, centerY, buttonWidth, buttonHeight)
            .build());
  }

  @Override
  public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
    super.extractRenderState(graphics, mouseX, mouseY, partialTick);

    int textX = this.width / 2;
    int textY = 40;
    int textColor = 0xFFFFFFFF;
    graphics.text(font, Component.translatable(MODID+".gui.title"), textX, textY, textColor);
  }


  @Override
  public boolean isPauseScreen() {
    return false;
  }

}
