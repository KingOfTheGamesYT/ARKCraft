package com.uberverse.arkcraft.common.tileentity.energy;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public interface IEnergyGridDevice {
	void invalidateGrid();
	void setGrid(EnergyGrid grid);
	BlockPos getPos2();
	boolean canConnect(EnumFacing face);
	default List<BlockPos> next(){
		List<BlockPos> ret = new ArrayList<>();
		for (EnumFacing d : EnumFacing.VALUES) {
			if (canConnect(d)) {
				ret.add(getPos2().offset(d));
			}
		}
		return ret;
	}
	default void markNetDirty(){}
}