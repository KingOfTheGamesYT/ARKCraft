package com.uberverse.arkcraft.common.block.crafter;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.proxy.CommonProxy;
import com.uberverse.arkcraft.common.tileentity.crafter.engram.TileEntityMP;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author wildbill22
 */
public class BlockMortarAndPestle extends BlockARKContainer
{
	public BlockMortarAndPestle()
	{
		super(Material.ROCK);
		this.setHardness(0.5F);
		this.setCreativeTab(ARKCraft.tabARK);
		float f = 0.25F;
		float f1 = 0.25F; // Height
		this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
	}

	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityMP();
	}

	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
	{
		return true;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
	{
		return null;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public int getId()
	{
		return CommonProxy.GUI.MORTAR_AND_PESTLE.id;
	}
}
