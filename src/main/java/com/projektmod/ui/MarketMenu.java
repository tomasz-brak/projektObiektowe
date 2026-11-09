package com.projektmod.ui;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class MarketMenu extends AbstractContainerMenu {
    private final Container container;

    public MarketMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this(containerId, playerInventory);
    }

    public MarketMenu(int containerId, Inventory playerInventory) {
        super(ModMenus.MARKET_MENU.get(), containerId);
        this.container = new SimpleContainer(1);
        this.addSlot(new Slot(this.container, 0, -80 ,35));
        this.addStandardInventorySlots(playerInventory, 8, 110); // offset to center
    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int slotIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            itemstack = stackInSlot.copy();

            if (slotIndex == 0) {
                if (!this.moveItemStackTo(stackInSlot, 1, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stackInSlot, 0, 1, false)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return this.container.stillValid(player);
    }
}
