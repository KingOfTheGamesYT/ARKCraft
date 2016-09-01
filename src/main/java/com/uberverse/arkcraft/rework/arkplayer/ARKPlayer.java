package com.uberverse.arkcraft.rework.arkplayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import com.google.common.collect.ImmutableList;
import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.config.WeightsConfig;
import com.uberverse.arkcraft.common.entity.IArkLeveling;
import com.uberverse.arkcraft.common.entity.ITranquilizable;
import com.uberverse.arkcraft.common.entity.data.CalcPlayerWeight;
import com.uberverse.arkcraft.common.entity.event.ArkExperienceGainEvent;
import com.uberverse.arkcraft.common.inventory.InventoryEngram;
import com.uberverse.arkcraft.common.network.PlayerPoop;
import com.uberverse.arkcraft.rework.arkplayer.network.ARKPlayerUpdate;
import com.uberverse.arkcraft.rework.arkplayer.network.ARKPlayerUpdateRequest;
import com.uberverse.arkcraft.rework.arkplayer.network.PlayerEngramCrafterProgressUpdate;
import com.uberverse.arkcraft.rework.arkplayer.network.PlayerEngramCrafterUpdate;
import com.uberverse.arkcraft.rework.container.ContainerPlayerCrafting;
import com.uberverse.arkcraft.rework.engram.CraftingOrder;
import com.uberverse.arkcraft.rework.engram.EngramManager;
import com.uberverse.arkcraft.rework.engram.EngramManager.Engram;
import com.uberverse.arkcraft.rework.engram.IEngramCrafter;
import com.uberverse.arkcraft.rework.entity.IWeighable;
import com.uberverse.arkcraft.rework.util.FixedSizeQueue;
import com.uberverse.arkcraft.rework.util.NBTable;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Lewis_McReu Based on earlier concepts by wildbill22 and ERBF
 */
public class ARKPlayer implements IExtendedEntityProperties, IArkLeveling, IWeighable, ITranquilizable
{
	/*
	 * methods to simplify registering and accessing
	 * certain variables for static use
	 */
	public static final String propKey = "arkplayer";

	public static ARKPlayer get(EntityPlayer p)
	{
		return (ARKPlayer) p.getExtendedProperties(propKey);
	}

	public static void register(EntityPlayer p)
	{
		p.registerExtendedProperties(propKey, new ARKPlayer(p));
	}

	public static ARKPlayer getDefault()
	{
		return new ARKPlayer();
	}

	// General

	private EntityPlayer player;

	public ARKPlayer()
	{
		this.statMap = new HashMap<>();
		initializeVariables();
		unlockedEngrams = new ArrayList<>();
	}

	public ARKPlayer(EntityPlayer player)
	{
		this();
		this.player = player;
		engramCrafter = new PlayerEngramCrafter(this.player);
	}

	public void update()
	{
		if (!player.worldObj.isRemote)
		{
			// Update crafting
			if (engramCrafter.isCrafting())
			{
				engramCrafter.update();
			}

			sendSynchronization(false);
		}
		else
		{
			// Apply weight effects
			if (WeightsConfig.isEnabled)
			{
				if (!player.capabilities.isCreativeMode || WeightsConfig.allowInCreative)
				{
					// Removes the updating when the player is in a inventory
					if (Minecraft.getMinecraft().currentScreen == null)
					{
						// Weight rules
						if (isOverEncumbered()) // value changed to match ARK; see isOverEncumbered()
						{
							player.motionX *= 0;
							player.motionZ *= 0;
						}
						else if (isEncumbered()) // value changed to match ARK; see isEncumbered()
						{
							player.motionX *= WeightsConfig.encumberedSpeed;
							player.motionY *= WeightsConfig.encumberedSpeed;
							player.motionZ *= WeightsConfig.encumberedSpeed;
						}
					}
				}
			}
		}
	}

	public InventoryEngram getEngramInventory()
	{
		return new InventoryEngram();
	}

