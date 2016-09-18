package com.uberverse.arkcraft.wip.oregen;

import java.util.Random;

import com.uberverse.arkcraft.init.ARKCraftBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeDictionary;

public class RockResourceGenerator extends ClusterGenerator
{
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator,
			IChunkProvider chunkProvider)
	{
		if (world.getWorldType() == WorldType.FLAT) return;
		int x = random.nextInt(16) + chunkX * 16;
		int y = 60;
		int z = random.nextInt(16) + chunkZ * 16;
		BlockPos position = new BlockPos(x, y, z);
		BiomeGenBase biome = world.getBiomeGenForCoords(position);
		if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.WATER)) return;
		while (!world.canSeeSky(position) && !world.getBlockState(position).getBlock().isReplaceable(world, position))
			position = position.up();
		while (world.getBlockState(position.down()).getBlock().isReplaceable(world, position))
			position = position.down();
		// to prevent spawning on trees
		if (!world.getBlockState(position.down()).getBlock().isLeaves(world, position.down())) generate(world, random,
				position);
	}

	@Override
	public boolean generate(World world, Random random, BlockPos position)
	{
		if (world.canSeeSky(position))
		{
			while (!world.getBlockState(position).getBlock().isReplaceable(world, position))
				position = position.up();
			generateCluster(world, random, position);
			return true;
		}
		return false;
	}

	private void generateCluster(World world, Random random, BlockPos pos)
	{
		final int height = 2 + random.nextInt(2), width = 2 + random.nextInt(3), depth = 2 + random.nextInt(3);

		// bottom layer
		for (int x = 0; x < width; x++)
			for (int z = 0; z < depth; z++)
			{
				if (((x == 0 || x == width - 1) && (z == 0 || z == depth - 1)) && random.nextInt(6) > 0) continue;
				set(world, pos.add(x, 0, z));
			}

		// all but top and bottom layer
		for (int x = 0; x < width; x++)
			for (int z = 0; z < depth; z++)
				for (int y = 1; y < height - 1; y++)
					set(world, pos.add(x, y, z));

		// top layer
		for (int x = 0; x < width; x++)
			for (int z = 0; z < depth; z++)
				if (random.nextInt(4) > 0) set(world, pos.add(x, height - 1, z));
	}

	@Override
	public IBlockState getGeneratedState()
	{
		return ARKCraftBlocks.rockResource.getDefaultState();
	}

}
