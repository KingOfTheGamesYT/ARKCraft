package com.arkcraft.common.container.engram;

import com.arkcraft.common.container.player.ContainerPlayerCrafting;
import com.arkcraft.common.container.scrollable.ContainerScrollable;
import com.arkcraft.common.container.scrollable.IContainerScrollable;
import com.arkcraft.common.container.scrollable.SlotScrolling;
import com.arkcraft.common.engram.CraftingOrder;
import com.arkcraft.common.engram.EngramManager;
import com.arkcraft.common.engram.IEngramCrafter;
import com.arkcraft.common.item.ItemBlueprint;
import com.arkcraft.common.item.Qualitable;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

public abstract class ContainerEngramCrafting extends ContainerScrollable {
	private static final int SLOT_SIZE = 18;
	protected int playerInvBoundLeft, playerInvBoundRight, invBoundLeft, invBoundRight, scrollInvBoundLeft, scrollInvBoundRight;
	private EntityPlayer player;
	private EngramInventory engramInventory;
	private QueueInventory queueInventory;
	private IEngramCrafter crafter;
	private int counter = 0;
	private EngramManager.Engram selectedEngram = null;
	private Qualitable.ItemQuality targetQuality;
	private int[] cachedFields;
	private int selectedBlueprintIndex = -1;
	private boolean listenPutDown = false;

	public ContainerEngramCrafting(EngramManager.EngramType type, EntityPlayer player, IEngramCrafter crafter) {
		super();
		this.player = player;
		this.crafter = crafter;
		this.engramInventory = new EngramInventory(EngramManager.instance().getUnlockedEngramsOfType(player, type));
		this.queueInventory = new QueueInventory(crafter.getCraftingQueue());
		initPlayerSlots();
		initInventorySlots();
		initQueue();
		initScrollableSlots();
		if (player instanceof EntityPlayerMP) {
			//TODO Not sure if the queInventory is correct with getInventoryStacks
			//((EntityPlayerMP) player).updateCraftingInventory(this, engramInventory.getInventoryStacks());
			//((EntityPlayerMP) player).updateCraftingInventory(this, queueInventory.getInventoryStacks());
		}
	}

	private final void initInventorySlots() {
		invBoundLeft = counter;
		for (int row = 0; row < getInventorySlotsHeight(); row++) {
			for (int col = 0; col < getInventorySlotsWidth(); col++) {
				int index = col + row * getInventorySlotsWidth();
				Slot slot = new Slot(getIInventory(), index, getInventorySlotsX() + col * getSlotSize(), getInventorySlotsY() + row * getSlotSize());
				if (getPlayerInventory() == getBlueprintInventory())
					slot = new BlueprintSlot(slot.inventory, slot.getSlotIndex(), slot.xPos, slot.yPos);
				this.addSlotToContainer(slot);
				counter++;
			}
		}
		invBoundRight = counter;
	}

	private final void initQueue() {
		for (int row = 0; row < getQueueSlotsHeight(); row++) {
			for (int col = 0; col < getQueueSlotsWidth(); col++) {
				int index = col + row * getQueueSlotsWidth();
				this.addSlotToContainer(new QueueSlot(getQueueInventory(), index, getQueueSlotsX() + col * getSlotSize(), getQueueSlotsY() + row * getSlotSize()));
				counter++;
			}
		}
	}

