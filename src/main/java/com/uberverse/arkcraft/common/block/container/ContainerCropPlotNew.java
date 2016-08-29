package com.uberverse.arkcraft.common.block.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.uberverse.arkcraft.common.block.tile.TileEntityCropPlotNew;

public class ContainerCropPlotNew extends Container
{
	private TileEntityCropPlotNew te;
	private int waterLast = -1, fertilizerLast = -1;

	public ContainerCropPlotNew(InventoryPlayer inventory, TileEntityCropPlotNew tileEntity)
	{
		this.te = tileEntity;
		int id = 0;
		int y = 26;
		for (int i = 0; i < 5; i++)
		{
			addSlotToContainer(new SlotCropPlot(tileEntity, id + i, i * 18 + 44, y));
		}
		y += 18;
		id += 5;
		for (int i = 0; i < 5; i++)
		{
			addSlotToContainer(new SlotCropPlot(tileEntity, id + i, i * 18 + 44, y));
		}
		addPlayerSlots(inventory, 8, 84);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return te.isUseableByPlayer(playerIn);
	}

	public void addPlayerSlots(InventoryPlayer playerInventory, int x, int y)
	{
		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				addSlotToContainer(
						new Slot(playerInventory, j + i * 9 + 9, x + j * 18, y + i * 18));
			}
		}

		for (int i = 0; i < 9; ++i)
		{
			addSlotToContainer(new Slot(playerInventory, i, x + i * 18, y + 58));
		}
	}

	public static class SlotCropPlot extends Slot
	{

		public SlotCropPlot(IInventory inventoryIn, int index, int xPosition, int yPosition)
		{
			super(inventoryIn, index, xPosition, yPosition);
		}

		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return inventory.isItemValidForSlot(getSlotIndex(), stack);
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		return null;
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		for (Object crafterObj : crafters)
		{
			ICrafting crafter = (ICrafting) crafterObj;
			if (waterLast != te.getField(0))
			{
				crafter.sendProgressBarUpdate(this, 0, te.getField(0));
			}
			if (fertilizerLast != te.getField(1))
			{
				crafter.sendProgressBarUpdate(this, 1, te.getField(1));
			}
		}
		waterLast = te.getField(0);
		fertilizerLast = te.getField(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data)
	{
		te.setField(id, data);
	}
}
