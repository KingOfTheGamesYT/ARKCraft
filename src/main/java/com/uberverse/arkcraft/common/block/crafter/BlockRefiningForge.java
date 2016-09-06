package com.uberverse.arkcraft.common.block.crafter;

import java.util.Random;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.tileentity.crafter.burner.TileEntityRefiningForge;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRefiningForge extends BlockBurner
{
	public BlockRefiningForge(Material material)
	{
		super(material);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(BURNING, false).withProperty(PART,
				EnumPart.BOTTOM));
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
		if (player.capabilities.isCreativeMode && state.getValue(PART) == EnumPart.BOTTOM)
		{
			BlockPos blockpos1 = pos.up();
			if (worldIn.getBlockState(blockpos1).getBlock() == this)
			{
				worldIn.setBlockToAir(blockpos1);
			}
		}
		else if (player.capabilities.isCreativeMode && state.getValue(PART) == EnumPart.TOP)
		{
			BlockPos blockpos1 = pos.down();
			if (worldIn.getBlockState(blockpos1).getBlock() == this)
			{
				worldIn.setBlockToAir(blockpos1);
			}
		}
	}

	@Override
	public String getHarvestTool(IBlockState state)
	{
		return null;
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
		double d0 = (double) pos.getX() + 0.8D;
		double d1 = (double) pos.getY() + 1.9D;
		double d2 = (double) pos.getZ() + 0.5D;
		IBlockState blockState = getActualState(getDefaultState(), worldIn, pos);
		boolean burning = (Boolean) blockState.getValue(BURNING);
		if (burning)
		{
			worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
		}
	}

	// This is where you can do something when the block is broken. In this case
	// drop the inventory's contents
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if (state.getValue(PART).equals(EnumPart.TOP)) pos = pos.down();
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof IInventory)
		{
			InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileEntity);
		}

		// if (inventory != null){
		// // For each slot in the inventory
		// for (int i = 0; i < inventory.getSizeInventory(); i++){
		// // If the slot is not empty
		// if (inventory.getStackInSlot(i) != null)
		// {
		// // Create a new entity item with the item stack in the slot
		// EntityItem item = new EntityItem(worldIn, pos.getX() + 0.5,
		// pos.getY() + 0.5, pos.getZ() + 0.5, inventory.getStackInSlot(i));
		//
		// // Apply some random motion to the item
		// float multiplier = 0.1f;
		// float motionX = worldIn.rand.nextFloat() - 0.5f;
		// float motionY = worldIn.rand.nextFloat() - 0.5f;
		// float motionZ = worldIn.rand.nextFloat() - 0.5f;
		//
		// item.motionX = motionX * multiplier;
		// item.motionY = motionY * multiplier;
		// item.motionZ = motionZ * multiplier;
		//
		// // Spawn the item in the world
		// worldIn.spawnEntityInWorld(item);
		// }
		// }
		//
		// // Clear the inventory so nothing else (such as another mod) can do
		// anything with the items
		// inventory.clear();
		// }

		// Super MUST be called last because it removes the tile entity
		super.breakBlock(worldIn, pos, state);
	}

	// ------------------------------------------------------------
	// The code below isn't necessary for illustrating the Inventory Furnace
	// concepts, it's just used for rendering.
	// For more background information see MBE03
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		if (state.getValue(PART).equals(EnumPart.TOP)) pos = pos.down();
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof TileEntityRefiningForge)
		{
			TileEntityRefiningForge tileEntityRefiningForge = (TileEntityRefiningForge) tileEntity;
			return state.withProperty(BURNING, tileEntityRefiningForge.isBurning());
		}
		return state;
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

	// necessary to define which properties your blocks use
	// will also affect the variants listed in the blockstates model file. See
	// MBE03 for more info.
	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] { BURNING, FACING, PART });
	}

	public static final PropertyBool BURNING = PropertyBool.create("burning");
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
	public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		IBlockState state = worldIn.getBlockState(pos);
		return state.getValue(PART) != EnumPart.TOP;
	}

	// the block will render in the SOLID layer. See
	// http://greyminecraftcoder.blogspot.co.at/2014/12/block-rendering-18.html
	// for more information.
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.SOLID;
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