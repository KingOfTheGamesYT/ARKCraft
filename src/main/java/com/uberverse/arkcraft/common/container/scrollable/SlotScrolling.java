package com.uberverse.arkcraft.common.container.scrollable;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotScrolling extends Slot
{
	private IContainerScrollable container;

	public SlotScrolling(IInventory inventoryIn, int index, int xPosition, int yPosition, IContainerScrollable container)
	{
		super(inventoryIn, index, xPosition, yPosition);
		this.container = container;
	}

	public IContainerScrollable getContainer()
	{
		return container;
	}

	@Override
	public int getSlotIndex()
	{
		return super.getSlotIndex() + (container.getScrollingOffset() * container.getScrollableSlotsWidth());
	}

	@Override
	public ItemStack getStack()
	{
		int index = getSlotIndex();
		return index < container.getTotalSlotsAmount() ? this.inventory.getStackInSlot(index) : null;
	}

	@Override
	public void putStack(ItemStack stack)
	{
		if (getSlotIndex() < container.getTotalSlotsAmount())
		{
			inventory.setInventorySlotContents(getSlotIndex(), stack);
			onSlotChanged();
		}
	}

	@Override
	public ItemStack decrStackSize(int amount)
	{
		return this.inventory.decrStackSize(getSlotIndex(), amount);
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return getSlotIndex() < container.getTotalSlotsAmount();
	}

	public void refresh()
	{
		putStack(getStack());
	}
}
