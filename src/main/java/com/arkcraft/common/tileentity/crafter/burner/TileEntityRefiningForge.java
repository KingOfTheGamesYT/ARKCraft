package com.arkcraft.common.tileentity.crafter.burner;

import com.arkcraft.common.burner.BurnerManager;
import com.arkcraft.init.ARKCraftItems;

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
	public BurnerManager.BurnerType getBurnerType()
	{
		return BurnerManager.BurnerType.REFINING_FORGE;
	}
	
	@Override
	public String getOnSoundName()
	{
		return "arkcraft:on";
	}
	
	@Override
	public String getOffSoundName()
	{
		return "arkcraft:off";
	}
	
	@Override
	public String getLightSoundName()
	{
		return "arkcraft:light";
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return null;
	}
}
