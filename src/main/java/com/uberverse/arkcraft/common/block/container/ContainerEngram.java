package com.uberverse.arkcraft.common.block.container;

import com.uberverse.arkcraft.common.container.scrollable.IContainerScrollable;
import com.uberverse.arkcraft.common.container.scrollable.SlotScrolling;
import com.uberverse.arkcraft.common.inventory.InventoryPlayerEngram;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author ERBF
 *
 */
public class ContainerEngram extends Container implements IContainerScrollable
{
	public static final int SLOT_SPACING = 20;

	private InventoryPlayerEngram invEngram;

	private int totalSlots;
	private int scrollingOffset = 0;

	private short selection = -1;

	public ContainerEngram(InventoryPlayerEngram inventory, EntityPlayer player)
	{
		invEngram = inventory;
		totalSlots = inventory.getSizeInventory();
		addSlotsAndEngrams(inventory);
	}

	private void addSlotsAndEngrams(InventoryPlayerEngram inventory)
	{
		int index = 0;
		for (int y = 0; y < 4; ++y)
		{
			for (int x = 0; x < 8; ++x)
			{
				if (index < inventory.getSizeInventory())
				{
					this.addSlotToContainer(
							new EngramSlot(inventory, index, 1 + x * 20, 44 + y * 20, this));
					index++;
				}
				else return;
			}
		}
	}

	public InventoryPlayerEngram getEngramInventory()
	{
		return invEngram;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return false;
	}

	@Override
	public int getScrollingOffset()
	{
		return this.scrollingOffset;
	}

	@Override
	public void scroll(int offset)
	{
		int newScrollingOffset = scrollingOffset + offset;
		if (isValidOffset(newScrollingOffset))
		{
			scrollingOffset = newScrollingOffset;
			refreshLists();
		}
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
	public int getScrollableSlotsCount()
	{
		return getScrollableSlotsWidth() * getScrollableSlotsHeight();
	}

	@Override
	public int getMaxOffset()
	{
		return getTotalSlotsAmount() / getScrollableSlotsWidth() - getScrollableSlotsHeight() + 1;
	}

	@Override
	public double getRelativeScrollingOffset()
	{
		return (double) this.scrollingOffset / (double) getMaxOffset();
	}

	@SuppressWarnings("unchecked")
	private void refreshLists()
	{
		for (int i = 0; i < inventoryItemStacks.size(); i++)
		{
			Slot slot = (Slot) inventorySlots.get(i);
			if (slot instanceof EngramSlot)
			{
				inventoryItemStacks.set(i, slot.getStack());
			}
		}
	}

	private boolean isValidOffset(int offset)
	{
		int maxOffset = getMaxOffset();
		return canScroll() && offset >= 0 && offset <= maxOffset;
	}

	@Override
	public boolean canScroll()
	{
		return getVisibleSlotsAmounts() < getTotalSlotsAmount();
	}

	@Override
	public int getVisibleSlotsAmounts()
	{
		return getTotalSlotsAmount() < getScrollableSlotsCount() ? getTotalSlotsAmount() : getScrollableSlotsCount();
	}

	@Override
	public int getTotalSlotsAmount()
	{
		return totalSlots;
	}

	public class EngramSlot extends SlotScrolling
	{

		public EngramSlot(InventoryPlayerEngram inventoryIn, int index, int xPosition, int yPosition, ContainerEngram container)
		{
			super(inventoryIn, index, xPosition, yPosition, container);
		}

		@Override
		public boolean canTakeStack(EntityPlayer playerIn)
		{
			return true;
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
	}

	@Override
	public int getScrollableSlotsX()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getScrollableSlotsY()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSlotSize()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void refreshScrollableSlotContents()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public IInventory getScrollableInventory()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initScrollableSlots()
	{
		// TODO Auto-generated method stub

	}

	public short getSelected()
	{
		return selection;
	}
}
