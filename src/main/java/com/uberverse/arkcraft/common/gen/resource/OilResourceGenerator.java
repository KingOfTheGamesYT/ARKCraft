package com.uberverse.arkcraft.common.gen.resource;

import java.util.Random;

import com.uberverse.arkcraft.init.ARKCraftBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;

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

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
	}
}
