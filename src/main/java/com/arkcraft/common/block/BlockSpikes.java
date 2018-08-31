package com.arkcraft.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSpikes extends Block
{

	private int renderType = 3; // default value

	public BlockSpikes(Material m, float hardness)
	{
		super(m);
		this.setHardness(hardness);
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		entityIn.attackEntityFrom(DamageSource.cactus, 4.0F);
	}
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		float f = 0.0625F;
		return new AxisAlignedBB(pos.getX() + f, pos.getY(), pos.getZ()
				+ f, pos.getX() + 1 - f, pos.getY() + 1 - f,
				pos.getZ() + 1 - f);
	}
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		float f = 0.0F;
		return new AxisAlignedBB(pos.getX() + f, pos.getY(), pos.getZ()
				+ f, pos.getX() + 1 - f, pos.getY() + 1, pos.getZ()
				+ 1 - f);
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

	public void setRenderType(int renderType)
	{
		this.renderType = renderType;
	}

	public int getRenderType()
	{
		return renderType;
	}

}