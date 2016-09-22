package com.uberverse.arkcraft.common.tileentity;

import com.uberverse.arkcraft.common.item.IDecayable;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface IDecayer
{
	default void update()
	{
		for (int i = 0; i < getIInventory().getSizeInventory(); i++)
		{
			ItemStack s = getIInventory().getStackInSlot(i);
			if (s != null && s.getItem() instanceof IDecayable) ((IDecayable) s.getItem()).decayTick(getIInventory(), i,
					getDecayModifier(), s);
		}
	}

	double getDecayModifier();

	IInventory getIInventory();
}
