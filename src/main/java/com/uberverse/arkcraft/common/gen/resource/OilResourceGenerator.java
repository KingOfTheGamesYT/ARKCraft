package com.uberverse.arkcraft.common.gen.resource;

import com.uberverse.arkcraft.init.ARKCraftBlocks;

import net.minecraft.block.state.IBlockState;

public class OilResourceGenerator extends OceanResourceGenerator
{
	public OilResourceGenerator()
	{
		super(1, 2, 1, 2, 0.5);
	}

	@Override
	public IBlockState getGeneratedState()
	{
		return ARKCraftBlocks.oilResource.getDefaultState();
	}
}
