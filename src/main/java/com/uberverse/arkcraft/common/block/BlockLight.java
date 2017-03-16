package com.uberverse.arkcraft.common.block;

import java.util.Random;

import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockLight extends BlockAir
{
	public static final PropertyInteger TICKS = PropertyInteger.create("ticks", 0, 2);

	public BlockLight()
	{
		super();
		this.setLightLevel(0.8F);
		setDefaultState(getDefaultState().withProperty(TICKS, 0));
	}

	@Override
	public Material getMaterial(IBlockState state) {
		return Material.GRASS;
	}
	

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		int ticks = (int) worldIn.getBlockState(pos).getValue(TICKS);
		if (ticks < 2)
		{
			worldIn.setBlockState(pos, state.withProperty(TICKS, ++ticks));
			worldIn.markBlockForUpdate(pos);
			worldIn.scheduleUpdate(pos, this, 2);
		}
		else
		{
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