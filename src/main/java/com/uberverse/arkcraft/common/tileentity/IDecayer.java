package com.uberverse.arkcraft.common.tileentity;

import com.uberverse.arkcraft.common.item.IDecayable;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * @author Lewis_McReu
 */
public interface IDecayer
{
	public static void updateDecayer(IDecayer decayer)
	{
		for (int i = 0; i < decayer.getIInventory().getSizeInventory(); i++)
		{
			ItemStack s = decayer.getIInventory().getStackInSlot(i);
			if (s != null && s.getItem() instanceof IDecayable) ((IDecayable) s.getItem()).decayTick(decayer
					.getIInventory(), i, decayer.getDecayModifier(s), s);;
		}
	}

	default double getDecayModifier(ItemStack stack)
	{
		return 1;
	}

	IInventory getIInventory();
}