	@SideOnly(Side.CLIENT)
	public void requestSynchronization(boolean all)
	{
		Minecraft.getMinecraft().addScheduledTask(new Runnable()
		{
			@Override
			public void run()
			{
				ARKCraft.modChannel.sendToServer(new ARKPlayerUpdateRequest(all));
			}
		});
	}

	public void sendSynchronization(boolean all)
	{
		ARKCraft.modChannel.sendTo(new ARKPlayerUpdate(this, all), (EntityPlayerMP) player);
	}

	// max stats and stat increases

	private static final int healthIncrease = 10, staminaIncrease = 10, oxygenIncrease = 20, foodIncrease = 10, waterIncrease = 10,
			damageIncrease = 5, speedIncrease = 2, weightIncrease = 10, maxTorpor = 200;
	private static final short maxLevel = 98;

	// current stats

	private Map<String, Variable<?>> statMap;

	public Map<String, Variable<?>> getStats()
	{
		return statMap;
	}

	private Variable<Boolean> hasToGo;
	private Variable<Integer> health, oxygen, food, water, damage, speed, stamina, torpor, engramPoints, maxHealth, maxOxygen, maxFood, maxWater,
			maxDamage, maxSpeed, maxStamina;
	private Variable<Short> level;
	private Variable<Long> xp;
	private Variable<Double> weight, maxWeight;

	public boolean hasToGo()
	{
		return hasToGo.get();
	}

	public void relief()
	{
		this.hasToGo.set(false);
	}

	public void feelTheUrge()
	{
		hasToGo.set(true);
	}

