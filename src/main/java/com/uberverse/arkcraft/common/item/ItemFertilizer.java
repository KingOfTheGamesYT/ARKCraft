package com.uberverse.arkcraft.common.item;

public class ItemFertilizer extends ARKCraftItem
{
	private final long fertilizingTime;

	public ItemFertilizer(long fertilizingTime)
	{
		super();
		this.fertilizingTime = fertilizingTime;
	}

	public long getFertilizingTime()
	{
		return fertilizingTime;
	}
}
