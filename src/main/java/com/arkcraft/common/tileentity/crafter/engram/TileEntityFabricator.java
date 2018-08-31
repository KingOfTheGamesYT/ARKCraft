package com.arkcraft.common.tileentity.crafter.engram;

import com.arkcraft.init.ARKCraftItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityFabricator extends TileEntityEngramCrafter {
	private int fuel = 0;
	private static final int FUELV = 15 * 60 * 20;
	private boolean on;

	public TileEntityFabricator() {
		super(24, "fabricator");
	}
	@Override
	public void update() {
		if(!world.isRemote){
			if(fuel > 0){
				fuel--;
				super.update();
			}else{
				if(on){
					for(int i = 0;i<getSizeInventory();i++){
						ItemStack s = getStackInSlot(i);
						if(!isEmpty(s) && s.getItem() == ARKCraftItems.gasoline){
							decrStackSize(i, 1);
							fuel += FUELV;
						}
					}
				}
			}
		}else super.update();
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("fuel", fuel);
		compound.setBoolean("active", on);
		return super.writeToNBT(compound);
	}
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		fuel = compound.getInteger("fuel");
		on = compound.getBoolean("active");
		super.readFromNBT(compound);
	}
	private static boolean isEmpty(ItemStack s){//for 1.11
		return s == null;
	}
	public void setActive(boolean b) {
		on = b;
	}
	public boolean isActive() {
		return on;
	}
	@Override
	public int getField(int id) {
		if(id == 1)return on ? 1 : 0;
		return super.getField(id);
	}
	@Override
	public int getFieldCount() {
		return 2;
	}
	@Override
	public void setField(int id, int value) {
		if(id == 1){
			on = value == 1;
		}else
			super.setField(id, value);
	}
}