	public void go()
	{
		if (hasToGo())
		{
			if (player.worldObj.isRemote)
			{
				player.playSound(ARKCraft.MODID + ":" + "dodo_defficating", 1.0F,
						(player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.2F + 1.0F);
				ARKCraft.modChannel.sendToServer(new PlayerPoop(true));
			}
			relief();
		}
		else
		{
			player.addChatMessage(new ChatComponentTranslation("chat.canNotPoop"));
		}
	}

	public int getHealth()
	{
		return health.get();
	}

	public int getMaxHealth()
	{
		return maxHealth.get();
	}

	public int getOxygen()
	{
		return oxygen.get();
	}

	public int getMaxOxygen()
	{
		return maxOxygen.get();
	}

	public int getFood()
	{
		return food.get();
	}

	public int getMaxFood()
	{
		return maxFood.get();
	}

	public int getWater()
	{
		return water.get();
	}

	public int getMaxWater()
	{
		return maxWater.get();
	}

	public int getDamage()
	{
		return damage.get();
	}

	public int getMaxDamage()
	{
		return maxDamage.get();
	}

	public int getSpeed()
	{
		return speed.get();
	}

	public int getMaxSpeed()
	{
		return maxSpeed.get();
	}

	public int getStamina()
	{
		return stamina.get();
	}

	public int getMaxStamina()
	{
		return maxStamina.get();
	}

	public int getTorpor()
	{
		return torpor.get();
	}

	public static int getMaxTorpor()
	{
		return maxTorpor;
	}

	@Override
	public void applyTorpor(int torpor)
	{
		this.torpor.set(Math.min(getTorpor() + torpor, getMaxTorpor()));
	}

	public double getWeight()
	{
		return weight.get();
	}

	public double getMaxWeight()
	{
		return maxWeight.get();
	}

	public double getRelativeWeight()
	{
		return getWeight() / getMaxWeight();
	}

	public boolean isEncumbered()
	{
		return getRelativeWeight() >= 0.85;
	}

	public boolean isOverEncumbered()
	{
		return getRelativeWeight() >= 1;
	}

	public void updateWeight()
	{
		if (!player.worldObj.isRemote)
		{
			weight.set(CalcPlayerWeight.getAsDouble(player));
			sendSynchronization(false);
		}
	}

	public int getEngramPoints()
	{
		return engramPoints.get();
	}

	private void initializeVariables()
	{
		hasToGo = registerVariable("hasToGo", false);
		health = registerVariable("health", 0);
		oxygen = registerVariable("oxygen", 0);
		food = registerVariable("food", 0);
		water = registerVariable("water", 0);
		damage = registerVariable("damage", 0);
		speed = registerVariable("speed", 0);
		stamina = registerVariable("stamina", 0);
		torpor = registerVariable("torpor", 0);
		level = registerVariable("level", (short) 1);
		engramPoints = registerVariable("engramPoints", 0);
		maxHealth = registerVariable("maxHealth", 0);
		maxOxygen = registerVariable("maxOxygen", 0);
		maxFood = registerVariable("maxFood", 0);
		maxWater = registerVariable("maxWater", 0);
		maxDamage = registerVariable("maxDamage", 0);
		maxSpeed = registerVariable("maxSpeed", 0);
		maxStamina = registerVariable("maxStamina", 0);
		xp = registerVariable("xp", (long) 0);
		weight = registerVariable("weight", 0d);
		maxWeight = registerVariable("maxWeight", 0d);
	}

	private <T> Variable<T> registerVariable(String name, T in)
	{
		Variable<T> var = new Variable<T>(in);
		statMap.put(name, var);
		return var;
	}

	// IArkLeveling

	public void addXP(long xp)
	{
		ArkExperienceGainEvent event = new ArkExperienceGainEvent(this.player, xp);
		boolean canceled = ARKCraft.bus.post(event);
		if (!canceled)
		{
			this.xp.set(event.getXp() + this.xp.get());
			updateLevel();
			this.sendSynchronization(false);
		}
	}

	public void levelUpVariable(String variableKey)
	{
		if (!variableKey.startsWith("max")) variableKey = "max".concat(variableKey.substring(0, 1).toUpperCase().concat(variableKey.substring(1)));
		switch (variableKey)
		{
			case "maxHealth":
				maxHealth.set(maxHealth.get() + healthIncrease);
				break;
			case "maxOxygen":
				maxOxygen.set(maxOxygen.get() + oxygenIncrease);
				break;
			case "maxFood":
				maxFood.set(maxFood.get() + foodIncrease);
				break;
			case "maxWater":
				maxWater.set(maxWater.get() + waterIncrease);
				break;
			case "maxDamage":
				maxDamage.set(maxDamage.get() + damageIncrease);
				break;
			case "maxSpeed":
				maxSpeed.set(maxSpeed.get() + speedIncrease);
				break;
			case "maxStamina":
				maxStamina.set(maxStamina.get() + staminaIncrease);
				break;
			case "maxWeight":
				maxWeight.set(maxWeight.get() + weightIncrease);
				break;
		}
	}

	@Override
	public long getXP()
	{
		return xp.get();
	}

	public void updateLevel()
	{
		while (getLevel() < getMaxLevel() && getXP() > getRequiredXP())
		{
			increaseLevel();
			engramPoints.set(engramPoints.get() + getReceivedEngramPoints(getLevel()));
		}
	}

	public long getRequiredXP()
	{
		return Math.round(Math.pow((getLevel() + 1) * 5, 3) / 2);
	}

	// Calculate engrampoints / level
	private static int getReceivedEngramPoints(int level)
	{
		if (level < 60) return level / 10 * 4 + 8;
		if (level < 100) return (level / 10 - 6) * 10 + 40;
		return 0;
	}

	@Override
	public short getLevel()
	{
		return level.get();
	}

	private void increaseLevel()
	{
		level.set((short) (level.get() + 1));
	}

	@Override
	public short getMaxLevel()
	{
		return maxLevel;
	}

	// IExtendedEntityProperties

	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		NBTTagCompound properties = new NBTTagCompound();

		// ARK player properties
		properties.setBoolean("hasToGo", hasToGo());

		properties.setInteger("health", getHealth());
		properties.setInteger("oxygen", getOxygen());
		properties.setInteger("food", getFood());
		properties.setInteger("water", getWater());
		properties.setInteger("damage", getDamage());
		properties.setInteger("speed", getSpeed());
		properties.setInteger("stamina", getStamina());
		properties.setInteger("torpor", getTorpor());
		properties.setDouble("weight", getWeight());

		properties.setInteger("maxHealth", getMaxHealth());
		properties.setInteger("maxOxygen", getMaxOxygen());
		properties.setInteger("maxFood", getMaxFood());
		properties.setInteger("maxWater", getMaxWater());
		properties.setInteger("maxDamage", getMaxDamage());
		properties.setInteger("maxSpeed", getMaxSpeed());
		properties.setInteger("maxStamina", getMaxStamina());
		properties.setDouble("maxWeight", getMaxWeight());

		properties.setLong("xp", getXP());
		properties.setShort("level", getLevel());
		properties.setInteger("engramPoints", getEngramPoints());

		int[] c = new int[unlockedEngrams.size()];
		for (int i = 0; i < unlockedEngrams.size(); i++)
		{
			c[i] = unlockedEngrams.get(i);
		}
		properties.setIntArray("unlockedEngrams", c);

		NBTTagCompound nbt = new NBTTagCompound();
		getEngramCrafter().writeToNBT(nbt);

		properties.setTag("engramCrafter", nbt);

		compound.setTag(propKey, properties);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		if (compound == null || !compound.hasKey(propKey))
		{
			ARKCraft.logger.info("Couldn't find nbt!");
			return;
		}
		NBTTagCompound properties = compound.getCompoundTag(propKey);
		if (properties == null) { return; }

		// ARK player properties

		hasToGo.variable = properties.getBoolean("hasToGo");

		health.variable = properties.getInteger("health");
		oxygen.variable = properties.getInteger("oxygen");
		food.variable = properties.getInteger("food");
		water.variable = properties.getInteger("water");
		damage.variable = properties.getInteger("damage");
		speed.variable = properties.getInteger("speed");
		stamina.variable = properties.getInteger("stamina");
		torpor.variable = properties.getInteger("torpor");
		weight.variable = properties.getDouble("weight");

		maxHealth.variable = properties.getInteger("maxHealth");
		maxOxygen.variable = properties.getInteger("maxOxygen");
		maxFood.variable = properties.getInteger("maxFood");
		maxWater.variable = properties.getInteger("maxWater");
		maxDamage.variable = properties.getInteger("maxDamage");
		maxSpeed.variable = properties.getInteger("maxSpeed");
		maxStamina.variable = properties.getInteger("maxStamina");
		maxWeight.variable = properties.getDouble("maxWeight");

		xp.variable = properties.getLong("xp");
		level.variable = properties.getShort("level");
		engramPoints.variable = properties.getInteger("engramPoints");

		unlockedEngrams.clear();
		for (int i : properties.getIntArray("unlockedEngrams"))
		{
			unlockedEngrams.add((short) i);
		}

		NBTTagCompound nbt = new NBTTagCompound();
		getEngramCrafter().writeToNBT(nbt);

		properties.setTag("engramCrafter", nbt);

		compound.setTag(propKey, properties);
	}

