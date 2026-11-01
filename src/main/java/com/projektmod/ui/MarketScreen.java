package com.projektmod.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.NonNull;

import static com.projektmod.projektMod.ITEMS;
import static com.projektmod.projektMod.MODID;

public class MarketScreen extends Screen {

    private final int windowWidth = 320;
    private final int windowHeight = 200;

    public MarketScreen() {
        super(Component.translatable(MODID + ".gui.market"));
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void init() {
        LinearLayout main  = LinearLayout.vertical().spacing(12);

        EditBox searchBar = new EditBox(this.font, 0, 0, windowWidth, 20,Component.translatable(MODID + ".gui.market.search"));
        searchBar.setHint(Component.translatable(MODID + ".gui.market.search"));
        main.addChild(searchBar);


        MarketList marketList = new MarketList(minecraft, windowWidth, 100, 0, 20);
        main.addChild(marketList);

        main.arrangeElements();

        main.setX((this.width - main.getWidth()) / 2);
        main.setY((this.height - main.getHeight()) / 2);

        main.arrangeElements();

        // Sample items TODO to be moved
        marketList.addListEntry(new MarketList.MarketEntry(new ItemStack(Items.EMERALD)));
        marketList.addListEntry(new MarketList.MarketEntry(new ItemStack(Items.IRON_AXE)));
        marketList.addListEntry(new MarketList.MarketEntry(new ItemStack(Items.GOLD_BLOCK)));

        main.visitWidgets(this::addRenderableWidget);
    }

}