package com.arkcraft.common.container.player;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.arkplayer.ARKPlayer;
import com.arkcraft.common.container.scrollable.ContainerScrollable;
import com.arkcraft.common.container.scrollable.SlotScrolling;
import com.arkcraft.common.engram.EngramManager;
import com.arkcraft.common.inventory.InventoryEngram;
import com.arkcraft.common.network.UpdateEngrams;
import com.arkcraft.common.proxy.CommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author ERBF
 * @author Lewis_McReu
 */
public class ContainerEngram extends ContainerScrollable {
	private EngramManager.Engram selected = null;
	private InventoryEngram inventory;

	public ContainerEngram(InventoryEngram inventory) {
		super();
		this.inventory = inventory;
		initScrollableSlots();
	}

	@Override
	public void initScrollableSlots() {
		for (int i = 0; i < getVisibleSlotsAmount(); i++) {
			this.addSlotToContainer(new EngramSlot((InventoryEngram) getScrollableInventory(), i, getScrollableSlotsX()
					+ i % getScrollableSlotsWidth() * getSlotSize(), getScrollableSlotsY() + i
					/ getScrollableSlotsWidth() * getSlotSize(), this));
		}
	}

	@Override
	public ItemStack slotClick(int slotId, int clickedButton, ClickType mode, EntityPlayer playerIn) {
		if (slotId >= 0) {
			Slot s = getSlot(slotId);
			if (s instanceof EngramSlot && clickedButton == 0) {
				s.onTake(playerIn, playerIn.inventory.getCurrentItem());
				//TODO: check
				if (mode == ClickType.PICKUP_ALL && selected != null && ARKPlayer.get(playerIn).canLearnEngram(selected.getId())
						&& !playerIn.world.isRemote) {
					enchantItem(playerIn, 1);
				}
				return playerIn.inventory.getCurrentItem();
			}
		}
		return super.slotClick(slotId, clickedButton, mode, playerIn);
	}

	@Override
	public boolean enchantItem(EntityPlayer playerIn, int id) {
		if (id == 0) {
			playerIn.openGui(ARKCraft.instance(), CommonProxy.GUI.PLAYER.id, playerIn.world, 0, 0, 0);
			return true;
		} else if (id == 1) {
			ARKPlayer p = ARKPlayer.get(playerIn);
			p.learnEngram(selected.getId());
			ARKCraft.modChannel.sendTo(new UpdateEngrams(p.getUnlockedEngrams(), p.getEngramPoints()),
					(EntityPlayerMP) playerIn); // TODO still necessary?
			return true;
		}
		return super.enchantItem(playerIn, id);
	}

	public void setSelected(EngramManager.Engram engram) {
		selected = engram;
	}

	public EngramManager.Engram getSelectedEngram() {
		return selected;
	}

	@Override
	public int getScrollableSlotsWidth() {
		return 8;
	}

	@Override
	public int getScrollableSlotsHeight() {
		return 4;
	}

	@Override
	public int getScrollableSlotsX() {
		return 1;
	}

	@Override
	public int getScrollableSlotsY() {
		return 55;
	}

	@Override
	public int getSlotSize() {
		return 20;
	}

	@Override
	public IInventory getScrollableInventory() {
		return inventory;
	}

	public class EngramSlot extends SlotScrolling {

		public EngramSlot(InventoryEngram inventoryIn, int index, int xPosition, int yPosition, ContainerEngram container) {
			super(inventoryIn, index, xPosition, yPosition, container);
		}

		public EngramManager.Engram getEngram() {
			if (getSlotIndex() < inventory.getSizeInventory()) return EngramManager.instance().getEngram(
					(short) getSlotIndex());
			return null;
		}

		@Override
		public int getSlotStackLimit() {
			return 1;
		}

		@Override
		public ItemStack onTake(EntityPlayer playerIn, ItemStack stack) {
			ContainerEngram.this.setSelected(getEngram());
			return null;
		}
	}
}