	@Override
	public void init(Entity entity, World world)
	{
		// if (world.isRemote)
		// {
		// requestSynchronization(true);
		// }
	}

	/**
	 * Copies additional player data from the given ExtendedPlayer instance Avoids NBT disk I/O overhead when cloning a player after respawn
	 */
	public void copy(ARKPlayer props)
	{
		this.hasToGo.variable = props.hasToGo.variable;
		this.health.variable = props.health.variable;
		this.oxygen.variable = props.oxygen.variable;
		this.food.variable = props.food.variable;
		this.water.variable = props.water.variable;
		this.damage.variable = props.damage.variable;
		this.speed.variable = props.speed.variable;
		this.stamina.variable = props.stamina.variable;
		this.torpor.variable = props.torpor.variable;
		this.engramPoints.variable = props.engramPoints.variable;
		this.maxHealth.variable = props.maxHealth.variable;
		this.maxOxygen.variable = props.maxOxygen.variable;
		this.maxFood.variable = props.maxFood.variable;
		this.maxWater.variable = props.maxWater.variable;
		this.maxDamage.variable = props.maxDamage.variable;
		this.maxSpeed.variable = props.maxSpeed.variable;
		this.maxStamina.variable = props.maxStamina.variable;
		this.xp.variable = props.xp.variable;
		this.weight.variable = props.weight.variable;
		this.maxWeight.variable = props.maxWeight.variable;
		this.level.variable = props.level.variable;
	}

