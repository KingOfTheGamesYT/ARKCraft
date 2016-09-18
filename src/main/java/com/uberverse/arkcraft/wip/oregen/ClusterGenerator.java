package com.uberverse.arkcraft.wip.oregen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public abstract class ClusterGenerator extends WorldGenerator implements IWorldGenerator
{
	protected void set(World world, BlockPos pos)
	{
		IBlockState s = world.getBlockState(pos.down());
		if (s.getBlock().isReplaceable(world, pos.down()) || !s.getBlock().isOpaqueCube()) return;
		world.setBlockState(pos, getGeneratedState());
	}

	public abstract IBlockState getGeneratedState();
}
