package com.uberverse.arkcraft.wip.itemquality;

import net.minecraft.init.Blocks;

public class ItemStonePickaxe extends ItemToolBase
{
	public ItemStonePickaxe()
	{
		super(100, 4, 2, ItemType.TOOL, ToolType.PICK, Blocks.stone, Blocks.log, Blocks.log2);
	}
}
