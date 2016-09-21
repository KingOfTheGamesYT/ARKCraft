package com.uberverse.arkcraft.common.block.crafter;

import com.uberverse.arkcraft.common.burner.IBurner;
import com.uberverse.arkcraft.util.Identifiable;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockBurner extends BlockContainer implements Identifiable
{
	public static final PropertyBool BURNING = PropertyBool.create("burning");

	protected BlockBurner(Material materialIn)
	{
		super(materialIn);
		setDefaultState(getDefaultState().withProperty(BURNING, false));
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof IInventory)
		{
			InventoryHelper.dropInventoryItems(worldIn, pos,
					(IInventory) tileEntity);
		}
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn,
			BlockPos pos)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof IBurner)
		{
			IBurner burner = (IBurner) tileEntity;
			return state.withProperty(BURNING, burner.isBurning());
		}
		return state;
	}

	@Override
	public int getLightValue(IBlockAccess world, BlockPos pos)
	{
		int lightValue = 0;
		IBlockState blockState = getActualState(getDefaultState(), world, pos);
		boolean burning = (Boolean) blockState.getValue(BURNING);

		if (burning)
		{
			lightValue = 15;
		}
		else
		{
			lightValue = 0;
		}
		lightValue = MathHelper.clamp_int(lightValue, 0, 15);
		return lightValue;
	}

	@Override
	public int getRenderType()
	{
		return 3;
	}
}
