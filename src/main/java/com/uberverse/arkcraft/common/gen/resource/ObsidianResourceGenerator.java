package com.uberverse.arkcraft.common.gen.resource;

import java.util.Random;

import com.uberverse.arkcraft.init.ARKCraftBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeDictionary;

public class ObsidianResourceGenerator extends SurfaceResourceGenerator
{
	public ObsidianResourceGenerator()
	{
		super(1, 1, 1, 2, 0.8);
	}

	@Override
	public boolean isValidPosition(World world, BlockPos pos)
	{
		return BiomeDictionary.isBiomeOfType(world.getBiomeForCoordsBody(pos), BiomeDictionary.Type.MOUNTAIN)
				&& super.isValidPosition(world, pos) && pos.getY() > 100;
	}

	@Override
	public IBlockState getGeneratedState()
	{
		return ARKCraftBlocks.obsidianResource.getDefaultState();
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {		
	}
}
