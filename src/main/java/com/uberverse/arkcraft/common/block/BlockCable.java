package com.uberverse.arkcraft.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.tileentity.TileEntityCable;

public class BlockCable extends BlockContainer {
	public static final UnlistedPropertyCableData DATA = new UnlistedPropertyCableData();
	public BlockCable() {
		super(Material.ROCK);
		setCreativeTab(ARKCraft.tabARK);
		this.setHardness(0.5F);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityCable();
	}
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	public static class UnlistedPropertyCableData implements IUnlistedProperty<TileEntityCable>{

		@Override
		public String getName() {
			return "cabledata";
		}

		@Override
		public boolean isValid(TileEntityCable value) {
			return value != null;
		}

		@Override
		public Class<TileEntityCable> getType() {
			return TileEntityCable.class;
		}

		@Override
		public String valueToString(TileEntityCable value) {
			return value.toString();
		}
	}
	@Override
	protected BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[]{DATA});
	}
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		return state instanceof IExtendedBlockState && te != null && te instanceof TileEntityCable ? ((IExtendedBlockState)state).withProperty(DATA, ((TileEntityCable) te).checkConnections()) : state;
	}
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return NULL_AABB;
	}
}
