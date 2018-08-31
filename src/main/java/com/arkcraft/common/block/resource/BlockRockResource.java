package com.arkcraft.common.block.resource;

import java.util.Arrays;
import java.util.Collection;

import com.arkcraft.init.ARKCraftItems;
import com.arkcraft.util.AbstractItemStack;
import com.arkcraft.util.AbstractItemStack.ChancingAbstractItemStack;

import net.minecraft.block.material.Material;

public class BlockRockResource extends BlockARKResource
{
	public BlockRockResource()
	{
		super(Material.ROCK);
	}

	@Override
	public Collection<AbstractItemStack> getDrops()
	{
		return Arrays.asList(new AbstractItemStack(ARKCraftItems.stone, 4), new ChancingAbstractItemStack(
				ARKCraftItems.metal, 0.25), new AbstractItemStack(ARKCraftItems.flint, 3));
	}
}
