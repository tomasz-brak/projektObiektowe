package com.projektmod.ui;

import com.projektmod.projektMod;
import com.projektmod.ui.componets.LargeSlot;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NonNull;

import java.util.function.Supplier;

import static com.projektmod.projektMod.MODID;

public class MarketScreen extends AbstractContainerScreen<MarketMenu> {

    public int windowWidth = 320;
    public int windowHeight = 200;
    private final int LAYOUT_SPACING = 6;
    private final int MAIN_AREA_HEIGHT = 100;
    private static final Identifier SLOT_HIGHLIGHT_BACK = Identifier.withDefaultNamespace("container/slot_highlight_back");
    private static final Identifier SLOT_HIGHLIGHT_FRONT = Identifier.withDefaultNamespace("container/slot_highlight_front");

    private MarketList marketList;

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

        marketList = new MarketList(minecraft, 0, MAIN_AREA_HEIGHT, 0, 20);
        SelectedItemView selectedItemView = new SelectedItemView(0, 0, this.menu.getCurrentPicked());

        listItemLayout.addChild(selectedItemView);
        int spacerWidth = this.windowWidth - selectedItemView.getWidth() - marketList.getWidth() - LAYOUT_SPACING*2; // TODO: Dumb make this smarter
        if (spacerWidth > 0) {
            listItemLayout.addChild(SpacerElement.width(spacerWidth));
        }
        listItemLayout.addChild(marketList);

        main.addChild(listItemLayout);

        main.arrangeElements();

        main.setX((this.width - main.getWidth()) / 2);
        main.setY(0);

        main.arrangeElements();

        Supplier<String> randSymbol = () -> {return new String[]{"▼", "▲", "-"}[java.util.concurrent.ThreadLocalRandom.current().nextInt(3)];};
        for (int i = 0; i < 1000; i++) {
            marketList.addListEntry(new MarketList.MarketEntry(new ItemStack(Items.EMERALD), "22"+randSymbol.get(), "11"+randSymbol.get(), "32"));
            marketList.addListEntry(new MarketList.MarketEntry(new ItemStack(Items.IRON_AXE), "14"+randSymbol.get(), "23"+randSymbol.get(), "22"));
            marketList.addListEntry(new MarketList.MarketEntry(new ItemStack(Items.GOLD_BLOCK), "31"+randSymbol.get(), "90"+randSymbol.get(), "11"));
        }
        main.visitWidgets(this::addRenderableWidget);
    }

    @Override
    protected void extractLabels(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(this.font, this.playerInventoryTitle, 8, 99, -12566464, false);
    }

    @Override
    protected void extractSlot(@NonNull GuiGraphicsExtractor graphics, @NonNull Slot slot, int mouseX, int mouseY) {
        if (slot instanceof LargeSlot largeSlot) {
            boolean isHovered = (slot == this.getHoveredSlot());
            int highlightSize = largeSlot.getSize() + 8;

            if (isHovered) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_HIGHLIGHT_BACK, slot.x - 4, slot.y - 4, highlightSize, highlightSize);
            }

            LargeSlot.render(graphics, largeSlot);

            if (isHovered) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_HIGHLIGHT_FRONT, slot.x - 4, slot.y - 4, highlightSize, highlightSize);
            }
            return;
        }

        super.extractSlot(graphics, slot, mouseX, mouseY);
    }


    @Override
    protected boolean isHovering(int left, int top, int w, int h, double xm, double ym) {
        for (Slot slot : this.menu.slots) {
            if (slot instanceof LargeSlot largeSlot && slot.x == left && slot.y == top) {
                return super.isHovering(left, top, largeSlot.getSize(), largeSlot.getSize(), xm, ym);
            }
        }
        return super.isHovering(left, top, w, h, xm, ym);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (this.marketList != null && this.marketList.isMouseOver(mouseX, mouseY)) {
            if (this.marketList.mouseScrolled(mouseX, mouseY, scrollX, scrollY)) {
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

}