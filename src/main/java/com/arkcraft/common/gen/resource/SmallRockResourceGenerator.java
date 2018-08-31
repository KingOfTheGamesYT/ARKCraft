package com.arkcraft.common.gen.resource;

import com.arkcraft.init.ARKCraftBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;

import java.util.Random;

public class SmallRockResourceGenerator extends SurfaceResourceGenerator {
	public SmallRockResourceGenerator() {
		super(1, 1, 1, 1, 3);
	}

	@Override
	public IBlockState getGeneratedState() {
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
