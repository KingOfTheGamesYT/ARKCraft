package com.uberverse.arkcraft.common.item.tools;

import java.util.Set;

import net.minecraft.item.Item.ToolMaterial;

public class ARKCraftHatchet extends ARKCraftTool{
	
	public ARKCraftHatchet(String name, float attackDamage, ToolMaterial material, Set effectiveBlocks, ToolType toolType)
	{
		super(name, attackDamage,material, effectiveBlocks, ToolType.HATCHET);
	}
}



