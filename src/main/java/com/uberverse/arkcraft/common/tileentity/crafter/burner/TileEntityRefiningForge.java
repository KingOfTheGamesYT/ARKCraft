package com.uberverse.arkcraft.common.tileentity.crafter.burner;

import com.uberverse.arkcraft.common.burner.BurnerManager.BurnerType;
import com.uberverse.arkcraft.init.ARKCraftItems;

import net.minecraft.item.ItemStack;

/**
 * @author Lewis_McReu
 */
public class TileEntityRefiningForge extends TileEntityBurner
{
	// TODO annotations!
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return stack != null ? stack.getItem() == ARKCraftItems.oil || stack.getItem() == ARKCraftItems.hide || stack
				.getItem() == ARKCraftItems.metal || super.isItemValidForSlot(index, stack) : false;
	}

	@Override
	public int getSizeInventory()
	{
		return 8;
	}

	@Override
	public BurnerType getBurnerType()
	{
		return BurnerType.REFINING_FORGE;
	}
}
