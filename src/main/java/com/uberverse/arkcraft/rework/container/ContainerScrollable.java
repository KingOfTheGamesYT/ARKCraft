package com.uberverse.arkcraft.rework.container;

import com.uberverse.arkcraft.common.container.scrollable.IContainerScrollable;
import com.uberverse.arkcraft.common.container.scrollable.SlotScrolling;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public abstract class ContainerScrollable extends Container implements IContainerScrollable
{
	private int scrollingOffset;

	public ContainerScrollable()
	{
		super();
	}

	@Override
	public void initScrollableSlots()
	{
		for (int i = 0; i < getScrollableInventory().getSizeInventory(); i++)
		{
			this.addSlotToContainer(
					new SlotScrolling(getScrollableInventory(), i, getScrollableSlotsX() + i % getScrollableSlotsWidth() * getSlotSize(),
							getScrollableSlotsY() + i / getScrollableSlotsWidth() * getSlotSize(), this));
		}
	}

	@Override
	public int getScrollingOffset()
	{
		return this.scrollingOffset;
	}

	@Override
	public void scroll(float scroll)
	{
		int offset = (int) ((float) getMaxOffset() * scroll);

		int newScrollingOffset = offset;
		if (isValidOffset(newScrollingOffset))
		{
			scrollingOffset = newScrollingOffset;
			refreshScrollableSlotContents();
		}
	}

	@Override
	public int getScrollableSlotsCount()
	{
		return getScrollableSlotsWidth() * getScrollableSlotsHeight();
	}

	@Override
	public int getMaxOffset()
	{
		int offset = getTotalSlotsAmount() / getScrollableSlotsWidth() - getScrollableSlotsHeight();
		if (getTotalSlotsAmount() % getScrollableSlotsWidth() > 0) offset++;
		return offset;
	}

	@Override
	public float getRelativeScrollingOffset()
	{
		return (float) this.scrollingOffset / (float) getMaxOffset();
	}

	@Override
	public void refreshScrollableSlotContents()
	{
		for (int i = 0; i < inventoryItemStacks.size(); i++)
		{
			Slot slot = (Slot) inventorySlots.get(i);
			if (slot instanceof SlotScrolling)
			{
				inventoryItemStacks.set(i, slot.getStack());
			}
		}
	}

	private boolean isValidOffset(int offset)
	{
		int maxOffset = getMaxOffset();
		return canScroll() && offset >= 0 && offset <= maxOffset;
	}

	@Override
	public boolean canScroll()
	{
		return getVisibleSlotsAmount() < getTotalSlotsAmount();
	}

	@Override
	public int getVisibleSlotsAmount()
	{
		return getTotalSlotsAmount() < getScrollableSlotsCount() ? getTotalSlotsAmount() : getScrollableSlotsCount();
	}

	@Override
	public int getTotalSlotsAmount()
	{
		return getScrollableInventory().getSizeInventory();
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}

	@Override
	public int getSlotSize()
	{
		return 20;
	}
}
