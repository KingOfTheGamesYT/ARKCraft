package com.arkcraft.common.container.burner;

import com.arkcraft.common.burner.IBurner;
import com.arkcraft.common.burner.IBurnerContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.Arrays;

public abstract class ContainerBurner extends Container implements IBurnerContainer {
	private IBurner burner;
	private EntityPlayer player;
	private int slotStart;
	private int slotEnd;
	private int playerSlotStart;
	private int[] cachedFields;

	public ContainerBurner(IBurner burner, EntityPlayer player) {
		this.burner = burner;
		this.player = player;
		init();
	}

	private void init() {
		int slotCounter = 0;
		slotStart = slotCounter;
		// Burner slots
		for (int i = 0; i < burner.getIInventory().getSizeInventory(); i++) {
			addSlotToContainer(new Slot(burner.getIInventory(), i, getSlotsX() + 18 * (i % getSlotsWidth()), getSlotsY() + i / getSlotsWidth() * 18));
			slotCounter++;
		}

		slotEnd = slotCounter;
		playerSlotStart = slotCounter;
		// Player hotbar
		for (int x = 0; x < 9; x++) {
			addSlotToContainer(new Slot(player.inventory, x, getPlayerHotbarSlotsX() + 18 * x, getPlayerHotbarSlotsY()));
		}

		// Player main inventory
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				int slotIndex = 9 + y * 9 + x;
				int xpos = getPlayerInventorySlotsX() + x * 18;
				int ypos = getPlayerInventorySlotsY() + y * 18;
				addSlotToContainer(new Slot(player.inventory, slotIndex, xpos, ypos));
			}
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		if (player.world.isRemote)
			return null;
		Slot sourceSlot = (Slot) inventorySlots.get(index);
		if (sourceSlot == null || !sourceSlot.getHasStack())
			return null;
		ItemStack sourceStack = sourceSlot.getStack();
		ItemStack copyOfSourceStack = sourceStack.copy();

		if (index >= playerSlotStart && index < playerSlotStart + 36) {
			if (!mergeItemStack(sourceStack, slotStart, slotEnd, false)) {
				return null;
			}
		} else if (index >= slotStart && index < slotEnd + 1) {
			if (!mergeItemStack(sourceStack, playerSlotStart, playerSlotStart + 36, false)) {
				return null;
			}
		} else {
			System.err.print("Invalid slotIndex:" + index);
			return null;
		}

		if (sourceStack.getCount() == 0) {
			sourceSlot.putStack(null);
		} else {
			sourceSlot.onSlotChanged();
		}
		// sourceSlot.onPickupFromSlot(player, sourceStack);
		return copyOfSourceStack;
	}

	@Override
	public void updateProgressBar(int id, int data) {
		burner.getIInventory().setField(id, data);
		super.updateProgressBar(id, data);
	}

	@Override
	public boolean enchantItem(EntityPlayer playerIn, int id) {
		if (id == 0) {
			boolean newBurning = !burner.isBurning();
			return burner.updateIsBurning(newBurning) == newBurning;
		}
		return super.enchantItem(playerIn, id);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		boolean allFieldsHaveChanged = false;
		boolean fieldHasChanged[] = new boolean[burner.getIInventory().getFieldCount()];
		if (cachedFields == null) {
			cachedFields = new int[burner.getIInventory().getFieldCount()];
			allFieldsHaveChanged = true;
		} else if (cachedFields.length != burner.getIInventory().getFieldCount()) {
			cachedFields = Arrays.copyOf(cachedFields, burner.getIInventory().getFieldCount());
		}
		for (int i = 0; i < cachedFields.length; i++) {
			if (allFieldsHaveChanged || cachedFields[i] != burner.getIInventory().getField(i)) {
				cachedFields[i] = burner.getIInventory().getField(i);
				fieldHasChanged[i] = true;
			}
		}

		for (int i = 0; i < this.listeners.size(); ++i) {
			IContainerListener icrafting = this.listeners.get(i);
			for (int fieldID = 0; fieldID < burner.getIInventory().getFieldCount(); ++fieldID) {
				if (fieldHasChanged[fieldID]) {
					icrafting.sendWindowProperty(this, fieldID, cachedFields[fieldID]);
				}
			}
		}
	}

	@Override
	public IBurner getBurner() {
		return burner;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return burner.getIInventory().isUsableByPlayer(playerIn);
	}

	public abstract int getSlotsX();

	public abstract int getSlotsY();

	public abstract int getSlotsWidth();

	public abstract int getPlayerInventorySlotsX();

	public abstract int getPlayerInventorySlotsY();

	public abstract int getPlayerHotbarSlotsX();

	public abstract int getPlayerHotbarSlotsY();
}
