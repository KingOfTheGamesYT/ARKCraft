package com.uberverse.arkcraft.common.block.unused;

import com.uberverse.arkcraft.ARKCraft;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ARKCraftBlock extends Block
{
	public ARKCraftBlock(Material materialIn)
	{
		super(materialIn);
		setCreativeTab(ARKCraft.tabARK);
	}
}
