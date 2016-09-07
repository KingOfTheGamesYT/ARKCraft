package com.uberverse.arkcraft.common.block.crafter;

import java.util.Random;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.tileentity.crafter.burner.TileEntityRefiningForge;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRefiningForge extends BlockBurner
{
	public BlockRefiningForge(Material material)
	{
		super(material);
		setHardness(2.0f);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.NORTH).withProperty(PART, EnumPart.BOTTOM));
	}

	@Override
	public int getId()
	{
		return ARKCraft.GUI.REFINING_FORGE.id;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		IBlockState state = getStateFromMeta(meta);
		if (state.getValue(PART).equals(EnumPart.BOTTOM)) return new TileEntityRefiningForge();
		return null;
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		if (state.getValue(PART) == EnumPart.BOTTOM)
		{
			BlockPos blockpos1 = pos.up();
			if (worldIn.getBlockState(blockpos1).getBlock() == this)
			{
				worldIn.setBlockToAir(blockpos1);
			}
			super.onBlockHarvested(worldIn, blockpos1, state, player);
		}
		else if (state.getValue(PART) == EnumPart.TOP)
		{
			BlockPos blockpos1 = pos.down();
			if (worldIn.getBlockState(blockpos1).getBlock() == this)
			{
				worldIn.setBlockToAir(blockpos1);
			}
			super.onBlockHarvested(worldIn, blockpos1, state, player);
		}
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		if (state.getValue(PART) == EnumPart.TOP) pos = pos.down();
		return super.getActualState(state, worldIn, pos);
	}

	@Override
	public String getHarvestTool(IBlockState state)
	{
		return "pick";
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
			EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY,
			float hitZ)
	{
		if (worldIn.isRemote) return true;
		if (state.getValue(PART).equals(EnumPart.TOP)) pos = pos.down();
		playerIn.openGui(ARKCraft.instance(), getId(), worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		double x = (double) pos.getX();
		double y = (double) pos.getY();
		double z = (double) pos.getZ();
		EnumFacing f = (EnumFacing) state.getValue(FACING);
		double xOffset = f == EnumFacing.WEST ? 0.75d : f == EnumFacing.EAST ? 0.25d : 0.5d;
		double yOffset = 1.9d;
		double zOffset = f == EnumFacing.NORTH ? 0.75d : f == EnumFacing.SOUTH ? 0.25d : 0.5d;
		IBlockState blockState = getActualState(getDefaultState(), worldIn, pos);
		boolean burning = (Boolean) blockState.getValue(BURNING);
		if (burning)
		{
			worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x + xOffset, y + yOffset, z + zOffset, 0, 0.05, 0);
			for (int i = 0; i < 5; i++)
				if (rand.nextBoolean()) worldIn.spawnParticle(EnumParticleTypes.FLAME, x + xOffset + ((double) rand.nextInt(2) - 1) / 10, y + yOffset,
						z + zOffset + ((double) rand.nextInt(2) - 1) / 10, 0, 0.05, 0);
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		EnumFacing enumfacing = EnumFacing.getHorizontal(meta);
		int metaOld = meta;
		EnumPart part = (metaOld & 8) > 0 ? EnumPart.TOP : EnumPart.BOTTOM;
		return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(PART, part);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		byte b0 = 0;
		int i = b0 | ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
		if (state.getValue(PART).equals(EnumPart.TOP)) i |= 8;
		return i;
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] { BURNING, FACING, PART });
	}

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyEnum PART = PropertyEnum.create("part", EnumPart.class);

	public static enum EnumPart implements IStringSerializable
	{
		TOP, BOTTOM;

		public String toString()
		{
			return getName();
		}

		public String getName()
		{
			return name().toLowerCase();
		}
	}

	@Override
	public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		IBlockState state = worldIn.getBlockState(pos);
		return state.getValue(PART) != EnumPart.TOP;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.SOLID;
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