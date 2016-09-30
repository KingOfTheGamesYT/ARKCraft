package com.uberverse.arkcraft.common.tileentity.crafter.burner;

import java.util.HashMap;
import java.util.Map;

import com.uberverse.arkcraft.common.burner.BurnerManager.BurnerFuel;
import com.uberverse.arkcraft.common.burner.BurnerManager.BurnerRecipe;
import com.uberverse.arkcraft.common.burner.IBurner;
import com.uberverse.arkcraft.common.item.ItemFuel;
import com.uberverse.arkcraft.common.tileentity.IDecayer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

/**
 * @author Lewis_McReu
 */
public abstract class TileEntityBurner extends TileEntity implements IInventory, IUpdatePlayerListBox, IBurner, IDecayer
{
	private ItemStack[] inventory;
	/** the currently active recipes */
	private Map<BurnerRecipe, Integer> activeRecipes = new HashMap<>();
	/** the ticks burning left */
	private int burningTicks;
	private boolean burning;
	private BurnerFuel currentFuel;

	public TileEntityBurner()
	{
		super();
		inventory = new ItemStack[getSizeInventory()];
		burning = false;
	}

	@Override
	public void update()
	{
		if (!worldObj.isRemote)
		{
			IBurner.super.update();
			IDecayer.super.update();
		}
	}

	public void setBurning(boolean burning)
	{
		this.burning = burning;
	}

	public int getBurningTicks()
	{
		return burningTicks;
	}

	public void setBurningTicks(int burningTicks)
	{
		this.burningTicks = burningTicks;
	}

	public boolean isBurning()
	{
		return burning;
	}

	public Map<BurnerRecipe, Integer> getActiveRecipes()
	{
		return activeRecipes;
	}

	public void sync()
	{
		markDirty();
		getWorld().markBlockForUpdate(pos);
	}

	@Override
	public World getWorldIA()
	{
		return worldObj;
	}

	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		IBurner.super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		IBurner.super.readFromNBT(compound);
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		writeToNBT(nbtTagCompound);
		return new S35PacketUpdateTileEntity(this.pos, 0, nbtTagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return index >= 0 && index < getSizeInventory() ? inventory[index] : null;
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		getStackInSlot(index).stackSize -= count;
		return new ItemStack(getStackInSlot(index).getItem(), count);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index)
	{
		return getStackInSlot(index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		if (index >= 0) inventory[index] = stack;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return Integer.MAX_VALUE;
	}

	// TODO option to do tribe access stuff here
	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player)
	{}

	@Override
	public void closeInventory(EntityPlayer player)
	{}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return stack != null ? stack.getItem() instanceof ItemFuel : false;
	}

	@Override
	public int getField(int id)
	{
		return id == 0 ? burningTicks : id == 1 ? burning ? 1 : 0 : 0;
	}

	@Override
	public void setField(int id, int value)
	{
		switch (id)
		{
			case 0:
				burningTicks = value;
				break;
			case 1:
				burning = value == 0 ? false : true;
				break;
		}
	}

	@Override
	public int getFieldCount()
	{
		return 2;
	}

	@Override
	public void clear()
	{
		for (int i = 0; i < getSizeInventory(); i++)
			inventory[i] = null;
	}

	@Override
	public String getName()
	{
		return null;
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public IChatComponent getDisplayName()
	{
		return null;
	}

	@Override
	public IInventory getIInventory()
	{
		return this;
	}

	@Override
	public BlockPos getPosition()
	{
		return pos;
	}

	@Override
	public BurnerFuel getCurrentFuel()
	{
		return currentFuel;
	}

	@Override
	public void setCurrentFuel(BurnerFuel fuel)
	{
		this.currentFuel = fuel;
	}

	@Override
	public ItemStack[] getInventory()
	{
		return inventory;
	}
}
