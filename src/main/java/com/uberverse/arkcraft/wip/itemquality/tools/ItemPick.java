package com.uberverse.arkcraft.wip.itemquality.tools;

public abstract class ItemPick extends ItemToolBase
{
	public ItemPick(int baseDurability, double baseBreakSpeed, double baseDamage, ToolMaterial mat)
	{
		super(baseDurability, baseBreakSpeed, baseDamage, ItemType.TOOL, ToolType.PICK, mat);
	}
}
