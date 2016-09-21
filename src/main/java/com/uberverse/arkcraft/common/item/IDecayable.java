package com.uberverse.arkcraft.common.item;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface IDecayable
{
	void decayTick(IInventory inventory, int itemSlot, double decayModifier,
			ItemStack stack);
}
