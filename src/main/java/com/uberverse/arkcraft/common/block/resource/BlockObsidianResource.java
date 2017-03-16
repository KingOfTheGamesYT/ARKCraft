package com.uberverse.arkcraft.common.block.resource;

import java.util.Arrays;
import java.util.Collection;

import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.arkcraft.util.AbstractItemStack;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public class BlockObsidianResource extends BlockARKResource
{
	public BlockObsidianResource()
	{
		super(Material.ROCK);
		setBlockBounds(0, 0, 0, 1, 0.68f, 1);
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
}
