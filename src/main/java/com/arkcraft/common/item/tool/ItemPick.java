package com.arkcraft.common.item.tool;

import com.arkcraft.common.item.ItemQualitable;

public abstract class ItemPick extends ItemToolBase
{
	public ItemPick(int baseDurability, double baseBreakSpeed, double baseDamage, ToolMaterial mat)
	{
		super(baseDurability, baseBreakSpeed, baseDamage, ItemQualitable.ItemType.TOOL, ToolType.PICK, mat);
	}
}
