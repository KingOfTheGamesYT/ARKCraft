package com.uberverse.arkcraft.common.block.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import com.uberverse.arkcraft.common.block.BlockCropPlot;
import com.uberverse.arkcraft.common.config.ModuleItemBalance.CROP_PLOT;
import com.uberverse.arkcraft.common.item.ARKCraftFeces;
import com.uberverse.arkcraft.common.item.ARKCraftSeed;
import com.uberverse.lib.LogHelper;
import com.uberverse.lib.Utils;

public class TileEntityCropPlotNew extends TileEntity implements IInventory, IUpdatePlayerListBox{
	private ItemStack[] stack = new ItemStack[this.getSizeInventory()];
	private int growth = 0;
	private CropPlotState state = CropPlotState.EMPTY;
	private int fertilizer = 0;
	private int water = 0;
	private ItemStack growing;
	@Override
	public String getName() {
		return "cropPlot";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		return new ChatComponentText(getName());
	}

	@Override
	public int getSizeInventory() {
		return 10;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return stack[index];
	}

	@Override
	public ItemStack decrStackSize(int slot, int par2) {
		if (this.stack[slot] != null) {
			ItemStack itemstack;
			if (this.stack[slot].stackSize <= par2) {
				itemstack = this.stack[slot];
				this.stack[slot] = null;
				return itemstack;
			} else {
				itemstack = this.stack[slot].splitStack(par2);

				if (this.stack[slot].stackSize == 0) {
					this.stack[slot] = null;
				}
				return itemstack;
			}
		} else {
			return null;
		}
	}

	/*@Override //for mc 1.9
	public ItemStack removeStackFromSlot(int index) {
		ItemStack is = stack[index];
		stack[index] = null;
		return is;
	}*/

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.stack[index] = stack;
	}

	@Override
	public int getInventoryStackLimit() {
		return 100;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return Utils.isUseable(pos, player, worldObj, this);
	}


	@Override
	public void openInventory(EntityPlayer player) {

	}

	@Override
	public void closeInventory(EntityPlayer player) {

	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int id) {
		return id == 0 ? water : id == 1 ? fertilizer : 0;
	}

	@Override
	public void setField(int id, int value) {
		if(id == 0)water = value;
		else if(id == 1)fertilizer = value;
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		stack = new ItemStack[this.getSizeInventory()];
	}

	@Override
	public void update() {
		if(!worldObj.isRemote){
			for(int i = 0;i<10;i++){
				if(stack[i] != null){
					Item item = stack[i].getItem();
					if(item instanceof ARKCraftFeces){
						if(fertilizer < 100){
							fertilizer += 20;
							stack[i].setItemDamage(stack[i].getItemDamage() + 1);
							if(stack[i].getMaxDamage() == stack[i].getItemDamage()){
								decrStackSize(i, 1);
							}
						}
					}else if(item instanceof ARKCraftSeed){
						if(growing == null && fertilizer > 0 && water > 0){
							growth = CROP_PLOT.SEEDLING_TIME_FOR_BERRY * 20;
							LogHelper.info("[Crop Plot at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]: Started Growing: " + stack[i]);
							growing = decrStackSize(i, 1);
							state = CropPlotState.SEEDED;
						}
					}else if(item == Items.water_bucket){
						if(water == 0){
							stack[i] = new ItemStack(Items.bucket);
							water = 1000;
						}
					}
				}
			}
			if(growing != null){
				if(growth >= 0 && fertilizer > 0 && water > 0 && worldObj.getLight(pos) > 7){
					growth--;
					water--;
					fertilizer--;
				}
				if(growth < 0){
					if(state == CropPlotState.FRUITING){
						ItemStack s = growing;
						if(growth == -1)growing = ARKCraftSeed.getBerryForSeed(growing);
						LogHelper.info("[Crop Plot at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]: Growing Successful: "+s + ", output: " + growing);
						growing = TileEntityHopper.func_174918_a(this, growing, null);
						if(growing == null){
							state = CropPlotState.EMPTY;
							growth = -1;
						}
						else{
							growth = -2;
							LogHelper.warn("[Crop Plot at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]: Output stuck!! Output: " + growing);
						}
					}else{
						state = state.next();
						LogHelper.info("[Crop Plot at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]: Growing State Updated! Growing: "+growing + ", state: " + state.name());
						if(state.getTime() > 0)
							growth = state.getTime() * 20;
						else
							growth = -1;
					}
				}
				setState(state.age);
				if(growth % 50 == 0)LogHelper.info("[Crop Plot at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]: Growing: "+growing + ", growth remaining: " + growth + ", water: " + water + ", fertilizer: " + fertilizer + ".");
			}else{
				setState(0);
			}
		}
	}
	private void setState(int age){
		IBlockState state = worldObj.getBlockState(pos);
		//System.out.println(state);
		if(((Integer)state.getValue(BlockCropPlot.AGE)) != age){
			TileEntity tileentity = worldObj.getTileEntity(pos);
			worldObj.setBlockState(pos, state.withProperty(BlockCropPlot.AGE, age), 3);
			if (tileentity != null)
			{
				tileentity.validate();
				worldObj.setTileEntity(pos, tileentity);
			}
		}
	}
	@Override
	public ItemStack getStackInSlotOnClosing(int index) {
		return null;
	}
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		stack = new ItemStack[this.getSizeInventory()];
		NBTTagList list = compound.getTagList("inventory", 10);
		for (int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound = list.getCompoundTagAt(i);
			int j = nbttagcompound.getByte("Slot") & 255;

			if (j >= 0 && j < this.stack.length)
			{
				this.stack[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
			}
		}
		growth = compound.getInteger("growth");
		water = compound.getInteger("water");
		fertilizer = compound.getInteger("fertilizer");
		state = CropPlotState.VALUES[compound.getInteger("cropState")];
	}
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		NBTTagList list = new NBTTagList();
		for(int i = 0;i<stack.length;i++){
			if(stack[i] != null){
				NBTTagCompound tag = new NBTTagCompound();
				stack[i].writeToNBT(tag);
				tag.setByte("Slot", (byte) i);
				list.appendTag(tag);
			}
		}
		compound.setTag("inventory", list);
		compound.setInteger("growth", growth);
		compound.setInteger("water", water);
		compound.setInteger("fertilizer", fertilizer);
		compound.setInteger("cropState", state.ordinal());
		//return compound; //for mc 1.9
	}
	@Override
	public void markDirty() {
		super.markDirty();
	}
	public static enum CropPlotState{
		EMPTY(0){

			@Override
			public int getTime() {
				return 0;
			}

		}, SEEDED(1) {
			@Override
			public int getTime() {
				return 0;
			}
		}, SEEDLING(2) {
			@Override
			public int getTime() {
				return CROP_PLOT.SEEDLING_TIME_FOR_BERRY;
			}
		}, MIDLING(3) {
			@Override
			public int getTime() {
				return CROP_PLOT.MIDLING_TIME_FOR_BERRY;
			}
		}, GROWTHING(4) {
			@Override
			public int getTime() {
				return CROP_PLOT.GROWTHING_TIME_FOR_BERRY;
			}
		}, FRUITING(5) {
			@Override
			public int getTime() {
				return -1;
			}
		}

		;
		public final int age;
		public static final CropPlotState[] VALUES = values();

		public abstract int getTime();

		public CropPlotState next(){
			return VALUES[(ordinal() + 1) % VALUES.length];
		}
		private CropPlotState(int age) {
			this.age = age;
		}
	}

}
