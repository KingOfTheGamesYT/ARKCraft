/**
 *
 */
package com.arkcraft.common.burner;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.item.IDecayable;
import com.arkcraft.util.CollectionUtil;
import com.arkcraft.util.IInventoryAdder;
import com.arkcraft.util.NBTable;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author Lewis_McReu
 */
public interface IBurner extends IInventoryAdder, NBTable {
	public default void updateBurner() {
		int count = 0;
		updateBurning();
		World world = getWorldIA();
		if (!world.isRemote) {
			Collection<BurnerManager.BurnerRecipe> possibleRecipes = BurnerManager.instance().getRecipes(getBurnerType());
			possibleRecipes = CollectionUtil.filter(possibleRecipes, (BurnerManager.BurnerRecipe r) -> canCook(r));

			if (this.isBurning() && new Random().nextInt(40) == 0) {
				//	playOnSound();
				System.out.println("on");
			}
			if (this.isBurning() && possibleRecipes.size() > 0) {
				Map<BurnerManager.BurnerRecipe, Integer> activeRecipes = getActiveRecipes();

				Iterator<Entry<BurnerManager.BurnerRecipe, Integer>> it = activeRecipes.entrySet().iterator();
				while (it.hasNext()) {
					Entry<BurnerManager.BurnerRecipe, Integer> e = it.next();
					if (!possibleRecipes.contains(e.getKey())) {
						it.remove();
					}
				}
				for (BurnerManager.BurnerRecipe r : possibleRecipes) {
					if (!activeRecipes.containsKey(r)) activeRecipes.put(r, 0);
				}

				updateCookTimes();

				sync();
			} else clearActiveRecipes();
		}
		world.checkLightFor(EnumSkyBlock.BLOCK, getPosition());
	}

	public default boolean canCook(BurnerManager.BurnerRecipe r) {
		Item[] items = r.getItems().keySet().toArray(new Item[0]);
		Integer[] required = r.getItems().values().toArray(new Integer[0]);
		int[] found = new int[items.length];
		boolean[] done = new boolean[items.length];
		for (ItemStack s : getInventory()) {
			if (s != null) {
				for (int i = 0; i < items.length; i++) {
					if (items[i] == s.getItem() && !done[i]) {
						found[i] += required[i] > s.getCount() ? s.getCount() : required[i];
						done[i] = required[i] == found[i];
					}
				}
				boolean x = false;
				for (boolean b : done) {
					if (!b) {
						x = true;
						break;
					}
				}
				if (x) continue;
				return true;
			}
		}
		return false;
	}

	public default void updateCookTimes() {
		Iterator<Entry<BurnerManager.BurnerRecipe, Integer>> it = getActiveRecipes().entrySet().iterator();
		while (it.hasNext()) {
			Entry<BurnerManager.BurnerRecipe, Integer> e = it.next();
			e.setValue(e.getValue() + 1);
			if (e.getValue() == e.getKey().getCraftingTime()) {
				for (Entry<Item, Integer> i : e.getKey().getItems().entrySet()) {
					consume(new ItemStack(i.getKey(), i.getValue()));
				}
				ItemStack s = new ItemStack(e.getKey().getItem(), e.getKey().getAmount());
				if (s.getItem() instanceof IDecayable) IDecayable.setDecayStart(s, ARKCraft.proxy().getWorldTime());
				addOrDrop(s);
				it.remove();
			}
		}
	}

	public default void consume(ItemStack... itemStacks) {
		for (ItemStack stack : itemStacks)
			consume(stack);
	}

	public default void consume(ItemStack stack) {
		if (stack != null) {
			ItemStack[] inv = getInventory();
			for (int i = 0; i < inv.length; i++) {
				ItemStack in = inv[i];
				if (in != null && in.getItem() == stack.getItem()) {

					if (in.getCount() > stack.getCount()) {
						in.shrink(stack.getCount());
					} else {
						inv[i] = null;
						stack.shrink(in.getCount());
					}
				}
			}
		}
	}

	public default void clearActiveRecipes() {
		getActiveRecipes().clear();
	}

	public default boolean updateIsBurning(boolean burning) {
		setBurning(burning);
		updateBurning();
		if (burning != isBurning()) sync();
		return isBurning();
	}

