package com.uberverse.arkcraft.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ARKCraftBlock extends Block
{
	public ARKCraftBlock(Material materialIn, String unlocalizedName)
	{
		super(materialIn);
		setUnlocalizedName(unlocalizedName);
		// setCreativeTab(ARKCraft.tabARK);
	}
}
