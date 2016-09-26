package com.uberverse.arkcraft.common.container.engram;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

import com.uberverse.arkcraft.common.container.player.ContainerPlayerCrafting;
import com.uberverse.arkcraft.common.container.scrollable.ContainerScrollable;
import com.uberverse.arkcraft.common.container.scrollable.IContainerScrollable;
import com.uberverse.arkcraft.common.container.scrollable.SlotScrolling;
import com.uberverse.arkcraft.common.engram.CraftingOrder;
import com.uberverse.arkcraft.common.engram.EngramManager;
import com.uberverse.arkcraft.common.engram.EngramManager.Engram;
import com.uberverse.arkcraft.common.engram.EngramManager.EngramType;
import com.uberverse.arkcraft.common.engram.IEngramCrafter;
import com.uberverse.arkcraft.common.item.ItemBlueprint;
import com.uberverse.arkcraft.common.item.Qualitable.ItemQuality;

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
	private QueueInventory queueInventory;
	private IEngramCrafter crafter;

	protected int playerInvBoundLeft, playerInvBoundRight, invBoundLeft, invBoundRight, scrollInvBoundLeft,
			scrollInvBoundRight;

	private int counter = 0;

	public ContainerEngramCrafting(EngramType type, EntityPlayer player, IEngramCrafter crafter)
	{
		super();
		this.player = player;
		this.crafter = crafter;
		this.engramInventory = new EngramInventory(EngramManager.instance().getUnlockedEngramsOfType(player, type));
		this.queueInventory = new QueueInventory(crafter.getCraftingQueue());
		initPlayerSlots();
		initInventorySlots();
		initQueue();
		initScrollableSlots();
		if (player instanceof EntityPlayerMP)
		{
			((EntityPlayerMP) player).func_175173_a(this, engramInventory);
			((EntityPlayerMP) player).func_175173_a(this, queueInventory);
		}
	}

	private final void initInventorySlots()
	{
		invBoundLeft = counter;
		for (int row = 0; row < getInventorySlotsHeight(); row++)
		{
			for (int col = 0; col < getInventorySlotsWidth(); col++)
			{
				int index = col + row * getInventorySlotsWidth();
				Slot slot = new Slot(getIInventory(), index, getInventorySlotsX() + col * getSlotSize(),
						getInventorySlotsY() + row * getSlotSize());
				if (getPlayerInventory() == getBlueprintInventory()) slot = new BlueprintSlot(slot.inventory, slot
						.getSlotIndex(), slot.xDisplayPosition, slot.yDisplayPosition);
				this.addSlotToContainer(slot);
				counter++;
			}
		}
		invBoundRight = counter;
	}

	private final void initQueue()
	{
		for (int row = 0; row < getQueueSlotsHeight(); row++)
		{
			for (int col = 0; col < getQueueSlotsWidth(); col++)
			{
				int index = col + row * getQueueSlotsWidth();
				this.addSlotToContainer(new QueueSlot(getQueueInventory(), index, getQueueSlotsX() + col
						* getSlotSize(), getQueueSlotsY() + row * getSlotSize()));
				counter++;
			}
		}
	}

	private final void initPlayerSlots()
	{
		playerInvBoundLeft = counter;
		for (int row = 0; row < 3; row++)
		{
			for (int col = 0; col < 9; col++)
			{
				int slotIndex = col + row * 9 + 9;
				Slot slot = new Slot(getPlayerInventory(), slotIndex, getPlayerInventorySlotsX() + col * getSlotSize(),
						getPlayerInventorySlotsY() + row * getSlotSize());
				if (getPlayerInventory() == getBlueprintInventory()) slot = new BlueprintSlot(slot.inventory, slot
						.getSlotIndex(), slot.xDisplayPosition, slot.yDisplayPosition);
				addSlotToContainer(slot);
				counter++;
			}
		}

		for (int col = 0; col < 9; col++)
		{
			Slot slot = new Slot(player.inventory, col, getPlayerHotbarSlotsX() + col * getSlotSize(),
					getPlayerHotbarSlotsY());
			if (this instanceof ContainerPlayerCrafting) slot = new BlueprintSlot(slot.inventory, slot.getSlotIndex(),
					slot.xDisplayPosition, slot.yDisplayPosition);
			addSlotToContainer(slot);
			counter++;
		}

		playerInvBoundRight = counter;
	}

	protected abstract IInventory getBlueprintInventory();

	@Override
	public void initScrollableSlots()
	{
		scrollInvBoundLeft = counter;
		for (int i = 0; i < getVisibleSlotsAmount(); i++)
		{
			this.addSlotToContainer(new EngramSlot(engramInventory, i, getScrollableSlotsX() + i
					% getScrollableSlotsWidth() * getSlotSize(), getScrollableSlotsY() + i / getScrollableSlotsWidth()
							* getSlotSize(), this));
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

	public abstract int getQueueSlotsWidth();

	public abstract int getQueueSlotsHeight();

	public abstract int getQueueSlotsX();

	public abstract int getQueueSlotsY();

	public IInventory getIInventory()
	{
		return crafter.getIInventory();
	}

	public IInventory getQueueInventory()
	{
		return queueInventory;
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

	private Engram selectedEngram = null;
	private ItemQuality targetQuality;

	/**
	 * @return the selectedEngram
	 */
	public Engram getSelectedEngram()
	{
		return selectedEngram;
	}

	public void craftOne()
	{
		if (selectedEngram != null)
		{
			if (crafter.startCraft(selectedEngram, targetQuality)) detectAndSendChanges();
		}
	}

	public void craftAll()
	{
		if (selectedEngram != null)
		{
			if (crafter.startCraftAll(selectedEngram, targetQuality)) detectAndSendChanges();
		}
	}

	@Override
	public void updateProgressBar(int id, int data)
	{
		crafter.setField(id, data);
		super.updateProgressBar(id, data);
	}

	private int[] cachedFields;

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
		else if (cachedFields.length != crafter.getFieldCount())
		{
			cachedFields = Arrays.copyOf(cachedFields, crafter.getFieldCount());
		}
		for (int i = 0; i < cachedFields.length; i++)
		{
			if (allFieldsHaveChanged || cachedFields[i] != crafter.getField(i))
			{
				cachedFields[i] = crafter.getField(i);
				fieldHasChanged[i] = true;
			}
		}

		for (int i = 0; i < this.crafters.size(); ++i)
		{
			ICrafting icrafting = (ICrafting) this.crafters.get(i);
			for (int fieldID = 0; fieldID < crafter.getFieldCount(); ++fieldID)
			{
				if (fieldHasChanged[fieldID])
				{
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
			if (s instanceof EngramSlot && clickedButton == 0)
			{
				s.onPickupFromSlot(playerIn, playerIn.inventory.getCurrentItem());
				if (mode == 6) craftOne();
				return playerIn.inventory.getCurrentItem();
			}
			else if (s instanceof QueueSlot)
			{
				QueueSlot q = (QueueSlot) s;
				if (clickedButton == 1)
				{
					CraftingOrder c = q.getCraftingOrder();
					if (c != null)
					{
						if (c.isQualitable()) crafter.cancelCraftAll(c.getEngram(), c.getItemQuality());
						else crafter.cancelCraftAll(c.getEngram());
						return playerIn.inventory.getCurrentItem();
					}
				}
				else if (clickedButton == 0)
				{
					s.onPickupFromSlot(playerIn, playerIn.inventory.getCurrentItem());
					return playerIn.inventory.getCurrentItem();
				}
			}
			else if (s instanceof BlueprintSlot)
			{
				BlueprintSlot b = (BlueprintSlot) s;
				if (listenPutDown)
				{
					selectedBlueprintIndex = b.slotNumber;
					listenPutDown = false;
					return super.slotClick(slotId, clickedButton, mode, playerIn);
				}
				if (b.hasBlueprint() && clickedButton == 0)
				{
					b.onPickupFromSlot(playerIn, playerIn.inventory.getCurrentItem());
					if (mode == 6) craftOne();
					return null;
				}
				else if (clickedButton == 1) clickedButton = 0;
				if (b.slotNumber == selectedBlueprintIndex) listenPutDown = b.hasBlueprint();
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
						if (!this.mergeItemStack(itemstack1, playerInvBoundLeft, playerInvBoundRight, false))
							return null;
					}
					else if (slot.inventory == getPlayerInventory()) if (!this.mergeItemStack(itemstack1, invBoundLeft,
							invBoundRight, false)) return null;
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

	public class EngramSlot extends SlotScrolling implements EngramCraftingSlot
	{
		public EngramSlot(EngramInventory inventoryIn, int index, int xPosition, int yPosition, IContainerScrollable container)
		{
			super(inventoryIn, index, xPosition, yPosition, container);
		}

		public Engram getEngram()
		{
			int index = getSlotIndex();
			return index < ContainerEngramCrafting.this.getTotalSlotsAmount()
					? ContainerEngramCrafting.this.engramInventory.getEngram(index) : null;
		}

		@Override
		public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
		{
			ContainerEngramCrafting.this.selectedEngram = getEngram();
			ContainerEngramCrafting.this.targetQuality = getItemQuality();
			ContainerEngramCrafting.this.selectedBlueprintIndex = -1;
		}

		@Override
		public ItemQuality getItemQuality()
		{
			return ItemQuality.PRIMITIVE;
		}
	}

	private int selectedBlueprintIndex = -1;

	public int getSelectedBlueprintIndex()
	{
		return selectedBlueprintIndex;
	}

	private boolean listenPutDown = false;

	public class BlueprintSlot extends Slot implements EngramCraftingSlot
	{
		public BlueprintSlot(IInventory inventoryIn, int index, int xPosition, int yPosition)
		{
			super(inventoryIn, index, xPosition, yPosition);
		}

		public boolean hasBlueprint()
		{
			return getStack() != null ? getStack().getItem() instanceof ItemBlueprint : false;
		}

		public Engram getEngram()
		{
			if (hasBlueprint() && getStack() != null) return ItemBlueprint.getEngram(getStack());
			return null;
		}

		public ItemQuality getItemQuality()
		{
			Engram e = getEngram();
			if (e != null && e.isQualitable()) return ItemBlueprint.getItemQuality(getStack());
			return null;
		}

		@Override
		public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
		{
			if (hasBlueprint())
			{
				ContainerEngramCrafting.this.selectedEngram = getEngram();
				ContainerEngramCrafting.this.targetQuality = getItemQuality();
				ContainerEngramCrafting.this.selectedBlueprintIndex = slotNumber;
			}
		}
	}

	public interface EngramCraftingSlot
	{
		public Engram getEngram();

		public ItemQuality getItemQuality();
	}

	public class QueueSlot extends Slot
	{
		public QueueSlot(IInventory inventoryIn, int index, int xPosition, int yPosition)
		{
			super(inventoryIn, index, xPosition, yPosition);
		}

		public CraftingOrder getCraftingOrder()
		{
			return ContainerEngramCrafting.this.queueInventory.getCraftingOrder(getSlotIndex());
		}

		public Engram getEngram()
		{
			return ContainerEngramCrafting.this.queueInventory.getEngram(getSlotIndex());
		}

		@Override
		public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
		{
			CraftingOrder c = getCraftingOrder();
			if (c != null)
			{
				Engram e = c.getEngram();
				if (c.isQualitable()) ContainerEngramCrafting.this.crafter.cancelCraftOne(e, c.getItemQuality());
				else ContainerEngramCrafting.this.crafter.cancelCraftOne(e);
			}
		}
	}

	public class EngramInventory implements IInventory
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

			ItemStack output = engrams.get(index).getOutputAsItemStack();
			output.stackSize = 1;
			return output;
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

	public class QueueInventory implements IInventory
	{
		private Queue<CraftingOrder> queue;

		public QueueInventory(Queue<CraftingOrder> queue)
		{
			this.queue = queue;
		}

		@Override
		public String getName()
		{
			return null;
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
			return queue.size();
		}

		private CraftingOrder getCraftingOrder(int index)
		{
			if (index >= queue.size()) return null;
			return queue.toArray(new CraftingOrder[0])[index];
		}

		private Engram getEngram(int index)
		{
			if (index >= queue.size()) return null;
			return getCraftingOrder(index).getEngram();
		}

		@Override
		public ItemStack getStackInSlot(int index)
		{
			if (index < getSizeInventory())
			{
				CraftingOrder c = getCraftingOrder(index);
				ItemStack output = c.getEngram().getOutputAsItemStack(c.getItemQuality());
				output.stackSize *= c.getCount();
				return output;
			}
			return null;
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
			return 0;
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
