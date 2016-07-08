package com.uberverse.arkcraft.common.item.tools;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public class ItemMetalHatchet extends ARKCraftHatchet
{

	public ItemMetalHatchet(String name, float attackDamage, ToolMaterial material, Set effectiveBlocks, ToolType toolType) 
	{
		super(name, attackDamage, material, effectiveBlocks, toolType);
	}
}

