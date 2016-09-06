package com.uberverse.arkcraft.common.block.crafter;

import java.util.Random;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.tileentity.crafter.burner.TileEntityCampfire;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCampfire extends BlockBurner
{
	public BlockCampfire(Material material)
	{
		super(material);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setDefaultState(this.blockState.getBaseState().withProperty(BURNING, false));
		float f = 0.65F; // Height
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
	}

	@Override
	public int getId()
	{
		return ARKCraft.GUI.CAMPFIRE.id;
	}

	// Called when the block is placed or loaded client side to get the tile
	// entity for the block
	// Should return a new instance of the tile entity for the block
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityCampfire();
	}

	@Override
	public String getHarvestTool(IBlockState state)
	{
		return null;
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
			worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
			worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
		}

		// worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4 * 1, d1
		// + d3, d2 + d4 * 1, 0.0D, 0.0D, 0.0D, new int[0]);
		// worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4 * 1, d1 + d3,
		// d2 + d4 * 1, 0.0D, 0.0D, 0.0D, new int[0]);

	}

	// This is where you can do something when the block is broken. In this case
	// drop the inventory's contents
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof IInventory)
		{
			InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileEntity);
		}
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState();
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}

	// necessary to define which properties your blocks use
	// will also affect the variants listed in the blockstates model file. See
	// MBE03 for more info.
	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] { BURNING });
	}

	public static final PropertyBool BURNING = PropertyBool.create("burning");

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
			// linearly interpolate the light value depending on how many slots
			// are burning
			lightValue = 0;
		}
		lightValue = MathHelper.clamp_int(lightValue, 0, 15);
		return lightValue;
	}

	// the block will render in the SOLID layer. See
	// http://greyminecraftcoder.blogspot.co.at/2014/12/block-rendering-18.html
	// for more information.
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.CUTOUT;
	}

	// used by the renderer to control lighting and visibility of other blocks.
	// set to false because this block doesn't fill the entire 1x1x1 space
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	// used by the renderer to control lighting and visibility of other blocks,
	// also by
	// (eg) wall or fence to control whether the fence joins itself to this
	// block
	// set to false because this block doesn't fill the entire 1x1x1 space
	@Override
	public boolean isFullCube()
	{
		return false;
	}

	// render using a BakedModel
	// not strictly required because the default (super method) is 3.
	@Override
	public int getRenderType()
	{
		return 3;
	}
}
