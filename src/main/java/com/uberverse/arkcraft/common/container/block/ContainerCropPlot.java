package com.uberverse.arkcraft.common.container.block;

import com.uberverse.arkcraft.common.tileentity.crafter.TileEntityCropPlot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerCropPlot extends Container
{
    private TileEntityCropPlot te;
    private int waterLast = -1, fertilizerLast = -1;

    public ContainerCropPlot(InventoryPlayer inventory, TileEntityCropPlot tileEntity)
    {
        this.te = tileEntity;
        int id = 0;
        int y = 26;
        for (int i = 0; i < 5; i++) {
            addSlotToContainer(new SlotCropPlot(tileEntity, id + i, i * 18 + 44, y));
        }
        y += 18;
        id += 5;
        for (int i = 0; i < 5; i++) {
            addSlotToContainer(new SlotCropPlot(tileEntity, id + i, i * 18 + 44, y));
        }
        addPlayerSlots(inventory, 8, 84);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return te.isUsableByPlayer(playerIn);
    }

    public void addPlayerSlots(InventoryPlayer playerInventory, int x, int y)
    {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, x + j * 18, y + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            addSlotToContainer(new Slot(playerInventory, i, x + i * 18, y + 58));
        }
    }

    public static class SlotCropPlot extends Slot
    {

        public SlotCropPlot(IInventory inventoryIn, int index, int xPosition, int yPosition)
        {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack)
        {
            return inventory.isItemValidForSlot(getSlotIndex(), stack);
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int sourceSlotIndex)
    {
        Slot sourceSlot = (Slot) inventorySlots.get(sourceSlotIndex);
        if (sourceSlot == null || !sourceSlot.getHasStack())
            return null;
        ItemStack sourceStack = sourceSlot.getStack();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (sourceSlotIndex >= 10 && sourceSlotIndex < 10 + 36) {
            if (!mergeItemStack(sourceStack, 0, 9 + 1, false)) {
                return null;
            }
        }
        else if (sourceSlotIndex >= 0 && sourceSlotIndex < 9 + 1) {
            if (!mergeItemStack(sourceStack, 10, 10 + 36, false)) {
                return null;
            }
        }
        else {
            System.err.print("Invalid slotIndex:" + sourceSlotIndex);
            return null;
        }

        if (sourceStack.stackSize == 0) {
            sourceSlot.putStack(null);
        }
        else {
            sourceSlot.onSlotChanged();
        }

        sourceSlot.onPickupFromSlot(player, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        for (IContainerListener crafter : listeners) {
            if (waterLast != te.getField(0)) {
                crafter.sendProgressBarUpdate(this, 0, te.getField(0));
            }
            if (fertilizerLast != te.getField(1)) {
                crafter.sendProgressBarUpdate(this, 1, te.getField(1));
            }
        }
        waterLast = te.getField(0);
        fertilizerLast = te.getField(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        te.setField(id, data);
    }
}
