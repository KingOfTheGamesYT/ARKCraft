package com.uberverse.arkcraft.common.item.tools;

import java.util.Set;

import net.minecraft.item.ItemTool;

public class ARKCraftTool extends ItemTool{

	public ARKCraftTool(float attackDamage, ToolMaterial material, Set effectiveBlocks) {
		super(attackDamage, material, effectiveBlocks);
	}
	
	public ARKCraftTool(String name, float attackDamage, ToolMaterial material, Set effectiveBlocks){
		super(attackDamage, material, effectiveBlocks);
		this.setUnlocalizedName(name);
	}
}
