package com.uberverse.arkcraft.common.tileentity.energy;

import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import com.uberverse.arkcraft.common.tileentity.crafter.TileEntityArkCraft;

public abstract class TileEntityGenerator extends TileEntityArkCraft implements ITickable, IEnergyGridDevice {
	public EnergyGrid grid;
	public abstract boolean isActive();
	@Override
	public void invalidateGrid(){
		grid = null;
	}
	@Override
	public void setGrid(EnergyGrid grid) {
		this.grid = grid;
	}
	@Override
	public final void update() {
		if(!world.isRemote){
			if(grid == null){
				grid = new EnergyGrid();
				grid.validate(this);
			}
		}
		updateI();
	}
	public abstract void updateI();
	@Override
	public BlockPos getPos2() {
		return pos;
	}
	public void invalidateGrid2() {
		if(grid != null){
			grid.invalidate();
			if(grid == null){
				grid = new EnergyGrid();
				grid.validate(this);
			}
		}
	}
}
