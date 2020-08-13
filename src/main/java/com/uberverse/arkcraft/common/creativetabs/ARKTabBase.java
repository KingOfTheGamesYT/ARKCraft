package com.uberverse.arkcraft.common.creativetabs;

import net.minecraft.creativetab.CreativeTabs;

public abstract class ARKTabBase extends CreativeTabs
{

	public ARKTabBase(String label)
	{
		super(label);
		setBackgroundImageName("arkcraft_logo.png");
	}

	@Override
	public boolean hasSearchBar()
	{
		return true;
	}
}
