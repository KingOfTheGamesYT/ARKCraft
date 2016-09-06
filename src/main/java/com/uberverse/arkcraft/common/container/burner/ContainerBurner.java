package com.uberverse.arkcraft.common.container.burner;

import java.util.Arrays;

import com.uberverse.arkcraft.common.burner.IBurner;
import com.uberverse.arkcraft.common.burner.IBurnerContainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;

public abstract class ContainerBurner extends Container implements IBurnerContainer
{
	private IBurner burner;
	private EntityPlayer player;

	public ContainerBurner(IBurner burner, EntityPlayer player)
	{
		this.burner = burner;
		this.player = player;
		init();
	}

	private void init()
	{
		// Burner slots
		for (int i = 0; i < burner.getIInventory().getSizeInventory(); i++)
		{
			addSlotToContainer(new Slot(burner.getIInventory(), i, getSlotsX() + 18 * i, getSlotsY() + i / getSlotsWidth() * 18));
		}

		// Player hotbar
		for (int x = 0; x < 9; x++)
		{
			addSlotToContainer(new Slot(player.inventory, x, getPlayerHotbarSlotsX() + 18 * x, getPlayerHotbarSlotsY()));
		}

		// Player main inventory
		for (int y = 0; y < 3; y++)
		{
			for (int x = 0; x < 9; x++)
			{
				int slotIndex = 9 + y * 9 + x;
				int xpos = getPlayerInventorySlotsX() + x * 18;
				int ypos = getPlayerInventorySlotsY() + y * 18;
				addSlotToContainer(new Slot(player.inventory, slotIndex, xpos, ypos));
			}
		}
	}

	private int[] cachedFields;

	@Override
	public void updateProgressBar(int id, int data)
	{
		burner.getIInventory().setField(id, data);
		super.updateProgressBar(id, data);
	}

	@Override
	public boolean enchantItem(EntityPlayer playerIn, int id)
	{
		if (id == 0)
		{
			boolean newBurning = !burner.isBurning();
			return burner.updateIsBurning(newBurning) == newBurning;
		}
		return super.enchantItem(playerIn, id);
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		boolean allFieldsHaveChanged = false;
		boolean fieldHasChanged[] = new boolean[burner.getIInventory().getFieldCount()];
		if (cachedFields == null)
		{
			cachedFields = new int[burner.getIInventory().getFieldCount()];
			allFieldsHaveChanged = true;
		}
		else if (cachedFields.length != burner.getIInventory().getFieldCount())
		{
			cachedFields = Arrays.copyOf(cachedFields, burner.getIInventory().getFieldCount());
		}
		for (int i = 0; i < cachedFields.length; i++)
		{
			if (allFieldsHaveChanged || cachedFields[i] != burner.getIInventory().getField(i))
			{
				cachedFields[i] = burner.getIInventory().getField(i);
				fieldHasChanged[i] = true;
			}
		}

		for (int i = 0; i < this.crafters.size(); ++i)
		{
			ICrafting icrafting = (ICrafting) this.crafters.get(i);
			for (int fieldID = 0; fieldID < burner.getIInventory().getFieldCount(); ++fieldID)
			{
				if (fieldHasChanged[fieldID])
				{
					icrafting.sendProgressBarUpdate(this, fieldID, cachedFields[fieldID]);
				}
			}
		}
	}

	@Override
	public IBurner getBurner()
	{
		return burner;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return burner.getIInventory().isUseableByPlayer(playerIn);
	}

	public abstract int getSlotsX();

	public abstract int getSlotsY();

	public abstract int getSlotsWidth();

	public abstract int getPlayerInventorySlotsX();

	public abstract int getPlayerInventorySlotsY();

	public abstract int getPlayerHotbarSlotsX();

	public abstract int getPlayerHotbarSlotsY();
}
