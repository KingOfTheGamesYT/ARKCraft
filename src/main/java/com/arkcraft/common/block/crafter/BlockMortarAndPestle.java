package com.arkcraft.common.block.crafter;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.proxy.CommonProxy;
import com.arkcraft.common.tileentity.crafter.engram.TileEntityMP;

/**
 * @author wildbill22
 */
public class BlockMortarAndPestle extends BlockARKContainer
{
	private static final AxisAlignedBB BB;
	static{
		float f = 0.25F;
		float f1 = 0.25F; // Height
		BB = new AxisAlignedBB(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
	}
	public BlockMortarAndPestle()
	{
		super(Material.ROCK);
		this.setHardness(0.5F);
		this.setCreativeTab(ARKCraft.tabARK);
	}

	@Override
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
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return NULL_AABB;
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
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		return BB;
	}
}