	public void copyConditionally(ARKPlayer props)
	{
		if (props.hasToGo.changed) this.hasToGo.variable = props.hasToGo.variable;
		if (props.health.changed) this.health.variable = props.health.variable;
		if (props.oxygen.changed) this.oxygen.variable = props.oxygen.variable;
		if (props.food.changed) this.food.variable = props.food.variable;
		if (props.water.changed) this.water.variable = props.water.variable;
		if (props.damage.changed) this.damage.variable = props.damage.variable;
		if (props.speed.changed) this.speed.variable = props.speed.variable;
		if (props.stamina.changed) this.stamina.variable = props.stamina.variable;
		if (props.torpor.changed) this.torpor.variable = props.torpor.variable;
		if (props.engramPoints.changed) this.engramPoints.variable = props.engramPoints.variable;
		if (props.maxHealth.changed) this.maxHealth.variable = props.maxHealth.variable;
		if (props.maxOxygen.changed) this.maxOxygen.variable = props.maxOxygen.variable;
		if (props.maxFood.changed) this.maxFood.variable = props.maxFood.variable;
		if (props.maxWater.changed) this.maxWater.variable = props.maxWater.variable;
		if (props.maxDamage.changed) this.maxDamage.variable = props.maxDamage.variable;
		if (props.maxSpeed.changed) this.maxSpeed.variable = props.maxSpeed.variable;
		if (props.maxStamina.changed) this.maxStamina.variable = props.maxStamina.variable;
		if (props.xp.changed) this.xp.variable = props.xp.variable;
		if (props.weight.changed) this.weight.variable = props.weight.variable;
		if (props.maxWeight.changed) this.maxWeight.variable = props.maxWeight.variable;
		if (props.level.changed) this.level.variable = props.level.variable;
	}

	/**
	 * Class for keeping separate variables and whether they've been changed recently
	 *
	 * @param <E>
	 */
	public static class Variable<E>
	{
		private Object variable;
		private boolean changed;

		public Variable(E variable)
		{
			this.variable = variable;
			this.changed = false;

		}

		public void set(Object variable)
		{
			if (!this.variable.equals(variable) && this.variable.getClass() == variable.getClass())
			{
				this.changed = true;
				this.variable = variable;
			}
		}

		public E get()
		{
			return (E) variable;
		}

		public boolean isChanged()
		{
			return changed;
		}

		public void setSynced()
		{
			this.changed = false;
		}

		public void writeConditionally(ByteBuf buf)
		{
			buf.writeBoolean(changed);
			if (changed) write(buf);
		}

		public void write(ByteBuf buf)
		{
			Class<?> c = variable.getClass();
			if (c == Integer.class)
			{
				buf.writeInt((int) variable);
			}
			else if (c == Boolean.class)
			{
				buf.writeBoolean((boolean) variable);
			}
			else if (c == Byte.class)
			{
				buf.writeByte((byte) variable);
			}
			else if (c == Short.class)
			{
				buf.writeShort((short) variable);
			}
			else if (c == Long.class)
			{
				buf.writeLong((long) variable);
			}
			else if (c == Character.class)
			{
				buf.writeChar((char) variable);
			}
			else if (c == Double.class)
			{
				buf.writeDouble((double) variable);
			}
			else if (c == Float.class)
			{
				buf.writeFloat((float) variable);
			}
			else
			{
				ARKCraft.logger.info("Can't write type " + c.getSimpleName() + " to ByteBuf");
				ARKCraft.logger.debug("Full class name: " + c.getCanonicalName());
			}
			setSynced();
		}

		public void read(ByteBuf buf)
		{
			Class<?> c = variable.getClass();
			if (c == Integer.class)
			{
				variable = buf.readInt();
			}
			else if (c == Boolean.class)
			{
				variable = buf.readBoolean();
			}
			else if (c == Byte.class)
			{
				variable = buf.readByte();
			}
			else if (c == Short.class)
			{
				variable = buf.readShort();
			}
			else if (c == Long.class)
			{
				variable = buf.readLong();
			}
			else if (c == Character.class)
			{
				variable = buf.readChar();
			}
			else if (c == Double.class)
			{
				variable = buf.readDouble();
			}
			else if (c == Float.class)
			{
				variable = buf.readFloat();
			}
			else
			{
				ARKCraft.logger.info("Can't read type " + c.getSimpleName() + " from ByteBuf");
				ARKCraft.logger.debug("Full class name: " + c.getCanonicalName());
			}
		}