	private final void initPlayerSlots() {
		playerInvBoundLeft = counter;
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				int slotIndex = col + row * 9 + 9;
				Slot slot = new Slot(getPlayerInventory(), slotIndex, getPlayerInventorySlotsX() + col * getSlotSize(), getPlayerInventorySlotsY() + row * getSlotSize());
				if (getPlayerInventory() == getBlueprintInventory())
					slot = new BlueprintSlot(slot.inventory, slot.getSlotIndex(), slot.xPos, slot.yPos);
				addSlotToContainer(slot);
				counter++;
			}
		}

		for (int col = 0; col < 9; col++) {
			Slot slot = new Slot(player.inventory, col, getPlayerHotbarSlotsX() + col * getSlotSize(), getPlayerHotbarSlotsY());
			if (this instanceof ContainerPlayerCrafting)
				slot = new BlueprintSlot(slot.inventory, slot.getSlotIndex(), slot.xPos, slot.yPos);
			addSlotToContainer(slot);
			counter++;
		}

		playerInvBoundRight = counter;
	}

	protected abstract IInventory getBlueprintInventory();

	@Override
	public void initScrollableSlots() {
		scrollInvBoundLeft = counter;
		for (int i = 0; i < getVisibleSlotsAmount(); i++) {
			this.addSlotToContainer(new EngramSlot(engramInventory, i, getScrollableSlotsX() + i % getScrollableSlotsWidth() * getSlotSize(), getScrollableSlotsY() + i / getScrollableSlotsWidth() * getSlotSize(), this));
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

	public IInventory getIInventory() {
		return crafter.getIInventory();
	}

	public IInventory getQueueInventory() {
		return queueInventory;
	}

	public IEngramCrafter getCrafter() {
		return crafter;
	}

	@Override
	public int getSlotSize() {
		return SLOT_SIZE;
	}

	@Override
	public IInventory getScrollableInventory() {
		return engramInventory;
	}

	public IInventory getPlayerInventory() {
		return player.inventory;
	}

	/**
	 * @return the selectedEngram
	 */
	public EngramManager.Engram getSelectedEngram() {
		return selectedEngram;
	}

	public void craftOne() {
		if (selectedEngram != null) {
			if (crafter.startCraft(selectedEngram, targetQuality))
				detectAndSendChanges();
		}
	}

	public void craftAll() {
		if (selectedEngram != null) {
			if (crafter.startCraftAll(selectedEngram, targetQuality))
				detectAndSendChanges();
		}
	}

	@Override
	public void updateProgressBar(int id, int data) {
		crafter.setField(id, data);
		super.updateProgressBar(id, data);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		boolean allFieldsHaveChanged = false;
		boolean fieldHasChanged[] = new boolean[crafter.getFieldCount()];
		if (cachedFields == null) {
			cachedFields = new int[crafter.getFieldCount()];
			allFieldsHaveChanged = true;
		} else if (cachedFields.length != crafter.getFieldCount()) {
			cachedFields = Arrays.copyOf(cachedFields, crafter.getFieldCount());
		}
		for (int i = 0; i < cachedFields.length; i++) {
			if (allFieldsHaveChanged || cachedFields[i] != crafter.getField(i)) {
				cachedFields[i] = crafter.getField(i);
				fieldHasChanged[i] = true;
			}
		}

		for (int i = 0; i < this.listeners.size(); ++i) {
			IContainerListener icrafting = this.listeners.get(i);
			for (int fieldID = 0; fieldID < crafter.getFieldCount(); ++fieldID) {
				if (fieldHasChanged[fieldID]) {
					icrafting.sendWindowProperty(this, fieldID, cachedFields[fieldID]);
				}
			}
		}
	}

	@Override
	public boolean enchantItem(EntityPlayer playerIn, int id) {
		if (id == 0) {
			craftOne();
			return true;
		} else if (id == 1) {
			craftAll();
			return true;
		} else
			return super.enchantItem(playerIn, id);
	}

	@Override
	public ItemStack slotClick(int slotId, int clickedButton, ClickType mode, EntityPlayer playerIn) {
		if (slotId >= 0) {
			Slot s = getSlot(slotId);
			if (s instanceof EngramSlot && clickedButton == 0) {
				s.onTake(playerIn, playerIn.inventory.getCurrentItem());
				if (mode == ClickType.PICKUP)
					craftOne();
				return playerIn.inventory.getCurrentItem();
			} else if (s instanceof QueueSlot) {
				QueueSlot q = (QueueSlot) s;
				if (clickedButton == 1) {
					CraftingOrder c = q.getCraftingOrder();
					if (c != null) {
						if (c.isQualitable())
							crafter.cancelCraftAll(c.getEngram(), c.getItemQuality());
						else
							crafter.cancelCraftAll(c.getEngram());
						return playerIn.inventory.getCurrentItem();
					}
				} else if (clickedButton == 0) {
					s.onTake(playerIn, playerIn.inventory.getCurrentItem());
					return playerIn.inventory.getCurrentItem();
				}
			} else if (s instanceof BlueprintSlot) {
				BlueprintSlot b = (BlueprintSlot) s;
				if (listenPutDown) {
					selectedBlueprintIndex = b.slotNumber;
					listenPutDown = false;
					return super.slotClick(slotId, clickedButton, mode, playerIn);
				}
				if (b.hasBlueprint() && clickedButton == 0) {
					b.onTake(playerIn, playerIn.inventory.getCurrentItem());
					if (mode == ClickType.PICKUP)
						craftOne();
					return null;
				} else if (clickedButton == 1)
					clickedButton = 0;
				if (b.slotNumber == selectedBlueprintIndex)
					listenPutDown = b.hasBlueprint();
			}
		}
		return super.slotClick(slotId, clickedButton, mode, playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		if (getPlayerInventory() != null) {
			if (getIInventory() != null && getIInventory().getSizeInventory() > 0) {
				ItemStack itemstack = null;
				Slot slot = this.inventorySlots.get(index);

				if (slot != null && slot.getHasStack()) {
					ItemStack itemstack1 = slot.getStack();
					itemstack = itemstack1.copy();
					if (slot.inventory == getIInventory()) {
						if (!this.mergeItemStack(itemstack1, playerInvBoundLeft, playerInvBoundRight, false))
							return null;
					} else if (slot.inventory == getPlayerInventory())
						if (!this.mergeItemStack(itemstack1, invBoundLeft, invBoundRight, false))
							return null;
					if (itemstack1.getCount() == 0) {
						slot.putStack((ItemStack) null);
					} else {
						slot.onSlotChanged();
					}
				}

				return itemstack;
			} else {
				ItemStack itemstack = null;
				Slot slot = this.inventorySlots.get(index);

				if (slot != null && slot.getHasStack()) {
					ItemStack itemstack1 = slot.getStack();
					itemstack = itemstack1.copy();

					if (index >= 0 && index < 27) {
						if (!this.mergeItemStack(itemstack1, 27, 36, false)) {
							return null;
						}
					} else if (index >= 27 && index < 36) {
						if (!this.mergeItemStack(itemstack1, 0, 27, false)) {
							return null;
						}
					}

					if (itemstack1.getCount() == 0) {
						slot.putStack((ItemStack) null);
					} else {
						slot.onSlotChanged();
					}

					if (itemstack1.getCount() == itemstack.getCount()) {
						return null;
					}
				}
			}
		}

		return null;
	}

	public int getSelectedBlueprintIndex() {
		return selectedBlueprintIndex;
	}

	public interface EngramCraftingSlot {
		public EngramManager.Engram getEngram();

		public Qualitable.ItemQuality getItemQuality();
	}

	public class EngramSlot extends SlotScrolling implements EngramCraftingSlot {
		public EngramSlot(EngramInventory inventoryIn, int index, int xPosition, int yPosition, IContainerScrollable container) {
			super(inventoryIn, index, xPosition, yPosition, container);
		}

		@Override
		public EngramManager.Engram getEngram() {
			int index = getSlotIndex();
			return index < ContainerEngramCrafting.this.getTotalSlotsAmount() ? ContainerEngramCrafting.this.engramInventory.getEngram(index) : null;
		}

		@Override
		public ItemStack onTake(EntityPlayer playerIn, ItemStack stack) {
			ContainerEngramCrafting.this.selectedEngram = getEngram();
			ContainerEngramCrafting.this.targetQuality = getItemQuality();
			ContainerEngramCrafting.this.selectedBlueprintIndex = -1;
			return null;
		}

		@Override
		public Qualitable.ItemQuality getItemQuality() {
			return Qualitable.ItemQuality.PRIMITIVE;
		}
	}

	public class BlueprintSlot extends Slot implements EngramCraftingSlot {
		public BlueprintSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		public boolean hasBlueprint() {
			return getStack() != null ? getStack().getItem() instanceof ItemBlueprint : false;
		}

		@Override
		public EngramManager.Engram getEngram() {
			if (hasBlueprint() && getStack() != null)
				return ItemBlueprint.getEngram(getStack());
			return null;
		}

		@Override
		public Qualitable.ItemQuality getItemQuality() {
			EngramManager.Engram e = getEngram();
			if (e != null && e.isQualitable())
				return ItemBlueprint.getItemQuality(getStack());
			return null;
		}

		@Override
		public ItemStack onTake(EntityPlayer playerIn, ItemStack stack) {
			if (hasBlueprint()) {
				ContainerEngramCrafting.this.selectedEngram = getEngram();
				ContainerEngramCrafting.this.targetQuality = getItemQuality();
				ContainerEngramCrafting.this.selectedBlueprintIndex = slotNumber;
			}
			return null;
		}
	}

	public class QueueSlot extends Slot {
		public QueueSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		public CraftingOrder getCraftingOrder() {
			return ContainerEngramCrafting.this.queueInventory.getCraftingOrder(getSlotIndex());
		}

		public EngramManager.Engram getEngram() {
			return ContainerEngramCrafting.this.queueInventory.getEngram(getSlotIndex());
		}

		@Override
		public ItemStack onTake(EntityPlayer playerIn, ItemStack stack) {
			CraftingOrder c = getCraftingOrder();
			if (c != null) {
				EngramManager.Engram e = c.getEngram();
				if (c.isQualitable())
					ContainerEngramCrafting.this.crafter.cancelCraftOne(e, c.getItemQuality());
				else
					ContainerEngramCrafting.this.crafter.cancelCraftOne(e);
			}
			return null;
		}
	}

	public class EngramInventory implements IInventory {
		private List<EngramManager.Engram> engrams;

		public EngramInventory(List<EngramManager.Engram> engrams) {
			this.engrams = engrams;
			Collections.sort(this.engrams);
		}

		public EngramManager.Engram getEngram(int index) {
			return engrams.get(index);
		}

		public List<ItemStack> getInventoryStacks() {
			List<ItemStack> stacks = Lists.newArrayList();

			for (int i = 0; i < getSizeInventory(); i++)
				stacks.add(getStackInSlot(i));

			return stacks;
		}

		@Override
		public String getName() {
			return "";
		}

		@Override
		public boolean hasCustomName() {
			return false;
		}

		@Override
		public ITextComponent getDisplayName() {
			return null;
		}

		@Override
		public int getSizeInventory() {
			return engrams.size();
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public ItemStack getStackInSlot(int index) {
			ItemStack output = engrams.get(index).getOutputAsItemStack();
			output.setCount(1);
			return output;
		}

		@Override
		public ItemStack decrStackSize(int index, int count) {
			return null;
		}

		@Override
		public void setInventorySlotContents(int index, ItemStack stack) {
		}

		@Override
		public int getInventoryStackLimit() {
			return Integer.MAX_VALUE;
		}

		@Override
		public void markDirty() {
		}

		@Override
		public boolean isUsableByPlayer(EntityPlayer player) {
			return false;
		}

		@Override
		public void openInventory(EntityPlayer player) {
		}

		@Override
		public void closeInventory(EntityPlayer player) {
		}

		@Override
		public boolean isItemValidForSlot(int index, ItemStack stack) {
			return false;
		}

		@Override
		public int getField(int id) {
			return 0;
		}

		@Override
		public void setField(int id, int value) {
		}

		@Override
		public int getFieldCount() {
			return 0;
		}

		@Override
		public void clear() {
		}

		@Override
		public ItemStack removeStackFromSlot(int index) {
			return null;
		}
	}

	public class QueueInventory implements IInventory {
		private Queue<CraftingOrder> queue;

		public QueueInventory(Queue<CraftingOrder> queue) {
			this.queue = queue;
		}

		public List<ItemStack> getInventoryStacks() {
			List<ItemStack> stacks = Lists.newArrayList();

			for (int i = 0; i < getSizeInventory(); i++)
				stacks.add(getStackInSlot(i));

			return stacks;
		}

		@Override
		public String getName() {
			return null;
		}

		@Override
		public boolean hasCustomName() {
			return false;
		}

		@Override
		public ITextComponent getDisplayName() {
			return null;
		}

		@Override
		public int getSizeInventory() {
			return queue.size();
		}

		@Override
		public boolean isEmpty() {
			return queue.isEmpty();
		}

		private CraftingOrder getCraftingOrder(int index) {
			if (index >= queue.size())
				return null;
			return queue.toArray(new CraftingOrder[0])[index];
		}

		private EngramManager.Engram getEngram(int index) {
			if (index >= queue.size())
				return null;
			return getCraftingOrder(index).getEngram();
		}

		@Override
		public ItemStack getStackInSlot(int index) {
			if (index < getSizeInventory()) {
				CraftingOrder c = getCraftingOrder(index);
				ItemStack output = c.getEngram().getOutputAsItemStack(c.getItemQuality());
				output.setCount(output.getCount() * c.getCount());
				return output;
			}
			return null;
		}

		@Override
		public ItemStack decrStackSize(int index, int count) {
			return null;
		}

		@Override
		public void setInventorySlotContents(int index, ItemStack stack) {
		}

		@Override
		public int getInventoryStackLimit() {
			return 0;
		}

		@Override
		public void markDirty() {
		}

		@Override
		public boolean isUsableByPlayer(EntityPlayer player) {
			return false;
		}

		@Override
		public void openInventory(EntityPlayer player) {
		}

		@Override
		public void closeInventory(EntityPlayer player) {
		}

		@Override
		public boolean isItemValidForSlot(int index, ItemStack stack) {
			return false;
		}

		@Override
		public int getField(int id) {
			return 0;
		}

		@Override
		public void setField(int id, int value) {
		}

		@Override
		public int getFieldCount() {
			return 0;
		}

		@Override
		public void clear() {
		}

		@Override
		public ItemStack removeStackFromSlot(int index) {
			return null;
		}
	}
}
