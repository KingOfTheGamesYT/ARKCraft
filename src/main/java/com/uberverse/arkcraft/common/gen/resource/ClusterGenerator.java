package com.uberverse.arkcraft.common.gen.resource;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public abstract class ClusterGenerator extends WorldGenerator implements IWorldGenerator
{
	protected final int minHeight, maxHeight, minWidth, maxWidth, frequency;

	public ClusterGenerator(int minHeight, int maxHeight, int minWidth, int maxWidth, int frequency)
	{
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
		this.minWidth = minWidth;
		this.maxWidth = maxWidth;
		this.frequency = frequency;
	}

	public boolean shouldSpawn(Random random)
	{
		return random.nextInt(frequency) == 0;
	}

	/**
	 * This method should find a valid location to create the cluster, then call
	 * the unconditional generation method
	 */
	public abstract void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator,
			IChunkProvider chunkProvider);

	/**
	 * Used as an unconditional generation method! No checks happen here
	 */
	@Override
	public boolean generate(World world, Random random, BlockPos position)
	{
		generateCluster(world, random, position);
		return true;
	}

	private final int getRandomizedHeight(Random random)
	{
		return maxHeight != minHeight ? random.nextInt(maxHeight - minHeight) : 0;
	}

	private final int getRandomizedWidth(Random random)
	{
		return maxWidth != minWidth ? random.nextInt(maxWidth - minWidth) : 0;
	}

	private void generateCluster(World world, Random random, BlockPos pos)
	{
		final int height = minHeight + getRandomizedHeight(random), width = minWidth + getRandomizedWidth(random),
				depth = minWidth + getRandomizedWidth(random);

		// bottom layer
		for (int x = 0; x < width; x++)
			for (int z = 0; z < depth; z++)
			{
				if (((x == 0 || x == width - 1) && (z == 0 || z == depth - 1)) && random.nextInt(6) > 0) continue;
				set(world, pos.add(x, 0, z));
			}
		if (height == 1) return;

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

	protected void set(World world, BlockPos pos)
	{
		IBlockState s = world.getBlockState(pos.down());
		if (s.getBlock().isReplaceable(world, pos.down()) || !s.getBlock().isOpaqueCube()) return;
		world.setBlockState(pos, getGeneratedState());
	}

	public abstract IBlockState getGeneratedState();
}
