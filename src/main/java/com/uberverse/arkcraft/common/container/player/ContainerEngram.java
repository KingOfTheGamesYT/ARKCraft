package com.uberverse.arkcraft.common.container.player;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.arkplayer.ARKPlayer;
import com.uberverse.arkcraft.common.container.scrollable.ContainerScrollable;
import com.uberverse.arkcraft.common.container.scrollable.SlotScrolling;
import com.uberverse.arkcraft.common.engram.EngramManager;
import com.uberverse.arkcraft.common.engram.EngramManager.Engram;
import com.uberverse.arkcraft.common.inventory.InventoryEngram;
import com.uberverse.arkcraft.common.network.UpdateEngrams;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * @author ERBF
 * @author Lewis_McReu
 */
public class ContainerEngram extends ContainerScrollable
{
	private short selection = -1;
	private Engram selected = null;
	private InventoryEngram inventory;
	private EntityPlayer player;

	public ContainerEngram(InventoryEngram inventory, EntityPlayer player)
	{
		super();
		this.inventory = inventory;
		this.player = player;
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

	@Override
	public boolean enchantItem(EntityPlayer playerIn, int id)
	{
		if (id == 0)
		{
			playerIn.openGui(ARKCraft.instance, ARKCraft.GUI.PLAYER.getID(), playerIn.worldObj, 0, 0, 0);
			return true;
		}
		else if (id == 1)
		{
			ARKPlayer p = ARKPlayer.get(playerIn);
			p.learnEngram(selection);
			ARKCraft.modChannel.sendTo(new UpdateEngrams(p.getUnlockedEngrams(), p.getEngramPoints()), (EntityPlayerMP) playerIn);
			return true;
		}
		return super.enchantItem(playerIn, id);
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

	public Engram getSelectedEngram()
	{
		return selected;
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
}
