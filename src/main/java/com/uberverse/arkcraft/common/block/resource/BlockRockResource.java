package com.uberverse.arkcraft.common.block.resource;

import java.util.Arrays;
import java.util.Collection;

import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.arkcraft.util.AbstractItemStack;
import com.uberverse.arkcraft.util.AbstractItemStack.ChancingAbstractItemStack;

import net.minecraft.block.material.Material;

public class BlockRockResource extends BlockARKResource
{
	public BlockRockResource()
	{
		super(Material.rock);
	}

	@Override
	public Collection<AbstractItemStack> getDrops()
	{
		return Arrays.asList(new AbstractItemStack(ARKCraftItems.stone, 8),
				new ChancingAbstractItemStack(ARKCraftItems.metal, 0.25),
				new AbstractItemStack(ARKCraftItems.flint, 3));
	}
}
