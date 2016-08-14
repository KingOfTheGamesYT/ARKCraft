package com.uberverse.arkcraft.common.container.scrollable;

public interface IContainerScrollable
{
	public int getScrollingOffset();

	public void scroll(int offset);

	public int getScrollableSlotsWidth();

	public int getScrollableSlotsHeight();

	public int getScrollableSlotsCount();

	public int getRequiredSlotsCount();

	public int getMaxOffset();

	public double getRelativeScrollingOffset();
}
