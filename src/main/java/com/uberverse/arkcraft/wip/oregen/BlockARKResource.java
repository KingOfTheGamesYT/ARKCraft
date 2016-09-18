package com.uberverse.arkcraft.wip.oregen;

import com.uberverse.arkcraft.common.entity.IArkLevelable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class BlockARKResource extends Block implements IExperienceSource
{
	public BlockARKResource(Material materialIn)
	{
		super(materialIn);
		setHardness(1.5f);
		setResistance(10f);
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return false;
	}

	@Override
	public void grantXP(IArkLevelable leveling)
	{
		leveling.addXP(0.4);
	}

	public abstract void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player);
}
