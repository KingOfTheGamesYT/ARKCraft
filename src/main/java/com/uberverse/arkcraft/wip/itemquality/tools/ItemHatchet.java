package com.uberverse.arkcraft.wip.itemquality.tools;

public abstract class ItemHatchet extends ItemToolBase
{
	public ItemHatchet(int baseDurability, double baseBreakSpeed, double baseDamage, ToolMaterial mat)
	{
		super(baseDurability, baseBreakSpeed, baseDamage, ItemType.TOOL, ToolType.HATCHET, mat);
	}
}
