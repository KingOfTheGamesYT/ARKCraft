package com.arkcraft.common.block.crafter;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.proxy.CommonProxy;
import com.arkcraft.common.tileentity.crafter.engram.TileEntityFabricator;

public class BlockFabricator  extends BlockARKContainer
{
	public static final PropertyEnum<EnumPart> PART = PropertyEnum.create("part", EnumPart.class);
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockFabricator()
	{
		super(Material.IRON);
		this.setHardness(0.5F);
		this.setCreativeTab(ARKCraft.tabARK);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityFabricator();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos blockPos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!playerIn.isSneaking()) {
			if (state.getValue(PART) == EnumPart.RIGHT)
				playerIn.openGui(ARKCraft.instance(), getId(), worldIn, blockPos.getX(), blockPos.getY(), blockPos.getZ());
			else {
				EnumFacing f = state.getValue(FACING);
				BlockPos pos = blockPos.offset(f.rotateY());
				IBlockState oState = worldIn.getBlockState(pos);
				oState.getBlock().onBlockActivated(worldIn, pos, oState, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	// ---------------- Stuff for multiblock ------------------------
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		if(state.getBlock() != this)return FULL_BLOCK_AABB;
		EnumFacing f = state.getValue(FACING);
		EnumPart p = state.getValue(PART);
		switch (f) {
		case NORTH:
			switch (p) {
			case LEFT:
				return new AxisAlignedBB(0, 0, 0, 2, 1, 1);
			case RIGHT:
				return new AxisAlignedBB(-1, 0, 0, 1, 1, 1);
			}
			break;
		case EAST:
			switch (p) {
			case LEFT:
				return new AxisAlignedBB(0, 0, 0, 1, 1, 2);
			case RIGHT:
				return new AxisAlignedBB(0, 0, -1, 1, 1, 1);
			}
			break;
		case SOUTH:
			switch (p) {
			case LEFT:
				return new AxisAlignedBB(-1, 0, 0, 1, 1, 1);
			case RIGHT:
				return new AxisAlignedBB(0, 0, 0, 2, 1, 1);
			}
			break;
		case WEST:
			switch (p) {
			case LEFT:
				return new AxisAlignedBB(0, 0, -1, 1, 1, 1);
			case RIGHT:
				return new AxisAlignedBB(0, 0, 0, 1, 1, 2);
			}
			break;
		default:
			break;
		}
		return FULL_BLOCK_AABB;
	}
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		EnumFacing enumfacing = state.getValue(FACING);

		if (state.getValue(PART) == EnumPart.LEFT) {
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
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.SOLID;
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		EnumFacing enumfacing = EnumFacing.getHorizontal(meta);
		return (meta & 8) > 0 ? this.getDefaultState().withProperty(PART, EnumPart.LEFT).withProperty(FACING, enumfacing) : this.getDefaultState().withProperty(PART, EnumPart.RIGHT).withProperty(FACING, enumfacing);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		byte b0 = 0;
		int i = b0 | state.getValue(FACING).getHorizontalIndex();
		if (state.getValue(PART) == EnumPart.LEFT) {
			i |= 8;
		}
		return i;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FACING, PART);
	}

	@Override
	public int getId()
	{
		return CommonProxy.GUI.FABRICATOR.id;
	}
}