package com.uberverse.arkcraft.common.item.tools;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public class ItemMetalHatchet extends ARKCraftHatchet
{

	public ItemMetalHatchet(String name, float attackDamage, Set effectiveBlocks, int durability, int efficiency) 
	{
		super(name, attackDamage, effectiveBlocks,durability,efficiency);
	}
}

