package com.uberverse.arkcraft.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.block.tile.TileInventorySmithy;

/**
 * @author wildbill22
 */
public class BlockSmithy extends BlockContainer {
	private int renderType = 3; // default value
	private boolean isOpaque = false;
	private int ID;
	private boolean render = false;
	public static final PropertyEnum PART = PropertyEnum.create("part", BlockSmithy.EnumPartType.class);
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockSmithy(Material mat, int ID) {
		super(mat);
		this.setHardness(0.5F);
		this.ID = ID;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileInventorySmithy();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos blockPos, IBlockState state, EntityPlayer playerIn,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!playerIn.isSneaking()) {
			if(state.getValue(PART) == EnumPartType.RIGHT)
				playerIn.openGui(ARKCraft.instance(), ID, worldIn, blockPos.getX(), blockPos.getY(), blockPos.getZ());
			else{
				EnumFacing f = (EnumFacing) state.getValue(FACING);
				BlockPos pos = blockPos.offset(f.rotateY());
				IBlockState oState = worldIn.getBlockState(pos);
				oState.getBlock().onBlockActivated(worldIn, pos, oState, playerIn, side, hitX, hitY, hitZ);
			}
			return true;
		}
		return false;
	}

	public void setRenderType(int renderType) {
		this.renderType = renderType;
	}

	@Override
	public int getRenderType() {
		return renderType;
	}

	public void setOpaque(boolean opaque) {
		opaque = isOpaque;
	}

	@Override
	public boolean isOpaqueCube() {
		return isOpaque;
	}

	public void setRenderAsNormalBlock(boolean b) {
		render = b;
	}

	public boolean renderAsNormalBlock() {
		return render;
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	/**
	 * Returns randomly, about 1/2 of the recipe items
	 */
	@Override
	public java.util.List<ItemStack> getDrops(net.minecraft.world.IBlockAccess world, BlockPos pos, IBlockState state,
			int fortune) {
		java.util.List<ItemStack> ret = super.getDrops(world, pos, state, fortune);
		Random rand = world instanceof World ? ((World) world).rand : new Random();
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof TileInventorySmithy) {
			TileInventorySmithy tiSmithy = (TileInventorySmithy) tileEntity;
			for (int i = 0; i < TileInventorySmithy.INVENTORY_SLOTS_COUNT; ++i) {
				if (rand.nextInt(2) == 0) {
					ret.add(tiSmithy.getStackInSlot(TileInventorySmithy.FIRST_INVENTORY_SLOT + i));
				}
			}
		}
		return ret;
	}

	// ---------------- Stuff for multiblock ------------------------
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
		this.setSmithyBounds();
	}

	private void setSmithyBounds() {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	/**
	 * Called when a neighboring block changes.
	 */
	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);

		if (state.getValue(PART) == BlockSmithy.EnumPartType.LEFT) {
			if (worldIn.getBlockState(pos.offset(enumfacing.rotateY())).getBlock() != this) {
				worldIn.setBlockToAir(pos);
			}
		}
		else if (worldIn.getBlockState(pos.offset(enumfacing.rotateYCCW())).getBlock() != this) {
			worldIn.setBlockToAir(pos);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.SOLID;
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getHorizontal(meta);
		return (meta & 8) > 0
				? this.getDefaultState().withProperty(PART, BlockSmithy.EnumPartType.LEFT).withProperty(FACING,
						enumfacing)
						: this.getDefaultState().withProperty(PART, BlockSmithy.EnumPartType.RIGHT).withProperty(FACING,
								enumfacing);
	}

	/**
	 * Get the actual Block state of this Block at the given position. This
	 * applies properties not visible in the metadata, such as fence
	 * connections.
	 */
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state;
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		byte b0 = 0;
		int i = b0 | ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
		if (state.getValue(PART) == BlockSmithy.EnumPartType.LEFT) {
			i |= 8;
		}
		return i;
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, PART });
	}

	public static enum EnumPartType implements IStringSerializable {
		LEFT("left"), RIGHT("right");
		private final String name;

		private EnumPartType(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}

		@Override
		public String getName() {
			return this.name;
		}
	}
}
