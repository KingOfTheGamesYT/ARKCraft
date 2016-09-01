package com.uberverse.arkcraft.common.container.scrollable;

import net.minecraft.inventory.IInventory;

public interface IContainerScrollable
{
	public int getScrollingOffset();

	public void scroll(float scroll);
	
	public void scroll(int amount);

	public int getScrollableSlotsWidth();

	public int getScrollableSlotsHeight();

	public int getScrollableSlotsCount();

	public int getMaxOffset();

	public float getRelativeScrollingOffset();

	public int getVisibleSlotsAmount();

	public int getTotalSlotsAmount();

	public boolean canScroll();

	public void initScrollableSlots();

	public int getScrollableSlotsX();

	public int getScrollableSlotsY();

	public int getSlotSize();

	public void refreshScrollableSlotContents();

	public IInventory getScrollableInventory();
}