	public default void updateBurning() {
		if (isBurning()) {
			int burningTicks = getBurningTicks();
			if (burningTicks > 0) {
				burningTicks--;
				setBurningTicks(getBurningTicks() - 1);
			}
			if (burningTicks <= 0) {
				if (!burn()) {
					setBurning(false);
				}
			}
		} else {
			setBurningTicks(0);
			setCurrentFuel(null);
		}
	}

	public default boolean burn() {
		BurnerManager.BurnerFuel fuel = getCurrentFuel();
		if (fuel != null) {
			if (!getWorldIA().isRemote && fuel.hasOutput()) {
				ItemStack s = new ItemStack(fuel.getOutput(), fuel.getOutputAmount());
				addOrDrop(s);
			}
			setCurrentFuel(null);
		}
		IInventory inventory = getIInventory();
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			if (stack != null && BurnerManager.instance().isValidFuel(stack.getItem(), getBurnerType())) {
				if (!getWorldIA().isRemote) {
					stack.shrink(1);
					if (stack.getCount() == 0) inventory.setInventorySlotContents(i, null);
				}
				setCurrentFuel(BurnerManager.instance().getFuel(stack.getItem()));
				setBurningTicks(getBurningTicks() + BurnerManager.instance().getFuel(stack.getItem()).getBurnTime());
				return true;
			}
		}
		return false;
	}

	//TODO Play correct sounds
	public default void playLightSound() {
		EntityPlayer player = Minecraft.getMinecraft().player;
		getWorldIA().playSound(player, getPosition(), SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 0.015F, 1F);
		//getWorldIA().playSoundEffect(getPosition().getX(), getPosition().getY(), getPosition().getZ(), getLightSoundName(),
		//		0.015F, 1F);
	}

	public default void playOnSound() {
		EntityPlayer player = Minecraft.getMinecraft().player;
		getWorldIA().playSound(player, getPosition(), SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 0.015F, 1F);
		//getWorldIA().playSoundEffect(getPosition().getX(), getPosition().getY(), getPosition().getZ(), getOnSoundName(),
		//		0.015F, 1F);
	}

	public default void playOffSound() {
		EntityPlayer player = Minecraft.getMinecraft().player;
		getWorldIA().playSound(player, getPosition(), SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 0.015F, 1F);
		//	getWorldIA().playSoundEffect(getPosition().getX(), getPosition().getY(), getPosition().getZ(), getOffSoundName(),
		//			0.015F, 1F);
	}

	public String getOnSoundName();

	public String getOffSoundName();

	public String getLightSoundName();

	public int getBurningTicks();

	public void setBurningTicks(int burningTicks);

	public BurnerManager.BurnerType getBurnerType();

	public boolean isBurning();

	public void setBurning(boolean burning);

	public BurnerManager.BurnerFuel getCurrentFuel();

	public void setCurrentFuel(BurnerManager.BurnerFuel fuel);

	public Map<BurnerManager.BurnerRecipe, Integer> getActiveRecipes();

	public void sync();

	@Override
	public default void readFromNBT(NBTTagCompound compound) {
		setBurning(compound.getBoolean("burning"));
		setBurningTicks(compound.getInteger("burningTicks"));

		NBTTagList inventory = compound.getTagList("inventory", NBT.TAG_COMPOUND);

		Collection<NBTTagCompound> out = new ArrayList<>();
		for (int i = 0; i < inventory.tagCount(); i++)
			out.add(inventory.getCompoundTagAt(i));

		List<ItemStack> in = CollectionUtil.convert(out, (NBTTagCompound n) -> n.getBoolean("null") ? null : new ItemStack(n));

		for (int i = 0; i < getInventory().length; i++)
			getInventory()[i] = in.get(i);
	}

	@Override
	public default NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("burningTicks", getBurningTicks());
		compound.setBoolean("burning", isBurning());

		NBTTagList inventory = new NBTTagList();

		for (NBTTagCompound nbt : CollectionUtil.convert(Arrays.asList(getInventory()), (ItemStack s) -> {
			NBTTagCompound n = new NBTTagCompound();
			n.setBoolean("null", true);
			if (s != null) {
				s.writeToNBT(n);
				n.setBoolean("null", false);
			}
			return n;
		}))
			inventory.appendTag(nbt);

		compound.setTag("inventory", inventory);
		return compound;
	}

	public ItemStack[] getInventory();
}
