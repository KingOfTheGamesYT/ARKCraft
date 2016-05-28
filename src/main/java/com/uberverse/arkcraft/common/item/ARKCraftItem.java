package com.uberverse.arkcraft.common.item;

import com.uberverse.arkcraft.ARKCraft;

import net.minecraft.item.Item;

public class ARKCraftItem extends Item
{
	public ARKCraftItem(String name)
	{
		this.setUnlocalizedName(name);
		this.setCreativeTab(ARKCraft.tabARK);
	}
}
