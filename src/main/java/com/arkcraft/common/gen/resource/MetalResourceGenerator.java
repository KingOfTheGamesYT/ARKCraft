package com.arkcraft.common.gen.resource;

import com.arkcraft.init.ARKCraftBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;

import java.util.Random;

public class MetalResourceGenerator extends SurfaceResourceGenerator {
	public MetalResourceGenerator() {
		super(1, 2, 2, 4, 0.8);
	}

	@Override
	public boolean isValidPosition(World world, BlockPos pos) {
		return BiomeDictionary.hasType(world.getBiomeForCoordsBody(pos), BiomeDictionary.Type.MOUNTAIN)
				&& super.isValidPosition(world, pos);
	}

	@Override
	public IBlockState getGeneratedState() {
		return ARKCraftBlocks.metalResource.getDefaultState();
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
						 IChunkProvider chunkProvider) {
	}
}
