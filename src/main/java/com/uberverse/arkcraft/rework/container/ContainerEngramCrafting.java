package com.uberverse.arkcraft.rework.container;

import java.util.Collections;
import java.util.List;

import com.uberverse.arkcraft.common.container.scrollable.IContainerScrollable;
import com.uberverse.arkcraft.common.container.scrollable.SlotScrolling;
import com.uberverse.arkcraft.rework.engram.EngramManager;
import com.uberverse.arkcraft.rework.engram.EngramManager.Engram;
import com.uberverse.arkcraft.rework.engram.EngramManager.EngramType;
import com.uberverse.arkcraft.rework.engram.IEngramCrafter;
import com.uberverse.arkcraft.rework.itemquality.Qualitable;
import com.uberverse.arkcraft.rework.itemquality.Qualitable.ItemQuality;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;

public abstract class ContainerEngramCrafting extends ContainerScrollable
{
	private static final int SLOT_SIZE = 18;

	private EntityPlayer player;
	private EngramInventory engramInventory;
	private IEngramCrafter crafter;

	protected int playerInvBoundLeft, playerInvBoundRight, invBoundLeft, invBoundRight, scrollInvBoundLeft, scrollInvBoundRight;

	private int counter = 0;

	public ContainerEngramCrafting(EngramType type, EntityPlayer player, IEngramCrafter crafter)
	{
		super();
		this.player = player;
		this.crafter = crafter;
		this.engramInventory = new EngramInventory(EngramManager.instance().getUnlockedEngramsOfType(player, type));
		initPlayerSlots();
		initInventorySlots();
		initQueue();
		initScrollableSlots();
		if (player instanceof EntityPlayerMP) detectAndSendChanges();
	}

	protected final void initInventorySlots()
	{
		invBoundLeft = counter;
		for (int row = 0; row < getInventorySlotsHeight(); row++)
		{
			for (int col = 0; col < getInventorySlotsWidth(); col++)
			{
				int index = col + row * getInventorySlotsWidth();
				this.addSlotToContainer(
						new Slot(getIInventory(), index, getInventorySlotsX() + col * getSlotSize(), getInventorySlotsY() + row * getSlotSize()));
				counter++;
			}
		}
		invBoundRight = counter;
	}

	private final void initQueue()
	{

	}

	private final void initPlayerSlots()
	{
		playerInvBoundLeft = counter;
		for (int row = 0; row < 3; row++)
		{
			for (int col = 0; col < 9; col++)
			{
				int slotIndex = col + row * 9 + 9;
				addSlotToContainer(new Slot(player.inventory, slotIndex, getPlayerInventorySlotsX() + col * getSlotSize(),
						getPlayerInventorySlotsY() + row * getSlotSize()));
				counter++;
			}
		}

		for (int col = 0; col < 9; col++)
		{
			addSlotToContainer(new Slot(player.inventory, col, getPlayerHotbarSlotsX() + col * getSlotSize(), getPlayerHotbarSlotsY()));
			counter++;
		}

		playerInvBoundRight = counter;
	}

	@Override
	public void initScrollableSlots()
	{
		scrollInvBoundLeft = counter;
		for (int i = 0; i < getVisibleSlotsAmount(); i++)
		{
			this.addSlotToContainer(new EngramSlot(engramInventory, i, getScrollableSlotsX() + i % getScrollableSlotsWidth() * getSlotSize(),
					getScrollableSlotsY() + i / getScrollableSlotsWidth() * getSlotSize(), this));
			counter++;
		}
		scrollInvBoundRight = counter;
	}

	public abstract int getPlayerInventorySlotsX();

	public abstract int getPlayerInventorySlotsY();

	public abstract int getPlayerHotbarSlotsX();

	public abstract int getPlayerHotbarSlotsY();

	public abstract int getInventorySlotsX();

	public abstract int getInventorySlotsY();

	public abstract int getInventorySlotsWidth();

	public abstract int getInventorySlotsHeight();

	public IInventory getIInventory()
	{
		return crafter.getIInventory();
	}

	public IEngramCrafter getCrafter()
	{
		return crafter;
	}

	@Override
	public int getSlotSize()
	{
		return SLOT_SIZE;
	}

	@Override
	public IInventory getScrollableInventory()
	{
		return engramInventory;
	}

	public IInventory getPlayerInventory()
	{
		return player.inventory;
	}

	private short selectedEngramId = -1;
	private ItemQuality targetQuality;

	public void craftOne()
	{
		if (selectedEngramId >= 0)
		{
			if (crafter.startCraft(selectedEngramId, targetQuality)) detectAndSendChanges();
		}
	}

	public void craftAll()
	{
		if (selectedEngramId >= 0)
		{
			if (crafter.startCraftAll(selectedEngramId, targetQuality)) detectAndSendChanges();
		}
	}

	public int progress;

	@Override
	public void updateProgressBar(int id, int data)
	{
		if (id == 0) progress = data;
		crafter.setField(id, data);
		super.updateProgressBar(id, data);
	}

	private int[] cachedFields;
	private int fieldCount = -1;

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		boolean allFieldsHaveChanged = false;
		boolean fieldHasChanged[] = new boolean[crafter.getFieldCount()];
		if (cachedFields == null)
		{
			cachedFields = new int[crafter.getFieldCount()];
			allFieldsHaveChanged = true;
		}
		if (fieldCount != crafter.getFieldCount())
		{
			fieldCount = crafter.getFieldCount();
			allFieldsHaveChanged = true;
		}
		for (int i = 0; i < cachedFields.length; ++i)
		{
			if (allFieldsHaveChanged || cachedFields[i] != crafter.getField(i))
			{
				cachedFields[i] = crafter.getField(i);
				fieldHasChanged[i] = true;
			}
		}

