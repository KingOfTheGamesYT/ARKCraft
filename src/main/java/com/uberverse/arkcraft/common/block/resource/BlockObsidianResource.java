package com.uberverse.arkcraft.common.block.resource;

import java.util.Arrays;
import java.util.Collection;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.arkcraft.util.AbstractItemStack;

public class BlockObsidianResource extends BlockARKResource
{
	private static final AxisAlignedBB BB = new AxisAlignedBB(0, 0, 0, 1, 0.68f, 1);
	public BlockObsidianResource()
	{
		super(Material.ROCK);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullBlock(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public Collection<AbstractItemStack> getDrops()
	{
		return Arrays.asList(new AbstractItemStack(ARKCraftItems.obsidian, 10), new AbstractItemStack(
				ARKCraftItems.stone, 10));
	}
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BB;
	}
}
