package com.arkcraft.common.block.resource;

import java.util.Arrays;
import java.util.Collection;

import com.arkcraft.init.ARKCraftItems;
import com.arkcraft.util.AbstractItemStack;

import net.minecraft.block.material.Material;

public class BlockMetalResource extends BlockARKResource
{
	public BlockMetalResource()
	{
		super(Material.ROCK);
	}

	@Override
	public Collection<AbstractItemStack> getDrops()
	{
		return Arrays.asList(new AbstractItemStack(ARKCraftItems.metal, 10), new AbstractItemStack(ARKCraftItems.stone,
				10));
	}
}
