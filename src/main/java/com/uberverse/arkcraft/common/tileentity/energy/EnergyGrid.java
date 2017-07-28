package com.uberverse.arkcraft.common.tileentity.energy;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnergyGrid {
	private List<IEnergyGridDevice> devices = new ArrayList<>();
	private List<TileEntityGenerator> generator = new ArrayList<>();
	public void invalidate(){
		devices.forEach(IEnergyGridDevice::invalidateGrid);
	}
	public void validate(TileEntityGenerator current) {
		Stack<IEnergyGridDevice> traversing = new Stack<>();
		World world = current.getWorld();
		traversing.add(current);
		while(!traversing.isEmpty()){
			IEnergyGridDevice d = traversing.pop();
			if(d instanceof TileEntityGenerator){
				generator.add((TileEntityGenerator) d);
			}
			devices.add(d);
			for(BlockPos p : d.next()){
				TileEntity te = world.getTileEntity(p);
				if(te instanceof IEnergyGridDevice){
					if(!devices.contains(te)){
						traversing.add((IEnergyGridDevice) te);
					}
				}
			}
		}
		devices.forEach(d -> d.setGrid(this));
	}
	public boolean isActive(){
		return generator.stream().anyMatch(TileEntityGenerator::isActive);
	}
	public void markNetDirty() {
		devices.forEach(IEnergyGridDevice::markNetDirty);
	}
}