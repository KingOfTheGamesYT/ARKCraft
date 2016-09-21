package com.uberverse.arkcraft.deprecated;

import java.util.Set;

import com.uberverse.arkcraft.common.item.tools.ARKCraftTool;
import com.uberverse.arkcraft.common.item.tools.ToolType;

import net.minecraft.item.Item.ToolMaterial;

public class ARKCraftHatchet extends ARKCraftTool
{

	public ARKCraftHatchet(float attackDamage, ToolMaterial material, Set effectiveBlocks)
	{
		super(attackDamage, material, effectiveBlocks, ToolType.HATCHET);
	}
}
