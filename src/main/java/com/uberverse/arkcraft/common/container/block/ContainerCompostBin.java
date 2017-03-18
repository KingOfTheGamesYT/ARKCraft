package com.uberverse.arkcraft.common.container.block;

import com.uberverse.arkcraft.common.tileentity.crafter.TileEntityCompostBin;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerCompostBin extends Container
{
	private final IInventory inventory;
	private final IInventory playerInventory;
	private static final int binXStart = 53;
	private static final int binYStart = 26;
	private static final int playerXStart = 8;
	private static final int playerYStart = 84;
	private static final int hotbarYStart = 142;

	public ContainerCompostBin(EntityPlayer player, TileEntityCompostBin tileEntity)
	{
		super();
		playerInventory = player.inventory;
		inventory = tileEntity;

		for (int row = 0; row < 2; row++)
			for (int col = 0; col < 4; col++)
				addSlotToContainer(new Slot(inventory, col + row * 4, binXStart + col * 18, binYStart + row * 18));

		for (int row = 0; row < 3; row++)
			for (int col = 0; col < 9; col++)
				addSlotToContainer(new Slot(playerInventory, 9 + col + row * 9, playerXStart + col * 18, playerYStart
						+ row * 18));

		for (int col = 0; col < 9; col++)
			addSlotToContainer(new Slot(playerInventory, col, playerXStart + col * 18, hotbarYStart));
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return inventory.isUsableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (slot.inventory == inventory)
			{
				if (!this.mergeItemStack(itemstack1, 8, 8 + 36, false)) return null;
			}
			else if (slot.inventory == playerInventory) if (inventory.isItemValidForSlot(0, itemstack))
			{
				if (!this.mergeItemStack(itemstack1, 0, inventory.getSizeInventory(), false)) return null;
			}
			else return null;
			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack) null);
			}
			else
			{
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

	private int[] cachedFields;

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		boolean allFieldsHaveChanged = false;
		boolean fieldHasChanged[] = new boolean[inventory.getFieldCount()];
		if (cachedFields == null)
		{
			cachedFields = new int[inventory.getFieldCount()];
			allFieldsHaveChanged = true;
		}

		for (int i = 0; i < cachedFields.length; ++i)
		{
			if (allFieldsHaveChanged || cachedFields[i] != inventory.getField(i))
			{
				cachedFields[i] = inventory.getField(i);
				fieldHasChanged[i] = true;
			}
		}

		for (int i = 0; i < this.listeners.size(); ++i)
		{
			IContainerListener icrafting = (IContainerListener) this.listeners.get(i);
			for (int fieldID = 0; fieldID < inventory.getFieldCount(); ++fieldID)
			{
				if (fieldHasChanged[fieldID])
				{
					// Note that although sendProgressBarUpdate takes 2 ints on
					// a server these are truncated to shorts
					icrafting.sendProgressBarUpdate(this, fieldID, cachedFields[fieldID]);
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int data)
	{
		inventory.setField(id, data);
	}
}
