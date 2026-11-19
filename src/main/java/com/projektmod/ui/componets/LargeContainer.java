package com.projektmod.ui.componets;

import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class LargeContainer implements Container, StackedContentsCompatible {
    public static final int DEFAULT_MAX_STACK_SIZE = Integer.MAX_VALUE;

    private final int size;
    private final NonNullList<ItemStack> items;
    private int maxStackSize;

    public LargeContainer() {
        this(1, DEFAULT_MAX_STACK_SIZE);
    }

    public LargeContainer(int size) {
        this(size, DEFAULT_MAX_STACK_SIZE);
    }

    public LargeContainer(int size, int maxStackSize) {
        this.size = size;
        this.maxStackSize = maxStackSize > 0 ? maxStackSize : DEFAULT_MAX_STACK_SIZE;
        this.items = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    public LargeContainer(ItemStack... itemstacks) {
        this(DEFAULT_MAX_STACK_SIZE, itemstacks);
    }

    public LargeContainer(int maxStackSize, ItemStack... itemstacks) {
        this.size = itemstacks.length;
        this.maxStackSize = maxStackSize > 0 ? maxStackSize : DEFAULT_MAX_STACK_SIZE;
        this.items = NonNullList.of(ItemStack.EMPTY, itemstacks);
    }

    public void setMaxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize > 0 ? maxStackSize : DEFAULT_MAX_STACK_SIZE;
    }

    @Override
    public int getMaxStackSize() {
        return this.maxStackSize;
    }

    @Override
    public int getMaxStackSize(ItemStack itemStack) {
        return this.maxStackSize;
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot >= 0 && slot < this.items.size() ? this.items.get(slot) : ItemStack.EMPTY;
    }

    public List<ItemStack> removeAllItems() {
        List<ItemStack> itemsRemoved = this.items.stream().filter((item) -> !item.isEmpty()).collect(Collectors.toList());
        this.clearContent();
        return itemsRemoved;
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        ItemStack result = ContainerHelper.removeItem(this.items, slot, count);
        if (!result.isEmpty()) {
            this.setChanged();
        }

        return result;
    }

    public ItemStack removeItemType(Item itemType, int count) {
        ItemStack removed = new ItemStack(itemType, 0);

        for (int slot = this.size - 1; slot >= 0; --slot) {
            ItemStack current = this.getItem(slot);
            if (current.is(itemType)) {
                int stillNeeded = count - removed.getCount();
                ItemStack removedFromThisSlot = current.split(stillNeeded);
                removed.grow(removedFromThisSlot.getCount());
                if (removed.getCount() == count) {
                    break;
                }
            }
        }

        if (!removed.isEmpty()) {
            this.setChanged();
        }

        return removed;
    }

    public ItemStack addItem(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            ItemStack remainingItems = itemStack.copy();
            this.moveItemToOccupiedSlotsWithSameType(remainingItems);
            if (remainingItems.isEmpty()) {
                return ItemStack.EMPTY;
            } else {
                this.moveItemToEmptySlots(remainingItems);
                return remainingItems.isEmpty() ? ItemStack.EMPTY : remainingItems;
            }
        }
    }

    public boolean canAddItem(ItemStack itemStack) {
        boolean hasSpace = false;

        for (ItemStack targetStack : this.items) {
            if (targetStack.isEmpty() || (ItemStack.isSameItemSameComponents(targetStack, itemStack) && targetStack.getCount() < this.getMaxStackSize(targetStack))) {
                hasSpace = true;
                break;
            }
        }

        return hasSpace;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack itemStack = this.items.get(slot);
        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.items.set(slot, ItemStack.EMPTY);
            return itemStack;
        }
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        this.setItem(slot, itemStack, false);
    }

    public void setItem(int slot, ItemStack itemStack, boolean insideTransaction) {
        this.items.set(slot, itemStack);
        if (!itemStack.isEmpty()) {
            itemStack.limitSize(this.getMaxStackSize(itemStack));
        }
        if (!insideTransaction) {
            this.setChanged();
        }
    }

    @Override
    public void setChanged() {
    }

    @Override
    public int getContainerSize() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : this.items) {
            if (!itemStack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.items.clear();
        this.setChanged();
    }

    @Override
    public void fillStackedContents(StackedItemContents contents) {
        for (ItemStack itemStack : this.items) {
            contents.accountStack(itemStack);
        }
    }

    @Override
    public String toString() {
        return this.items.stream().filter((item) -> !item.isEmpty()).toList().toString();
    }

    private void moveItemToEmptySlots(ItemStack sourceStack) {
        for (int slot = 0; slot < this.size; ++slot) {
            ItemStack targetStack = this.getItem(slot);
            if (targetStack.isEmpty()) {
                int maxCount = this.getMaxStackSize(sourceStack);
                if (sourceStack.getCount() > maxCount) {
                    this.setItem(slot, sourceStack.split(maxCount));
                } else {
                    this.setItem(slot, sourceStack.copyAndClear());
                    return;
                }
            }
        }
    }

    private void moveItemToOccupiedSlotsWithSameType(ItemStack sourceStack) {
        for (int slot = 0; slot < this.size; ++slot) {
            ItemStack targetStack = this.getItem(slot);
            if (ItemStack.isSameItemSameComponents(targetStack, sourceStack)) {
                this.moveItemsBetweenStacks(sourceStack, targetStack);
                if (sourceStack.isEmpty()) {
                    return;
                }
            }
        }
    }

    private void moveItemsBetweenStacks(ItemStack sourceStack, ItemStack targetStack) {
        int maxCount = this.getMaxStackSize(targetStack);
        int diff = Math.min(sourceStack.getCount(), maxCount - targetStack.getCount());
        if (diff > 0) {
            targetStack.grow(diff);
            sourceStack.shrink(diff);
            this.setChanged();
        }
    }

    public void fromItemList(ValueInput.TypedInputList<ItemStack> items) {
        this.clearContent();

        for (ItemStack stack : items) {
            this.addItem(stack);
        }
    }

    public void storeAsItemList(ValueOutput.TypedOutputList<ItemStack> output) {
        for (int i = 0; i < this.getContainerSize(); ++i) {
            ItemStack itemStack = this.getItem(i);
            if (!itemStack.isEmpty()) {
                output.add(itemStack);
            }
        }
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }
}
