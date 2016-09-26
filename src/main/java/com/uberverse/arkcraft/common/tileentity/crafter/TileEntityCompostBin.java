package com.uberverse.arkcraft.common.tileentity.crafter;

import java.util.Arrays;
import java.util.Collection;

import com.google.common.collect.Lists;
import com.uberverse.arkcraft.common.item.ARKCraftFeces;
import com.uberverse.arkcraft.common.tileentity.IDecayer;
import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.arkcraft.util.IInventoryAdder;
import com.uberverse.arkcraft.util.InventoryUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
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
 * @author wildbill22
 */
public class TileEntityCompostBin extends TileEntity implements IInventory, IUpdatePlayerListBox, IInventoryAdder,
		IDecayer
{
	private final ItemStack[] inventory;
	private int progress;

	public TileEntityCompostBin()
	{
		inventory = new ItemStack[8];
		progress = -1;
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
	public void update()
	{
		if (!worldObj.isRemote)
		{
			// if no process active
			int oldProgress = progress;
			findProcess();
			// if no process found
			if (progress < 0) return;
			else progress = oldProgress;
			progress++;
			// TODO implement composting time
			// done composting
			if (progress > 100) if (consume()) findProcess();
			IDecayer.super.update();
		}
	}

	private boolean consume()
	{
		if (canCompost())
		{
			// consume thatch
			int thatchLeft = 50;
			for (int s = 0; s < inventory.length; s++)
			{
				ItemStack i = inventory[s];
				if (i != null)
				{
					if (i.getItem() == ARKCraftItems.thatch)
					{
						if (i.stackSize > thatchLeft)
						{
							i.stackSize -= thatchLeft;
							thatchLeft = 0;
							break;
						}
						else
						{
							inventory[s] = null;
							thatchLeft -= i.stackSize;
							if (thatchLeft == 0) break;
						}
					}
				}
			}

			// consume feces
			Collection<Item> fecesFound = Lists.newArrayList();
			Collection<Integer> indices = Lists.newArrayList();
			outer: for (int i = 0; i < inventory.length; i++)
			{
				ItemStack s = inventory[i];
				if (s != null)
				{
					// TODO massive feces
					// if (s.getItem() == ARKCraftItems.massive_feces){
					// inventory[i] = null;
					// break;
					// }
					if (s.getItem() instanceof ARKCraftFeces && !fecesFound.contains(s.getItem()))
					{
						indices.add(i);
						fecesFound.add(s.getItem());
						for (int j = i + 1; j < inventory.length; j++)
						{
							ItemStack s2 = inventory[j];
							if (s2 != null && s2.getItem() == s.getItem()) indices.add(j);
							if (indices.size() == 3)
							{
								for (Integer k : indices)
									inventory[k] = null;
								progress = -1;
								break outer;
							}
						}
					}
				}
			}

			ItemStack out = new ItemStack(ARKCraftItems.fertilizer);
			ARKCraftItems.fertilizer.onCreated(out, worldObj, null);
			addOrDrop(out);

			markDirty();
			worldObj.markBlockForUpdate(pos);
			return true;
		}
		return false;
	}

	private void findProcess()
	{
		progress = -1;
		if (canCompost()) progress = 0;
	}

	private boolean canCompost()
	{
		if (countThatch() < 50) return false;

		Collection<Item> fecesFound = Lists.newArrayList();
		for (int i = 0; i < inventory.length; i++)
		{
			ItemStack s = inventory[i];
			if (s != null)
			{
				// TODO massive feces
				// if (s.getItem() == ARKCraftItems.massive_feces)
				// return true;
				if (s.getItem() instanceof ARKCraftFeces && !fecesFound.contains(s.getItem()))
				{
					fecesFound.add(s.getItem());
					ItemStack[] toSearch = Arrays.copyOfRange(inventory, i + 1, inventory.length);
					int count = 1;
					for (ItemStack s2 : toSearch)
					{
						if (s2 != null && s2.getItem() == s.getItem()) count++;
						if (count == 3) return true;
					}
				}
			}
		}
		return false;
	}

	private int countThatch()
	{
		int thatch = 0;
		for (ItemStack s : inventory)
			if (s != null && s.getItem() == ARKCraftItems.thatch) thatch += s.stackSize;
		return thatch;
	}

	@Override
	public int getSizeInventory()
	{
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return inventory[index];
	}

	@Override
	public ItemStack decrStackSize(int slotIndex, int count)
	{
		ItemStack itemStackInSlot = getStackInSlot(slotIndex);
		if (itemStackInSlot == null) { return null; }

		ItemStack itemStackRemoved;
		if (itemStackInSlot.stackSize <= count)
		{
			itemStackRemoved = itemStackInSlot;
			setInventorySlotContents(slotIndex, null);
		}
		else
		{
			itemStackRemoved = itemStackInSlot.splitStack(count);
			if (itemStackInSlot.stackSize == 0)
			{
				setInventorySlotContents(slotIndex, null);
			}
		}
		markDirty();
		return itemStackRemoved;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index)
	{
		return getStackInSlot(index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		if (index < getSizeInventory()) inventory[index] = stack;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		if (this.worldObj.getTileEntity(this.pos) != this) { return false; }
		final double X_CENTRE_OFFSET = 0.5;
		final double Y_CENTRE_OFFSET = 0.5;
		final double Z_CENTRE_OFFSET = 0.5;
		final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;
		return player.getDistanceSq(pos.getX() + X_CENTRE_OFFSET, pos.getY() + Y_CENTRE_OFFSET, pos.getZ()
				+ Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
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
		return stack != null && (stack.getItem() instanceof ARKCraftFeces || stack.getItem() == ARKCraftItems.thatch);
	}

	@Override
	public int getField(int id)
	{
		return progress;
	}

	@Override
	public void setField(int id, int value)
	{
		progress = value;
	}

	@Override
	public int getFieldCount()
	{
		return 1;
	}

	@Override
	public void clear()
	{
		Arrays.fill(inventory, null);
	}

	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		InventoryUtil.writeToNBT(compound, this);
		compound.setInteger("progress", progress);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		InventoryUtil.readFromNBT(compound, this);
		progress = compound.getInteger("progress");
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new S35PacketUpdateTileEntity(getPos(), 0, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public IInventory getIInventory()
	{
		return this;
	}

	@Override
	public BlockPos getPosition()
	{
		return getPos();
	}

	@Override
	public World getWorldIA()
	{
		return getWorld();
	}

	@Override
	public double getDecayModifier(ItemStack stack)
	{
		return 10;
	}
}