		// go through the list of crafters (players using this container) and
		// update them if necessary
		for (int i = 0; i < this.crafters.size(); ++i)
		{
			ICrafting icrafting = (ICrafting) this.crafters.get(i);
			for (int fieldID = 0; fieldID < crafter.getFieldCount(); ++fieldID)
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

	@Override
	public boolean enchantItem(EntityPlayer playerIn, int id)
	{
		if (id == 0)
		{
			craftOne();
			return true;
		}
		else if (id == 1)
		{
			craftAll();
			return true;
		}
		else return super.enchantItem(playerIn, id);
	}

	@Override
	public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer playerIn)
	{
		if (slotId >= 0)
		{
			Slot s = getSlot(slotId);
			if (s instanceof EngramSlot)
			{
				s.onPickupFromSlot(playerIn, player.inventory.getCurrentItem());
				return player.inventory.getCurrentItem();
			}
		}
		return super.slotClick(slotId, clickedButton, mode, playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		if (getPlayerInventory() != null)
		{
			if (getIInventory() != null && getIInventory().getSizeInventory() > 0)
			{
				ItemStack itemstack = null;
				Slot slot = (Slot) this.inventorySlots.get(index);

				if (slot != null && slot.getHasStack())
				{
					ItemStack itemstack1 = slot.getStack();
					itemstack = itemstack1.copy();
					if (slot.inventory == getIInventory())
					{
						if (!this.mergeItemStack(itemstack1, playerInvBoundLeft, playerInvBoundRight, false)) return null;
					}
					else if (slot.inventory == getPlayerInventory())
						if (!this.mergeItemStack(itemstack1, invBoundLeft, invBoundRight, false)) return null;
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
			else
			{
				ItemStack itemstack = null;
				Slot slot = (Slot) this.inventorySlots.get(index);

				if (slot != null && slot.getHasStack())
				{
					ItemStack itemstack1 = slot.getStack();
					itemstack = itemstack1.copy();

					if (index >= 0 && index < 27)
					{
						if (!this.mergeItemStack(itemstack1, 27, 36, false)) { return null; }
					}
					else if (index >= 27 && index < 36)
					{
						if (!this.mergeItemStack(itemstack1, 0, 27, false)) { return null; }
					}

					if (itemstack1.stackSize == 0)
					{
						slot.putStack((ItemStack) null);
					}
					else
					{
						slot.onSlotChanged();
					}

					if (itemstack1.stackSize == itemstack.stackSize) { return null; }
				}
			}
		}

		return null;
	}

	public class EngramSlot extends SlotScrolling
	{
		public EngramSlot(EngramInventory inventoryIn, int index, int xPosition, int yPosition, IContainerScrollable container)
		{
			super(inventoryIn, index, xPosition, yPosition, container);
		}

		public Engram getEngram()
		{
			return ContainerEngramCrafting.this.engramInventory.getEngram(getSlotIndex());
		}

		@Override
		public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
		{
			ContainerEngramCrafting.this.selectedEngramId = getEngram().getId();
			ContainerEngramCrafting.this.targetQuality = ItemQuality.PRIMITIVE;
		}
	}

	public class BluePrintSlot extends Slot
	{
		public BluePrintSlot(IInventory inventoryIn, int index, int xPosition, int yPosition)
		{
			super(inventoryIn, index, xPosition, yPosition);
		}

		public Engram getEngram()
		{
			return ContainerEngramCrafting.this.engramInventory.getEngram(getSlotIndex());
		}

		public ItemQuality getItemQuality()
		{
			ItemStack stack = getStack();
			if (stack != null && stack.getItem() instanceof Qualitable) { return Qualitable.get(stack); }
			return null;
		}

		@Override
		public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
		{
			ContainerEngramCrafting.this.selectedEngramId = getEngram().getId();
			ContainerEngramCrafting.this.targetQuality = getItemQuality();
		}
	}

	public static class EngramInventory implements IInventory
	{
		private List<Engram> engrams;

		public EngramInventory(List<Engram> engrams)
		{
			this.engrams = engrams;
			Collections.sort(this.engrams);
		}

		public Engram getEngram(int index)
		{
			return engrams.get(index);
		}

		@Override
		public String getName()
		{
			return "";
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
		public int getSizeInventory()
		{
			return engrams.size();
		}

		@Override
		public ItemStack getStackInSlot(int index)
		{
			return new ItemStack(engrams.get(index).getItem());
		}

		@Override
		public ItemStack decrStackSize(int index, int count)
		{
			return null;
		}

		@Override
		public ItemStack getStackInSlotOnClosing(int index)
		{
			return null;
		}

		@Override
		public void setInventorySlotContents(int index, ItemStack stack)
		{}

		@Override
		public int getInventoryStackLimit()
		{
			return Integer.MAX_VALUE;
		}

		@Override
		public void markDirty()
		{}

		@Override
		public boolean isUseableByPlayer(EntityPlayer player)
		{
			return false;
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
			return false;
		}

		@Override
		public int getField(int id)
		{
			return 0;
		}

		@Override
		public void setField(int id, int value)
		{}

		@Override
		public int getFieldCount()
		{
			return 0;
		}

		@Override
		public void clear()
		{}
	}
}
