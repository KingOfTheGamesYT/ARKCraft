package com.arkcraft.common.item;

import com.arkcraft.ARKCraft;
import com.arkcraft.util.I18n;
import com.arkcraft.util.Utils;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author Lewis_McReu
 * @author tom5454
 */
public interface IDecayable {
	public static long getDecayStart(ItemStack stack) {
		return stack.hasTagCompound() ? stack.getTagCompound().getLong("decayStart") : -1;
	}

	public static double getDecayModifier(ItemStack stack) {
		return stack.hasTagCompound() ? stack.getTagCompound().getLong("decayModifier") : 1;
	}

	public static void setDecayModifier(ItemStack stack, double decayModifier) {
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setDouble("decayModifier", decayModifier);
	}

	public static ItemStack setDecayStart(ItemStack stack, long decayStart) {
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setLong("decayStart", decayStart);
		return stack;
	}

	public default void decayTick(IInventory inventory, int slotId, double decayModifier, ItemStack stack, World world) {
		if (world.isRemote || world.getTotalWorldTime() % 20 != 0) return;
		decayTick(inventory, slotId, decayModifier, stack);
	}

	default void decayTick(IInventory inventory, int slotId, double decayModifier, ItemStack stack) {
		if (getDecayStart(stack) < 0) setDecayStart(stack, ARKCraft.proxy().getWorldTime());
		setDecayModifier(stack, decayModifier);
		if (shouldRemove(stack, decayModifier)) {
			stack.shrink(1);
			setDecayStart(stack, ARKCraft.proxy().getWorldTime());
			if (stack.getCount() <= 0) inventory.setInventorySlotContents(slotId, null);
		}
	}

	public default boolean shouldRemove(ItemStack stack, double decayModifier) {
		long decayStart = getDecayStart(stack);
		return decayStart >= 0 && getRemovalTime(stack, decayModifier) <= ARKCraft.proxy().getWorldTime();
	}

	public default long getRemovalTime(ItemStack stack, double decayModifier) {
		return (long) (getDecayStart(stack) + getDecayTime(stack) * decayModifier);
	}

	@SideOnly(Side.CLIENT)
	public default void addInformation(ItemStack itemStack, List<String> tooltip) {
		String toAdd = I18n.translate("arkcraft.decayable.tooltip");
		String ret = Utils.formatTime(getDecayTimeLeft(itemStack, getDecayModifier(itemStack)));
		if (!ret.isEmpty()) tooltip.add(toAdd + ret);
	}

	public default long getDecayTimeLeft(ItemStack stack, double decayModifier) {
		return getDecayStart(stack) > -1 ? getRemovalTime(stack, decayModifier) - ARKCraft.proxy().getWorldTime() : 0;
	}

	public long getDecayTime(ItemStack stack);
}
