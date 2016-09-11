package com.uberverse.arkcraft.common.item;

import com.uberverse.arkcraft.ARKCraft;

import net.minecraft.item.Item;

public class ARKCraftItem extends Item
{
	public ARKCraftItem()
	{
		super();
		this.setCreativeTab(ARKCraft.tabARK);
	}

	public ARKCraftItem(String name)
	{
		this();
		this.setUnlocalizedName(name);
	}
}
