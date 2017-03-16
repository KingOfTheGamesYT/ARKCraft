package com.uberverse.arkcraft.common.gen.resource;

import java.util.Random;

import com.uberverse.arkcraft.init.ARKCraftBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;

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

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
	}

	@Override
	public boolean isValidPosition(World world, BlockPos pos) {
		return false;
	}
}
