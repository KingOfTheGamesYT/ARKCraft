package com.uberverse.arkcraft.common.block.crafter;

import java.util.List;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.item.ARKCraftSeed;
import com.uberverse.arkcraft.common.proxy.CommonProxy;
import com.uberverse.arkcraft.common.tileentity.crafter.TileEntityCropPlot;
import com.uberverse.arkcraft.common.tileentity.crafter.TileEntityCropPlot.CropPlotType;
import com.uberverse.arkcraft.common.tileentity.crafter.TileEntityCropPlot.Part;
import com.uberverse.arkcraft.util.Identifiable;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author wildbill22
 */
public class BlockCropPlot extends BlockContainer implements Identifiable
{
	public static final int GROWTH_STAGES = 4; // 0 - 4
	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, GROWTH_STAGES);
	public static final PropertyEnum TYPE = PropertyEnum.create("type", CropPlotType.class);
	public static final PropertyEnum BERRY = PropertyEnum.create("berry", BerryColor.class);
	public static final PropertyBool TRANSPARENT = PropertyBool.create("transparent");
	private int renderType = 3; // default value
	private boolean isOpaque = false;

	public BlockCropPlot()
	{
		super(Material.wood);
		this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));
		// this.setTickRandomly(true);
		this.setCreativeTab(ARKCraft.tabARK);
		float f = 0.35F; // Height
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
		this.setHardness(0.5F);

		this.disableStats();
	}

	// replace use with below in comment? -> less tileentities means less lag?
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityCropPlot();
	}

	// @Override
	// public TileEntity createTileEntity(World world, IBlockState state)
	// {
	// return (boolean) state.getValue(TRANSPARENT) ? null : new TileEntityCropPlot();
	// }

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumFacing side, float hitX, float hitY, float hitZ)
	{
		state = getActualState(state, worldIn, pos);
		if (!playerIn.isSneaking())
		{
			if (state.getValue(TYPE) != TileEntityCropPlot.CropPlotType.SMALL && (boolean) state.getValue(TRANSPARENT))
			{
				for (int x = -1; x < 2; x++)
					for (int z = -1; z < 2; z++)
					{
						BlockPos pos2 = pos.add(x, 0, z);
						if (!(x == pos.getX() && z == pos.getZ()) && worldIn.getBlockState(pos2)
								.getBlock() instanceof BlockCropPlot)
						{
							IBlockState s = getActualState(worldIn.getBlockState(pos2), worldIn, pos2);
							if (!(boolean) s.getValue(TRANSPARENT)) return this.onBlockActivated(worldIn, pos2, state,
									playerIn, side, hitX, hitY, hitZ);
						}
					}
			}
			else
			{
				// TileEntityCropPlot te = (TileEntityCropPlot) worldIn.getTileEntity(pos);
				// if (te.part != Part.MIDDLE)
				// {
				// BlockPos pos2 = te.part.offset(pos, true);
				// return this.onBlockActivated(worldIn, pos2, worldIn.getBlockState(pos2), playerIn, side, hitX, hitY,
				// hitZ);
				// }
				// else
				// {
				if (playerIn.getHeldItem() != null && playerIn.getHeldItem().getItem() instanceof ARKCraftSeed
						&& ((Integer) state.getValue(AGE)) == 0)
				{
					ItemStack s = playerIn.getHeldItem().splitStack(1);
					s = TileEntityHopper.func_174918_a((IInventory) worldIn.getTileEntity(pos), s, null);
					if (s != null)
					{
						if (!playerIn.inventory.addItemStackToInventory(s))
						{
							EntityItem item = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), s);
							worldIn.spawnEntityInWorld(item);
						}
					}
				}
				// }
				if (playerIn.getHeldItem() != null && playerIn.getHeldItem().getItem() == Items.water_bucket)
				{
					ItemStack container = playerIn.getHeldItem();
					if (FluidContainerRegistry.isFilledContainer(container))
					{
						TileEntity entity = worldIn.getTileEntity(pos);
						if (entity instanceof TileEntityCropPlot && entity != null)
						{
							TileEntityCropPlot target = (TileEntityCropPlot) entity;
							int water = TileEntityCropPlot.getItemWaterValue(container) + target.getField(0);
							// The currentWater + addedWater needs to be smaller or
							// equal to the max water.
							if (water <= target.getType().maxWater)
							{
								target.setField(0, water);
								ItemStack drainedContainer = FluidContainerRegistry.drainFluidContainer(container);
								if (drainedContainer != null)
								{
									if (!playerIn.capabilities.isCreativeMode) container.setItem(drainedContainer
											.getItem());
								}
							}
						}
					}
				}

				else
				{
					playerIn.openGui(ARKCraft.instance(), getId(), worldIn, pos.getX(), pos.getY(), pos.getZ());
				}
			}
			return true;
		}
		return false;
	}

	public void setRenderType(int renderType)
	{
		this.renderType = renderType;
	}

	@Override
	public int getRenderType()
	{
		return renderType;
	}

	public void setOpaque(boolean opaque)
	{
		opaque = isOpaque;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return isOpaque;
	}

	@Override
	public boolean isFullCube()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.CUTOUT;
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(AGE, Integer.valueOf(meta % 5)).withProperty(TYPE,
				CropPlotType.VALUES[meta / 5]);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((Integer) state.getValue(AGE)).intValue() + ((CropPlotType) state.getValue(TYPE)).ordinal() * 5;
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] { AGE, TYPE, BERRY, TRANSPARENT });
	}

	/**
	 * Returns randomly, about 1/2 of the fertilizer and berries
	 */
	/*
	 * @Override public java.util.List<ItemStack>
	 * getDrops(net.minecraft.world.IBlockAccess world, BlockPos pos,
	 * IBlockState state, int fortune) { java.util.List<ItemStack> ret =
	 * super.getDrops(world, pos, state, fortune); Random rand = world
	 * instanceof World ? ((World) world).rand : new Random(); TileEntity
	 * tileEntity = world.getTileEntity(pos); if (tileEntity instanceof
	 * TileInventoryCropPlot) { TileInventoryCropPlot tiCropPlot =
	 * (TileInventoryCropPlot) tileEntity; for (int i = 0; i <
	 * TileInventoryCropPlot.FERTILIZER_SLOTS_COUNT; ++i) { if (rand.nextInt(2)
	 * == 0) { ret.add(tiCropPlot
	 * .getStackInSlot(TileInventoryCropPlot.FIRST_FERTILIZER_SLOT + i)); } } if
	 * (rand.nextInt(2) == 0) {
	 * ret.add(tiCropPlot.getStackInSlot(TileInventoryCropPlot.BERRY_SLOT)); } }
	 * return ret; }
	 */
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntityCropPlot tile = (TileEntityCropPlot) worldIn.getTileEntity(pos);
		if (tile != null) InventoryHelper.dropInventoryItems(worldIn, pos, tile);
		CropPlotType t = (CropPlotType) state.getValue(TYPE);
		if (tile.part == Part.MIDDLE)
		{
			if (t != CropPlotType.SMALL)
			{
				for (Part p : Part.VALUES)
				{
					if (p != Part.MIDDLE)
					{
						BlockPos pos2 = p.offset(pos, false);
						worldIn.setBlockState(pos2, Blocks.air.getDefaultState());
					}
				}
			}
		}
		else
		{
			worldIn.setBlockState(tile.part.offset(pos, true), Blocks.air.getDefaultState());
		}
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		CropPlotType t = CropPlotType.VALUES[stack.getMetadata() % 3];
		state = getDefaultState();
		state = state.withProperty(TYPE, t);
		worldIn.setBlockState(pos, state);
		if (tile != null && tile instanceof TileEntityCropPlot)
		{
			tile.validate();
			worldIn.setTileEntity(pos, tile);
		}
		if (t != CropPlotType.SMALL)
		{
			for (Part p : Part.VALUES)
			{
				if (p != Part.MIDDLE)
				{
					BlockPos pos2 = p.offset(pos, false);
					worldIn.setBlockState(pos2, state);
					TileEntity tileNew = worldIn.getTileEntity(pos2);
					if (tileNew instanceof TileEntityCropPlot)
					{
						TileEntityCropPlot te = (TileEntityCropPlot) tileNew;
						te.part = p;
					}
					else
					{
						ARKCraft.logger.error("Invalid tile entity at " + pos2.toString() + " tile entity: " + tileNew);
					}
				}
			}
		}
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood
	 * returns 4 blocks)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, @SuppressWarnings("rawtypes") List list)
	{
		list.add(new ItemStack(itemIn, 1, 0));
		list.add(new ItemStack(itemIn, 1, 1));
		list.add(new ItemStack(itemIn, 1, 2));
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return ((CropPlotType) state.getValue(TYPE)).ordinal();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
	{
		IBlockState state = worldIn.getBlockState(pos);
		CropPlotType t = (CropPlotType) state.getValue(TYPE);
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof TileEntityCropPlot)
		{
			TileEntityCropPlot te = (TileEntityCropPlot) tile;
			BlockPos p = new BlockPos(0, 0, 0);
			p = te.part.offset(p, true);
			if (t == CropPlotType.SMALL) return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1,
					pos.getY() + 0.35F, pos.getZ() + 1);
			else if (t == CropPlotType.MEDIUM) return new AxisAlignedBB(pos.getX() - 0.5F + p.getX(), pos.getY(), pos
					.getZ() - 0.5F + p.getZ(), pos.getX() + 1.5F + p.getX(), pos.getY() + 0.35F, pos.getZ() + 1.5F + p
							.getZ());
			else if (t == CropPlotType.LARGE) return new AxisAlignedBB(pos.getX() - 1 + p.getX(), pos.getY(), pos.getZ()
					- 1 + p.getZ(), pos.getX() + 2 + p.getX(), pos.getY() + 0.35F, pos.getZ() + 2 + p.getZ());
		}
		else
		{
			return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 0.35F, pos.getZ()
					+ 1);
		}
		return super.getSelectedBoundingBox(worldIn, pos);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
	{
		AxisAlignedBB bb = null;
		IBlockState state = worldIn.getBlockState(pos);
		CropPlotType t = (CropPlotType) state.getValue(TYPE);
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof TileEntityCropPlot)
		{
			TileEntityCropPlot te = (TileEntityCropPlot) tile;
			BlockPos p = new BlockPos(0, 0, 0);
			p = te.part.offset(p, true);
			if (t == CropPlotType.SMALL) bb = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos
					.getY() + 0.35F, pos.getZ() + 1);
			else if (t == CropPlotType.MEDIUM) bb = new AxisAlignedBB(pos.getX() - 0.5F + p.getX(), pos.getY(), pos
					.getZ() - 0.5F + p.getZ(), pos.getX() + 1.5F + p.getX(), pos.getY() + 0.35F, pos.getZ() + 1.5F + p
							.getZ());
			else if (t == CropPlotType.LARGE) bb = new AxisAlignedBB(pos.getX() - 1 + p.getX(), pos.getY(), pos.getZ()
					- 1 + p.getZ(), pos.getX() + 2 + p.getX(), pos.getY() + 0.35F, pos.getZ() + 2 + p.getZ());
		}
		else
		{
			bb = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 0.35F, pos.getZ()
					+ 1);
		}
		setBlockBounds((float) bb.minX - pos.getX(), (float) bb.minY - pos.getY(), (float) bb.minZ - pos.getZ(),
				(float) bb.maxX - pos.getX(), (float) bb.maxY - pos.getY(), (float) bb.maxZ - pos.getZ());
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
	{
		CropPlotType t = (CropPlotType) state.getValue(TYPE);
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof TileEntityCropPlot)
		{
			TileEntityCropPlot te = (TileEntityCropPlot) tile;
			BlockPos p = new BlockPos(0, 0, 0);
			p = te.part.offset(p, true);
			if (t == CropPlotType.SMALL) return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1,
					pos.getY() + 0.35F, pos.getZ() + 1);
			else if (t == CropPlotType.MEDIUM) return new AxisAlignedBB(pos.getX() - 0.5F + p.getX(), pos.getY(), pos
					.getZ() - 0.5F + p.getZ(), pos.getX() + 1.5F + p.getX(), pos.getY() + 0.35F, pos.getZ() + 1.5F + p
							.getZ());
			else if (t == CropPlotType.LARGE) return new AxisAlignedBB(pos.getX() - 1 + p.getX(), pos.getY(), pos.getZ()
					- 1 + p.getZ(), pos.getX() + 2 + p.getX(), pos.getY() + 0.35F, pos.getZ() + 2 + p.getZ());
		}
		else
		{
			return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 0.35F, pos.getZ()
					+ 1);
		}
		return super.getCollisionBoundingBox(worldIn, pos, state);
	}

	public static enum BerryColor implements IStringSerializable
	{
		AMAR, AZUL, MEJO, NARCO, STIM, TINTO

		;

		public static final BerryColor[] VALUES = values();

		@Override
		public String getName()
		{
			return name().toLowerCase();
		}
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof TileEntityCropPlot)
		{
			TileEntityCropPlot te = (TileEntityCropPlot) tile;
			return state.withProperty(BERRY, te.getGrowingColor()).withProperty(TRANSPARENT, te.isTransparent());
		}
		return state;
	}

	@Override
	public void fillWithRain(World worldIn, BlockPos pos)
	{
		TileEntityCropPlot te = (TileEntityCropPlot) worldIn.getTileEntity(pos);
		if (te.part == Part.MIDDLE)
		{
			te.fillWithRain(true);
		}
		else
		{
			TileEntity tile = worldIn.getTileEntity(te.part.offset(pos, true));
			if (tile instanceof TileEntityCropPlot)
			{
				te = (TileEntityCropPlot) tile;
				te.fillWithRain(true);
			}
		}
	}

	@Override
	public int getId()
	{
		return CommonProxy.GUI.CROP_PLOT.id;
	}
}