		public void readConditionally(ByteBuf buf)
		{
			changed = buf.readBoolean();
			if (changed)
			{
				read(buf);
			}
		}
	}

	// Engram Crafter

	private PlayerEngramCrafter engramCrafter;

	public PlayerEngramCrafter getEngramCrafter()
	{
		return engramCrafter;
	}

	public class PlayerEngramCrafter implements IEngramCrafter, NBTable, IInventory
	{
		private EntityPlayer player;
		private int progress;
		private int craftingDuration;
		private Queue<CraftingOrder> craftingQueue;

		public PlayerEngramCrafter(EntityPlayer player)
		{
			this.progress = 0;
			this.craftingDuration = 0;
			this.player = player;
			this.craftingQueue = new FixedSizeQueue<>(5);
		}

		public EntityPlayer getPlayer()
		{
			return player;
		}

		@Override
		public int getProgress()
		{
			return progress;
		}

		@Override
		public void setProgress(int progress)
		{
			this.progress = progress;
		}

		@Override
		public IInventory getIInventory()
		{
			return this;
		}

		@Override
		public void syncProgress()
		{
			if (player.openContainer instanceof ContainerPlayerCrafting)
				ARKCraft.modChannel.sendTo(new PlayerEngramCrafterProgressUpdate(progress), (EntityPlayerMP) player);
		}

		@Override
		public void sync()
		{
			ARKCraft.modChannel.sendTo(new PlayerEngramCrafterUpdate(this), (EntityPlayerMP) player);
		}

		@Override
		public ItemStack[] getInventory()
		{
			return player.inventory.mainInventory;
		}

		@Override
		public BlockPos getPosition()
		{
			return player.playerLocation;
		}

		@Override
		public World getWorld()
		{
			return player.worldObj;
		}

		@Override
		public Queue<CraftingOrder> getCraftingQueue()
		{
			return craftingQueue;
		}

		@Override
		public IInventory getConsumedInventory()
		{
			return player.inventory;
		}

		@Override
		public int getField(int id)
		{
			return IEngramCrafter.super.getField(id);
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
			return 0;
		}

		@Override
		public ItemStack getStackInSlot(int index)
		{
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
		public void clear()
		{

		}

		@Override
		public int getFieldCount()
		{
			return IEngramCrafter.super.getFieldCount();
		}

		@Override
		public void setField(int id, int value)
		{
			IEngramCrafter.super.setField(id, value);
		}

		@Override
		public int getCraftingDuration()
		{
			return craftingDuration;
		}

		@Override
		public void setCraftingDuration(int craftingDuration)
		{
			this.craftingDuration = craftingDuration;
		}
	}

	// Engrams

	// Unlocked Engrams
	private ArrayList<Short> unlockedEngrams;

	public ImmutableList<Short> getUnlockedEngrams()
	{
		return ImmutableList.copyOf(unlockedEngrams);
	}

	public void updateUnlockedEngrams(Collection<Short> engrams, int points)
	{
		unlockedEngrams.clear();
		for (short s : engrams)
		{
			unlockedEngrams.add(s);
		}

		engramPoints.variable = points;
	}

	public void learnEngram(short id)
	{
		if (!unlockedEngrams.contains(id))
		{
			Engram e = EngramManager.instance().getEngram(id);
			if (e != null)
			{
				unlockedEngrams.add(id);
				engramPoints.set(getEngramPoints() - e.getPoints());
				engramPoints.setSynced();
			}
		}
	}

	public boolean canLearnEngram(short id)
	{
		return EngramManager.instance().canPlayerLearn(player, id);
	}

	public boolean hasLearnedEngram(short id)
	{
		return unlockedEngrams.contains(id);
	}
}
