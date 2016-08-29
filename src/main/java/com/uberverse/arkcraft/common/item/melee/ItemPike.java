package com.uberverse.arkcraft.common.item.melee;

import net.minecraft.item.ItemSword;

public class ItemPike extends ItemSword
{
	public ItemPike(ToolMaterial m)
	{
		super(m);
		this.setMaxStackSize(1);
		this.setFull3D();
	}
}