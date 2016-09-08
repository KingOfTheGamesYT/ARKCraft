package com.uberverse.arkcraft;

import com.uberverse.arkcraft.init.ARKCraftItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ARKCreativeTab extends CreativeTabs
{
	public static ARKCreativeTab INSTANCE = new ARKCreativeTab();

	public ARKCreativeTab()
	{
		super("tabARKCraft");
		setBackgroundImageName("arkcraft.png");
	}

	@Override
	public Item getTabIconItem()
	{
		return ARKCraftItems.info_book;
	}

	@Override
	public boolean hasSearchBar()
	{
		return true;
	}
}
