package com.uberverse.arkcraft.common.gen.resource;

import java.util.Random;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeDictionary;

public abstract class SurfaceOreGenerator extends ClusterGenerator
{
	public SurfaceOreGenerator(int minHeight, int maxHeight, int minWidth, int maxWidth, int frequency)
	{
		super(minHeight, maxHeight, minWidth, maxWidth, frequency);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator,
			IChunkProvider chunkProvider)
	{
		if (!shouldSpawn(random)) return;
		int x = random.nextInt(16) + chunkX * 16;
		int y = 60;
		int z = random.nextInt(16) + chunkZ * 16;
		BlockPos position = new BlockPos(x, y, z);
		position = getSurfacePosition(position, world);
		// to prevent spawning on trees
		if (isValidPosition(world, position)) generate(world, random, position);
	}

	public boolean isValidPosition(World world, BlockPos pos)
	{
		return world.getWorldType() != WorldType.FLAT && !BiomeDictionary.isBiomeOfType(world.getBiomeGenForCoords(pos),
				BiomeDictionary.Type.WATER) && !world.getBlockState(pos.down()).getBlock().isLeaves(world, pos.down());
	}

	public static BlockPos getSurfacePosition(BlockPos pos, World world)
	{
		boolean check = false;
		while (!world.canBlockSeeSky(pos))
		{
			pos = pos.up();
			check = true;
		}
		if (!check) while (world.getBlockState(pos.down()).getBlock().isReplaceable(world, pos) || !world.getBlockState(
				pos.down()).getBlock().isOpaqueCube())
			pos = pos.down();
		if (check) if (!world.getBlockState(pos).getBlock().isReplaceable(world, pos) && world.getBlockState(pos)
				.getBlock().isOpaqueCube()) pos = pos.up();
		return pos;
	}
}
