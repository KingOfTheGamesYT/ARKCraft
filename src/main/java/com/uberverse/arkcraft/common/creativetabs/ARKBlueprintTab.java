package com.uberverse.arkcraft.common.creativetabs;

import com.uberverse.arkcraft.init.ARKCraftItems;

import net.minecraft.item.Item;

public class ARKBlueprintTab extends ARKTabBase
{
	public ARKBlueprintTab()
	{
		super("tabARKBlueprints");
	}

	@Override
	public Item getTabIconItem()
	{
		return ARKCraftItems.tabItem;
	}
}
