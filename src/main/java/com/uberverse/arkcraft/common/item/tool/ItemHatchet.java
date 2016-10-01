package com.uberverse.arkcraft.common.item.tool;

public abstract class ItemHatchet extends ItemToolBase
{
	public ItemHatchet(int baseDurability, double baseBreakSpeed, double baseDamage, ToolMaterial mat)
	{
		super(baseDurability, baseBreakSpeed, baseDamage, ItemType.TOOL, ToolType.HATCHET, mat);
	}
}
