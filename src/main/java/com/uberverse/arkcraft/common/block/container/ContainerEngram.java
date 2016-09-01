package com.uberverse.arkcraft.common.block.container;

import com.uberverse.arkcraft.common.container.scrollable.SlotScrolling;
import com.uberverse.arkcraft.common.inventory.InventoryEngram;
import com.uberverse.arkcraft.rework.container.ContainerScrollable;
import com.uberverse.arkcraft.rework.engram.EngramManager;
import com.uberverse.arkcraft.rework.engram.EngramManager.Engram;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * @author ERBF
 * @author Lewis_McReu
 */
public class ContainerEngram extends ContainerScrollable
{
	private short selection = -1;
	private Engram selected;
	private InventoryEngram inventory;

	public ContainerEngram(InventoryEngram inventory, EntityPlayer player)
	{
		super();
		this.inventory = inventory;
		initScrollableSlots();
	}

	@Override
	public void initScrollableSlots()
	{
		for (int i = 0; i < getVisibleSlotsAmount(); i++)
		{
			this.addSlotToContainer(new EngramSlot((InventoryEngram) getScrollableInventory(), i,
					getScrollableSlotsX() + i % getScrollableSlotsWidth() * getSlotSize(),
					getScrollableSlotsY() + i / getScrollableSlotsWidth() * getSlotSize(), this));
		}
	}

	public class EngramSlot extends SlotScrolling
	{

		public EngramSlot(InventoryEngram inventoryIn, int index, int xPosition, int yPosition, ContainerEngram container)
		{
			super(inventoryIn, index, xPosition, yPosition, container);
		}

		public Engram getEngram()
		{
			return EngramManager.instance().getEngram((short) getSlotIndex());
		}

		@Override
		public int getSlotStackLimit()
		{
			return 1;
		}

		@Override
		public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
		{
			setSelected((short) getSlotIndex());
		}
	}

	public void setSelected(short index)
	{
		this.selection = index;
		selected = EngramManager.instance().getEngram(index);
	}

	public short getSelected()
	{
		return selection;
	}

	@Override
	public int getScrollableSlotsWidth()
	{
		return 8;
	}

	@Override
	public int getScrollableSlotsHeight()
	{
		return 4;
	}

	@Override
	public int getScrollableSlotsX()
	{
		return 1;
	}

	@Override
	public int getScrollableSlotsY()
	{
		return 44;
	}

	@Override
	public int getSlotSize()
	{
		return 20;
	}

	@Override
	public IInventory getScrollableInventory()
	{
		return inventory;
	}

	public Engram getSelectedEngram()
	{
		return selected;
	}
}
