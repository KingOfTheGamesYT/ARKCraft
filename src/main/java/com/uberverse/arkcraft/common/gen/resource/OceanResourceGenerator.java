package com.uberverse.arkcraft.common.gen.resource;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeDictionary;

public abstract class OceanResourceGenerator extends ClusterGenerator
{
	public OceanResourceGenerator(int minHeight, int maxHeight, int minWidth, int maxWidth, double chance)
	{
		super(minHeight, maxHeight, minWidth, maxWidth, chance);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator,
			IChunkProvider chunkProvider)
	{
		if (!shouldSpawn(random)) return;
		int x = random.nextInt(16) + chunkX * 16;
		int y = 1;
		int z = random.nextInt(16) + chunkZ * 16;
		BlockPos position = new BlockPos(x, y, z);
		position = getOceanFloorPosition(world, position);
		// to prevent spawning on trees
		if (isValidPosition(world, position)) generate(world, random, position);
	}

	@Override
	public boolean isValidPosition(World world, BlockPos pos)
	{
		IBlockState self = world.getBlockState(pos);
		return pos.getY() < 50 && (self.getBlock() == Blocks.water || self.getBlock() == Blocks.flowing_water)
				&& BiomeDictionary.isBiomeOfType(world.getBiomeGenForCoords(pos), BiomeDictionary.Type.OCEAN);
	}

	public static BlockPos getOceanFloorPosition(World world, BlockPos pos)
	{
		while (world.getBlockState(pos).getBlock() != Blocks.water)
		{
			pos = pos.up();
			if (pos.getY() > 50) break;
		}
		return pos;
	}
}
