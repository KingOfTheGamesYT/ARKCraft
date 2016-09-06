package com.uberverse.arkcraft.common.block.crafter;

import com.uberverse.arkcraft.deprecated.TileInventoryCampfire;
import com.uberverse.arkcraft.util.Identifiable;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public abstract class BlockBurner extends BlockContainer implements Identifiable
{
	public static final PropertyBool BURNING = PropertyBool.create("burning");

	protected BlockBurner(Material materialIn)
	{
		super(materialIn);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof TileInventoryCampfire)
		{
			TileInventoryCampfire tileInventoryForge = (TileInventoryCampfire) tileEntity;
			return state.withProperty(BURNING, tileInventoryForge.isBurning());
		}
		return state;
	}
}
