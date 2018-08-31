package com.arkcraft.common.item.melee;

import com.arkcraft.ARKCraft;

import net.minecraft.item.ItemSword;

public class ItemPike extends ItemSword
{
	public ItemPike(ToolMaterial m)
	{
		super(m);
		this.setCreativeTab(ARKCraft.tabARK);
		this.setMaxStackSize(1);
		this.setFull3D();
	}
}