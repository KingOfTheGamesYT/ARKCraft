package com.uberverse.arkcraft.common.block;

import java.util.Random;

import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLight extends BlockAir
{
	public static final PropertyInteger TICKS = PropertyInteger.create("ticks", 0, 2);

	public BlockLight()
	{
		super();
		this.setLightLevel(0.8F);
		this.setBlockBounds(0, 0, 0, 0, 0, 0);
		setDefaultState(getDefaultState().withProperty(TICKS, 0));
		setTickRandomly(true);
	}

	@Override
	public boolean isAir(IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return true;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		System.out.println("tick " + worldIn.getWorldTime());
		int ticks = (int) worldIn.getBlockState(pos).getValue(TICKS);
		if (ticks < 2)
		{
			System.out.println("update tick");
			System.out.println(Integer.valueOf(ticks));
			worldIn.setBlockState(pos, state.withProperty(TICKS, ticks++));
			worldIn.markBlockForUpdate(pos);

			System.out.println(worldIn.getBlockState(pos).getValue(TICKS));
		}
		else
		{
			System.out.println("remove");
			worldIn.setBlockToAir(pos);
			worldIn.markBlockForUpdate(pos);
		}
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] { TICKS });
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		System.out.println(state.getValue(TICKS));
		return super.getActualState(state, worldIn, pos);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(TICKS, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (int) state.getValue(TICKS);
	}
}