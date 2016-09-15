package com.uberverse.arkcraft.wip.oregen;

import java.util.List;

import com.uberverse.arkcraft.wip.itemquality.ItemToolBase;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class ARKResourceBlock extends Block implements IExperienceSource
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
	public float getPlayerRelativeBlockHardness(EntityPlayer playerIn, World worldIn, BlockPos pos)
	{
		ItemStack s = playerIn.getHeldItem();
		if (s != null && s.getItem() instanceof ItemToolBase) return super.getPlayerRelativeBlockHardness(playerIn,
				worldIn, pos);
		else return 50f;
	}

	@Override
	public abstract List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune);
}
