package com.uberverse.arkcraft.common.item;

import com.uberverse.arkcraft.ARKCraft;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ARKCraftUtility extends Item
{

	public ARKCraftUtility()
	{
		this.setMaxStackSize(1);
		this.setCreativeTab(ARKCraft.tabARK);
	}
	
	// seconds that this fertilizer will grow a crop
	public static int getItemGrowTime(ItemStack itemStack)
	{
		return itemStack.getMaxDamage() - itemStack.getItemDamage();
	}

}
