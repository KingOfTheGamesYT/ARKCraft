package com.uberverse.arkcraft.common.block.resource;

import java.util.Arrays;
import java.util.Collection;

import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.arkcraft.util.AbstractItemStack;

import net.minecraft.block.material.Material;

public class BlockObsidianResource extends BlockARKResource
{
	public BlockObsidianResource()
	{
		super(Material.rock);
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean isFullBlock()
	{
		return false;
	}

	@Override
	public boolean isFullCube()
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
