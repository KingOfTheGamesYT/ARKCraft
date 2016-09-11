package com.uberverse.arkcraft.common.block.crafter;

import java.util.Random;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.proxy.CommonProxy;
import com.uberverse.arkcraft.common.tileentity.crafter.burner.TileEntityCampfire;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCampfire extends BlockBurner
{
	public BlockCampfire()
	{
		super(Material.wood);
		setHardness(1.5f);
		this.setCreativeTab(ARKCraft.tabARK);
		this.setDefaultState(this.blockState.getBaseState().withProperty(BURNING, false));
		float f = 0.65F; // Height
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
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
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY,
			float hitZ)
	{
		if (worldIn.isRemote) return true;

		playerIn.openGui(ARKCraft.instance(), getId(), worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		double d0 = (double) pos.getX() + 0.5D;
		double d1 = (double) pos.getY() + 0.7D;
		double d2 = (double) pos.getZ() + 0.5D;
		IBlockState blockState = getActualState(getDefaultState(), worldIn, pos);
		boolean burning = (Boolean) blockState.getValue(BURNING);

		if (burning)
		{
			worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.05D, 0.0D);
			worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(BURNING, meta == 1 ? true : false);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (Boolean) state.getValue(BURNING) ? 1 : 0;
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] { BURNING });
	}

	public static final PropertyBool BURNING = PropertyBool.create("burning");

	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.CUTOUT;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean isFullCube()
	{
		return false;
	}
}
