package com.arkcraft.common.gen.resource;

import com.arkcraft.init.ARKCraftBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;

import java.util.Random;

public class RockResourceGenerator extends SurfaceResourceGenerator {
	public RockResourceGenerator() {
		super(2, 4, 2, 5, 0.5);
	}

	@Override
	public IBlockState getGeneratedState() {
		return ARKCraftBlocks.rockResource.getDefaultState();
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
						 IChunkProvider chunkProvider) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isValidPosition(World world, BlockPos pos) {
		return false;
	}
}
