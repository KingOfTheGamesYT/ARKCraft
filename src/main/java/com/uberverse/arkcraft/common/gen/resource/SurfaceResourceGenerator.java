package com.uberverse.arkcraft.common.gen.resource;

import java.util.Random;

import com.uberverse.arkcraft.common.block.resource.BlockARKResource;
import com.uberverse.arkcraft.common.block.resource.BlockSmallRockResource;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeDictionary;

public abstract class SurfaceResourceGenerator extends ClusterGenerator
{
	public SurfaceResourceGenerator(int minHeight, int maxHeight, int minWidth, int maxWidth, double chance)
	{
		super(minHeight, maxHeight, minWidth, maxWidth, chance);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator,
			IChunkProvider chunkProvider)
	{
		if (!shouldSpawn(random)) return;

		double max = 1;

		if (chance > 1) max = (chance - 1) * 10;

		for (int i = 0; i < max; i++)
		{
			int x = random.nextInt(16) + chunkX * 16;
			int y = 60;
			int z = random.nextInt(16) + chunkZ * 16;
			BlockPos position = new BlockPos(x, y, z);
			position = getSurfacePosition(world, position);
			// to prevent spawning on trees
			if (isValidPosition(world, position)) generate(world, random, position);
		}
	}

	@Override
	public boolean isValidPosition(World world, BlockPos pos)
	{
		IBlockState self = world.getBlockState(pos);
		IBlockState down = world.getBlockState(pos.down());
		boolean in = world.getWorldType() != WorldType.FLAT && !BiomeDictionary.isBiomeOfType(world
				.getBiomeGenForCoords(pos), BiomeDictionary.Type.WATER) && !world.getBlockState(pos.down()).getBlock()
						.isLeaves(world, pos.down()) && !(down.getBlock() instanceof BlockARKResource) && self
								.getBlock() != Blocks.water && self.getBlock() != Blocks.lava;
		if (!in) return in;
		for (int x = -maxWidth / 2; x < maxWidth / 2; x++)
			for (int z = -maxWidth / 2; z < maxWidth / 2; z++)
			{
				in = !(world.getBlockState(pos.add(x, 0, z)).getBlock() instanceof BlockARKResource || world
						.getBlockState(pos.add(x, 0, z)).getBlock() instanceof BlockSmallRockResource);
				if (!in) return in;
			}

		return in;
	}

	public static BlockPos getSurfacePosition(World world, BlockPos pos)
	{
		boolean check = false;
		while (!world.canBlockSeeSky(pos))
		{
			pos = pos.up();
			check = true;
		}
		if (!check) while (world.getBlockState(pos.down()).getBlock().isReplaceable(world, pos.down()) || !world
				.getBlockState(pos.down()).getBlock().isOpaqueCube())
			pos = pos.down();
		if (check) if (!world.getBlockState(pos).getBlock().isReplaceable(world, pos) && world.getBlockState(pos)
				.getBlock().isOpaqueCube()) pos = pos.up();
		return pos;
	}
}
