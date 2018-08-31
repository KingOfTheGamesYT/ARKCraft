package com.arkcraft.common.creativetabs;

import com.arkcraft.init.ARKCraftItems;

import net.minecraft.item.Item;

public class ARKCreativeTab extends ARKTabBase
{
	public ARKCreativeTab()
	{
		super("tabARKCraft");
	}

	@Override
	public Item getTabIconItem()
	{
		return ARKCraftItems.info_book;
	}
}
