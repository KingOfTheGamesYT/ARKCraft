package com.uberverse.arkcraft.rework.engram;

import com.uberverse.arkcraft.rework.engram.EngramManager.Engram;

import net.minecraft.inventory.IInventory;

public class CraftingOrder
{
	private Engram engram;
	private int count;

	public CraftingOrder(Engram engram, int count)
	{
		this.engram = engram;
		this.count = count;
	}

	public CraftingOrder(Engram engram)
	{
		this(engram, 1);
	}

	public int getCount()
	{
		return count;
	}

	public Engram getEngram()
	{
		return engram;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public void decreaseCount(int decrease)
	{
		count -= decrease;
	}

	public void increaseCount(int increase)
	{
		count += increase;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof CraftingOrder) return ((CraftingOrder) obj).getEngram().equals(engram);
		else if (obj instanceof Engram) return ((Engram) obj).equals(engram);
		return false;
	}

	public void setEngram(Engram engram)
	{
		this.engram = engram;
	}

	public boolean canCraft(IInventory inventory, int amount)
	{
		return engram.canCraft(inventory, amount);
	}

	public boolean canCraft(IInventory inventory)
	{
		return engram.canCraft(inventory);
	}
}