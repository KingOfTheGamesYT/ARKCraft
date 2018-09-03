package com.arkcraft.common.block.crafter;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.proxy.CommonProxy;
import com.arkcraft.common.tileentity.crafter.burner.TileEntityCampfire;

public class BlockCampfire extends BlockBurner
{
	private static final AxisAlignedBB BB = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.65F, 1.0F);
	public BlockCampfire()
	{
		super(Material.WOOD);
		setHardness(1.5f);
		this.setCreativeTab(ARKCraft.tabARK);
		this.setDefaultState(this.blockState.getBaseState().withProperty(BURNING, false));
	}

	@Override
	public int getId()
	{
		return CommonProxy.GUI.CAMPFIRE.id;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityCampfire();
	}

	@Override
	public String getHarvestTool(IBlockState state)
	{
		return "axe";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		double d0 = pos.getX() + 0.5D;
		double d1 = pos.getY() + 0.7D;
		double d2 = pos.getZ() + 0.5D;
		IBlockState blockState = getActualState(getDefaultState(), worldIn, pos);
		boolean burning = blockState.getValue(BURNING);

		if (burning) {
			worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.05D, 0.0D);
			worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BB;
	}
}
