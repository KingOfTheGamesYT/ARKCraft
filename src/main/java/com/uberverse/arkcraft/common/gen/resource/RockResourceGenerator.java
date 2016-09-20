package com.uberverse.arkcraft.common.gen.resource;

import com.uberverse.arkcraft.init.ARKCraftBlocks;

import net.minecraft.block.state.IBlockState;

public class RockResourceGenerator extends SurfaceOreGenerator
{
	public RockResourceGenerator()
	{
		super(2, 4, 2, 5, 4);
	}

	@Override
	public IBlockState getGeneratedState()
	{
		return ARKCraftBlocks.rockResource.getDefaultState();
	}
}
