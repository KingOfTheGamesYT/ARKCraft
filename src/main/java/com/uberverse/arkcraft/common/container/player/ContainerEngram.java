package com.uberverse.arkcraft.common.container.player;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.arkplayer.ARKPlayer;
import com.uberverse.arkcraft.common.container.scrollable.ContainerScrollable;
import com.uberverse.arkcraft.common.container.scrollable.SlotScrolling;
import com.uberverse.arkcraft.common.engram.EngramManager;
import com.uberverse.arkcraft.common.engram.EngramManager.Engram;
import com.uberverse.arkcraft.common.inventory.InventoryEngram;
import com.uberverse.arkcraft.common.network.UpdateEngrams;
import com.uberverse.arkcraft.common.proxy.CommonProxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author ERBF
 * @author Lewis_McReu
 */
public class ContainerEngram extends ContainerScrollable
{
	private Engram selected = null;
	private InventoryEngram inventory;

	public ContainerEngram(InventoryEngram inventory)
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
			this.addSlotToContainer(new EngramSlot(
					(InventoryEngram) getScrollableInventory(), i,
					getScrollableSlotsX()
							+ i % getScrollableSlotsWidth() * getSlotSize(),
					getScrollableSlotsY()
							+ i / getScrollableSlotsWidth() * getSlotSize(),
					this));
		}
	}

	@Override
	public ItemStack slotClick(int slotId, int clickedButton, int mode,
			EntityPlayer playerIn)
	{
		if (slotId >= 0)
		{
			Slot s = getSlot(slotId);
			if (s instanceof EngramSlot && clickedButton == 0)
			{
				s.onPickupFromSlot(playerIn,
						playerIn.inventory.getCurrentItem());
				if (mode == 6 && selected != null
						&& ARKPlayer.get(playerIn)
								.canLearnEngram(selected.getId())
						&& !playerIn.worldObj.isRemote)
				{
					enchantItem(playerIn, 1);
				}
				return playerIn.inventory.getCurrentItem();
			}
		}
		return super.slotClick(slotId, clickedButton, mode, playerIn);
	}

	@Override
	public boolean enchantItem(EntityPlayer playerIn, int id)
	{
		if (id == 0)
		{
			playerIn.openGui(ARKCraft.instance(), CommonProxy.GUI.PLAYER.id,
					playerIn.worldObj, 0, 0, 0);
			return true;
		}
		else if (id == 1)
		{
			ARKPlayer p = ARKPlayer.get(playerIn);
			p.learnEngram(selected.getId());
			ARKCraft.modChannel.sendTo(new UpdateEngrams(p.getUnlockedEngrams(),
					p.getEngramPoints()), (EntityPlayerMP) playerIn);
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
			if (getSlotIndex() < inventory.getSizeInventory())
				return EngramManager.instance()
						.getEngram((short) getSlotIndex());
			return null;
		}

		@Override
		public int getSlotStackLimit()
		{
			return 1;
		}

		@Override
		public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
		{
			ContainerEngram.this.setSelected(getEngram());
		}
	}

	public void setSelected(Engram engram)
	{
		selected = engram;
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
