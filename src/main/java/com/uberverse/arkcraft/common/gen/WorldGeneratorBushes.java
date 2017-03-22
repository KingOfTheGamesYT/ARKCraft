package com.uberverse.arkcraft.common.gen;

import java.util.Random;

import net.minecraft.block.BlockTallGrass;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;

import net.minecraftforge.fml.common.IWorldGenerator;

import com.uberverse.arkcraft.init.ARKCraftBlocks;

public class WorldGeneratorBushes implements IWorldGenerator
{
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		for (int i = 0; i < 25; i++)
		{
			int x = chunkX * 16 + random.nextInt(16);
			int z = chunkZ * 16 + random.nextInt(16);

			BlockPos pos = world.getHeight(new BlockPos(x, 0, z));

			if (world.getBlockState(pos).getBlock() instanceof BlockTallGrass)
			{
				world.setBlockState(pos, ARKCraftBlocks.berryBush.getDefaultState());
			}
		}
	}
}
