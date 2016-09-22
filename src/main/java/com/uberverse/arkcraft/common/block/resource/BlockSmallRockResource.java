package com.uberverse.arkcraft.common.block.resource;

import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.arkcraft.util.InventoryUtil;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockSmallRockResource extends Block
{
	public BlockSmallRockResource()
	{
		super(Material.rock);
		setHardness(1.5f);
		setResistance(10f);
		setBlockBounds(0, 0, 0, 1, 0.2f, 1);
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return false;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (playerIn.getHeldItem() == null)
		{
			InventoryUtil.addOrDrop(new ItemStack(ARKCraftItems.stone), playerIn.inventory, pos, worldIn);
			worldIn.setBlockToAir(pos);
			return true;
		}
		return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean isFullCube()
	{
		return false;
	}
}
