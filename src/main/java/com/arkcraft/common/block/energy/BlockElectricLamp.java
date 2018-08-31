package com.arkcraft.common.block.energy;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.arkcraft.common.block.crafter.BlockARKContainer;
import com.arkcraft.common.tileentity.energy.TileEntityElectricLamp;

public class BlockElectricLamp extends BlockARKContainer {
	public static final PropertyBool POWERED = PropertyBool.create("powered");
	public BlockElectricLamp() {
		super(Material.GLASS);
		setHardness(0.3F);
		lightOpacity = 0;
		translucent = true;
	}

	@Override
	public int getId() {
		return -1;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityElectricLamp();
	}
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, POWERED);
	}
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(POWERED, meta == 1);
	}
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(POWERED) ? 1 : 0;
	}
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getValue(POWERED) ? 15 : 0;
	}
}
