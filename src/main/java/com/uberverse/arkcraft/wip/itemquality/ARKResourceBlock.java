package com.uberverse.arkcraft.wip.itemquality;

import com.uberverse.arkcraft.common.arkplayer.ARKPlayer;
import com.uberverse.arkcraft.common.entity.IArkLevelable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ARKResourceBlock extends Block implements IExperienceSource
{
	public ARKResourceBlock(Material materialIn)
	{
		super(materialIn);
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return false;
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		// TODO Calculate drops based on block resource type and player 
		super.onBlockHarvested(worldIn, pos, state, player);
		grantXP(ARKPlayer.get(player));
	}

	@Override
	public void grantXP(IArkLevelable leveling)
	{
		// TODO Auto-generated method stub

	}
}
