package com.uberverse.arkcraft.common.gen.resource;

import com.uberverse.arkcraft.init.ARKCraftBlocks;

import net.minecraft.block.state.IBlockState;

public class SmallRockResourceGenerator extends SurfaceResourceGenerator
{
	public SmallRockResourceGenerator()
	{
		super(1, 1, 1, 1, 3);
	}

	@Override
	public IBlockState getGeneratedState()
	{
		return ARKCraftBlocks.smallRockResource.getDefaultState();
	}
}
