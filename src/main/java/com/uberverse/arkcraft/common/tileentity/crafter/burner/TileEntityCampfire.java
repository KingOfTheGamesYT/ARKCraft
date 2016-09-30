package com.uberverse.arkcraft.common.tileentity.crafter.burner;

import com.uberverse.arkcraft.common.burner.BurnerManager.BurnerType;
import com.uberverse.arkcraft.init.ARKCraftItems;

import net.minecraft.item.ItemStack;

/**
 * @author Lewis_McReu
 */
public class TileEntityCampfire extends TileEntityBurner
{
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return stack != null ? stack.getItem() == ARKCraftItems.meat_raw || stack
				.getItem() == ARKCraftItems.primemeat_raw || super.isItemValidForSlot(index, stack) : false;
	}

	@Override
	public int getSizeInventory()
	{
		return 4;
	}

	@Override
	public BurnerType getBurnerType()
	{
		return BurnerType.CAMPFIRE;
	}
}
