package com.arkcraft.common.tileentity.crafter.burner;

import com.arkcraft.common.burner.BurnerManager;
import com.arkcraft.init.ARKCraftItems;
import net.minecraft.item.ItemStack;

/**
 * @author Lewis_McReu
 */
public class TileEntityCampfire extends TileEntityBurner {
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return stack != null ? stack.getItem() == ARKCraftItems.meat_raw || stack
				.getItem() == ARKCraftItems.primemeat_raw || super.isItemValidForSlot(index, stack) : false;
	}

	@Override
	public int getSizeInventory() {
		return 4;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public BurnerManager.BurnerType getBurnerType() {
		return BurnerManager.BurnerType.CAMPFIRE;
	}

	@Override
	public String getOnSoundName() {
		return "arkcraft:on";
	}

	@Override
	public String getOffSoundName() {
		return "arkcraft:off";
	}

	@Override
	public String getLightSoundName() {
		return "arkcraft:light";
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return null;
	}

}
