package com.projektmod.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.projektmod.projektMod.ITEMS;
import static com.projektmod.projektMod.MODID;

public class MarketScreen extends AbstractContainerScreen<MarketMenu> {

    public int windowWidth = 320;
    public int windowHeight = 200;
    private final int LIST_WIDTH = 300;
    private final int LAYOUT_SPACING = 6;
    private final int MAIN_AREA_HEIGHT = 100;

    public MarketScreen(MarketMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void init() {
        super.init();
        this.windowWidth = this.width;
        this.windowHeight = this.height;

        LinearLayout main  = LinearLayout.vertical().spacing(LAYOUT_SPACING);

        EditBox searchBar = new EditBox(this.font, 0, 0, windowWidth, 20,Component.translatable(MODID + ".gui.market.search"));
        searchBar.setHint(Component.translatable(MODID + ".gui.market.search"));
        main.addChild(searchBar);

        LinearLayout listItemLayout = LinearLayout.horizontal().spacing(LAYOUT_SPACING);

        MarketList marketList = new MarketList(minecraft, LIST_WIDTH, MAIN_AREA_HEIGHT, 0, 20);
        SelectedItemView selectedItemView = new SelectedItemView(0, 0);
        listItemLayout.addChild(selectedItemView);
        listItemLayout.addChild(marketList);

        main.addChild(listItemLayout);

        main.arrangeElements();

        main.setX((this.width - main.getWidth()) / 2);
        main.setY(0);

        main.arrangeElements();

        Supplier<String> randSymbol = () -> {return new String[]{"▼", "▲", "-"}[java.util.concurrent.ThreadLocalRandom.current().nextInt(3)];};
        for (int i = 0; i < 1000; i++) {
            marketList.addListEntry(new MarketList.MarketEntry(new ItemStack(Items.EMERALD), "22"+randSymbol.get(), "11"+randSymbol.get()));
            marketList.addListEntry(new MarketList.MarketEntry(new ItemStack(Items.IRON_AXE), "14"+randSymbol.get(), "23"+randSymbol.get()));
            marketList.addListEntry(new MarketList.MarketEntry(new ItemStack(Items.GOLD_BLOCK), "31"+randSymbol.get(), "90"+randSymbol.get()));
        }
        main.visitWidgets(this::addRenderableWidget);
    }

    @Override
    protected void extractLabels(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(this.font, this.playerInventoryTitle, 8, 99, -12566464, false);
    }


}