package com.uberverse.arkcraft.common.item.tools;

import java.util.Set;

import net.minecraft.item.Item.ToolMaterial;

public class ARKCraftHatchet extends ARKCraftTool{
	
	public ARKCraftHatchet(float attackDamage, ToolMaterial material, Set effectiveBlocks)
	{
		super(attackDamage, material, effectiveBlocks, ToolType.HATCHET);
	}
}



