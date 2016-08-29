package com.uberverse.arkcraft.common.entity.data;

import java.util.ArrayList;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.config.ModuleItemBalance;
import com.uberverse.arkcraft.common.entity.IArkLeveling;
import com.uberverse.arkcraft.common.entity.event.ArkExperienceGainEvent;
import com.uberverse.arkcraft.common.handlers.recipes.PlayerCraftingManager;
import com.uberverse.arkcraft.common.inventory.InventoryBlueprints;
import com.uberverse.arkcraft.common.inventory.InventoryPlayerCrafting;
import com.uberverse.arkcraft.common.inventory.InventoryPlayerEngram;
import com.uberverse.arkcraft.common.network.PlayerPoop;
import com.uberverse.arkcraft.common.network.SyncPlayerData;
import com.uberverse.arkcraft.rework.EngramManager;
import com.uberverse.arkcraft.rework.EngramManager.Engram;
import com.uberverse.lib.LogHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

/**
 * @author wildbill22, Lewis_McReu, ERBF
 */
public class ARKPlayer implements IExtendedEntityProperties, IArkLeveling
{
	// TODO
	private static final int healthIncrease = 10, staminaIncrease = 10, oxygenIncrease = 20,
			foodIncrease = 10, waterIncrease = 10, damageIncrease = 5, speedIncrease = 2,
			maxTorpor = 200, maxLevel = 98;

	public static final String EXT_PROP_NAME = "ARKPlayer";
	private final EntityPlayer player;

	// The extended player properties (anything below should be initialized in
	// constructor and in NBT):
	private boolean canPoop; // True if player can poop (timer sets this)
	// actual stats
	private int health, oxygen, food, water, damage, speed, stamina, torpor, level, engramPoints;
	private long xp;
	// max stats
	private int maxHealth, maxOxygen, maxFood, maxWater, maxDamage, maxSpeed, maxStamina;
	// actual weights
	private double carryWeight;
	// max weights
	private double maxCarryWeight;
	// Unlocked Engrams
	private ArrayList<Short> engrams;

	public ARKPlayer(EntityPlayer player, World world)
	{
		// Initialize some stuff
		this.player = player;
		this.inventoryPlayerCrafting = new InventoryPlayerCrafting("crafting", false, player);
		inventoryBlueprints = new InventoryBlueprints("Blueprints", false, BLUEPRINT_SLOTS_COUNT,
				PlayerCraftingManager.getInstance(), inventoryPlayerCrafting,
				(short) ModuleItemBalance.PLAYER_CRAFTING.CRAFT_TIME_FOR_ITEM);
		this.setCanPoop(false);
		this.water = 20;
		this.torpor = 0;
		this.stamina = 20;
		this.level = 1;
		// For player carry weight
		this.carryWeight = 0.0;
		this.maxCarryWeight = 100.0;
		this.engramPoints = 0;
		this.engrams = new ArrayList<>();
	}

	/**
	 * Registers properties to player
	 *
	 * @param player
	 */
	public static final void register(EntityPlayer player, World world)
	{
		player.registerExtendedProperties(ARKPlayer.EXT_PROP_NAME, new ARKPlayer(player, world));
	}

	/**
	 * @param player
	 * @return properties of player
	 */
	public static final ARKPlayer get(EntityPlayer player)
	{
		return (ARKPlayer) player.getExtendedProperties(EXT_PROP_NAME);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		NBTTagCompound properties = new NBTTagCompound();
		// ARK player properties
		properties.setBoolean("canPoop", canPoop);
		properties.setInteger("health", health);
		properties.setInteger("oxygen", oxygen);
		properties.setInteger("food", food);
		properties.setInteger("water", water);
		properties.setInteger("damage", damage);
		properties.setInteger("speed", speed);
		properties.setInteger("stamina", stamina);
		properties.setInteger("torpor", torpor);
		properties.setLong("xp", xp);
		properties.setInteger("level", level);
		properties.setInteger("engramPoints", engramPoints);
		properties.setDouble("carryWeight", carryWeight);
		properties.setDouble("weight", maxCarryWeight);

		properties.setInteger("maxHealth", maxHealth);
		properties.setInteger("maxOxygen", maxOxygen);
		properties.setInteger("maxFood", maxFood);
		properties.setInteger("maxWater", maxWater);
		properties.setInteger("maxDamage", maxDamage);
		properties.setInteger("maxSpeed", maxSpeed);
		properties.setInteger("maxStamina", maxStamina);
		properties.setDouble("maxCarryWeight", maxCarryWeight);

		Short[] s = engrams.toArray(new Short[1]);

		int[] c = new int[engrams.size()];
		for (int i = 0; i < engrams.size(); i++)
			c[i] = engrams.get(i);
		properties.setIntArray("unlockedEngrams", c);

		compound.setTag(EXT_PROP_NAME, properties);
		// inventoryPlayerCrafting.saveInventoryToNBT(compound);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		NBTTagCompound properties = compound.getCompoundTag(EXT_PROP_NAME);
		if (properties == null) { return; }
		// ARK player properties
		canPoop = properties.getBoolean("canPoop");
		health = properties.getInteger("health");
		oxygen = properties.getInteger("oxygen");
		maxCarryWeight = properties.getInteger("weight");

		food = properties.getInteger("food");
		water = properties.getInteger("water");
		damage = properties.getInteger("damage");
		speed = properties.getInteger("speed");
		stamina = properties.getInteger("stamina");
		torpor = properties.getInteger("torpor");
		xp = properties.getLong("xp");
		level = properties.getInteger("level");
		engramPoints = properties.getInteger("engramPoints");
		carryWeight = properties.getDouble("carryWeight");

		maxHealth = properties.getInteger("maxHealth");
		maxOxygen = properties.getInteger("maxOxygen");
		maxFood = properties.getInteger("maxFood");
		maxWater = properties.getInteger("maxWater");
		maxDamage = properties.getInteger("maxDamage");
		maxSpeed = properties.getInteger("maxSpeed");
		maxStamina = properties.getInteger("maxStamina");
		maxCarryWeight = properties.getDouble("maxCarryWeight");

		for (int i : properties.getIntArray("unlockedEngrams"))
			engrams.add((short) i);

		// inventoryPlayerCrafting.loadInventoryFromNBT(compound);
	}

	public void setWater(int water)
	{
		this.water = water;
		syncClient(player, false);
	}

	public void setTorpor(int torpor)
	{
		this.torpor = torpor;
		syncClient(player, false);
	}

	public void setStamina(int stamina)
	{
		this.stamina = stamina;
		syncClient(player, false);
	}

	/**
	 * Added by ERBF. Used to set the players carry weight
	 * 
	 * @param carryWeight
	 */
	public void setCarryWeight(double carryWeight)
	{
		this.carryWeight = carryWeight;
		syncClient(player, false);
	}

	/**
	 * Added by ERBF. Used to set the players weight
	 * 
	 * @param weight
	 */
	public void setWeight(double weight)
	{
		this.maxCarryWeight = weight;
		syncClient(player, false);
	}

	public void setEngramPoints(int engramPoints)
	{
		this.engramPoints = engramPoints;
		syncClient(player, false);
	}

	public ArrayList<Short> learnedEngrams()
	{
		return engrams;
	}

	public int getWater()
	{
		return water;
	}

	public int getTorpor()
	{
		return torpor;
	}

	public int getStamina()
	{
		return stamina;
	}

	public double getCarryWeight()
	{
		return carryWeight;
	}

	public double getCarryWeightRatio()
	{
		return (double) carryWeight / maxCarryWeight;
	}

	public int getEngramPoints()
	{
		return engramPoints;
	}

	public InventoryPlayerEngram getEngramInventory()
	{
		return new InventoryPlayerEngram();
	}

	/**
	 * Copies additional player data from the given ExtendedPlayer instance
	 * Avoids NBT disk I/O overhead when cloning a player after respawn
	 */
	public void copy(ARKPlayer props)
	{
		this.canPoop = props.canPoop;
		this.torpor = props.torpor;
		this.water = props.water;
		this.stamina = props.stamina;
		this.carryWeight = props.carryWeight;
	}

	@Override
	public void init(Entity entity, World world)
	{
	}

	public void syncClient(EntityPlayer player, boolean all)
	{
		ARKPlayer p = this;
		if (player instanceof EntityPlayerMP)
		{
			((EntityPlayerMP) player).getServerForPlayer().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					ARKCraft.modChannel.sendTo(new SyncPlayerData(all, p), (EntityPlayerMP) player);
				}
			});
		}
	}

	@SuppressWarnings("unused")
	private EntityPlayer getPlayer()
	{
		return player;
	}

	// --------- Pooping -----------------
	public boolean canPoop()
	{
		return canPoop;
	}

	public void setCanPoop(boolean canPoop)
	{
		this.canPoop = canPoop;
	}

	

	// ----------------- End of Properties stuff, rest is for crafting
	// -----------------

	// Inventory for Crafting
	private InventoryPlayerCrafting inventoryPlayerCrafting;
	private InventoryBlueprints inventoryBlueprints;

	// Constants for the inventory
	public static final int BLUEPRINT_SLOTS_COUNT = 20;
	public static final int FIRST_BLUEPRINT_SLOT = 0;
	public static final int INVENTORY_SLOTS_COUNT = 10;
	public static final int FIRST_INVENTORY_SLOT = 0;
	public static final int LAST_INVENTORY_SLOT = INVENTORY_SLOTS_COUNT - 1;

	public InventoryBlueprints getInventoryBlueprints()
	{
		return inventoryBlueprints;
	}

	public void setInventoryBlueprints(InventoryBlueprints inventoryBlueprints)
	{
		this.inventoryBlueprints = inventoryBlueprints;
	}

	public InventoryPlayerCrafting getInventoryPlayer()
	{
		return inventoryPlayerCrafting;
	}

	public void setInventoryPlayer(InventoryPlayerCrafting inventoryPlayer)
	{
		this.inventoryPlayerCrafting = inventoryPlayer;
	}

	// Leveling stuff
	public void addXP(long xp)
	{
		ArkExperienceGainEvent event = new ArkExperienceGainEvent(this.player, xp);
		boolean canceled = ARKCraft.bus.post(event);
		if (!canceled)
		{
			this.xp += event.getXp();
			updateLevel();
			ARKCraft.logger.info(engramPoints);
			syncClient(player, false);
		}
	}

	public void updateLevel()
	{
		while (level < maxLevel && xp > getRequiredXP())
		{
			level++;
			engramPoints += getReceivedEngramPoints(level);
		}
	}

	public long getRequiredXP()
	{
		return Math.round(Math.pow(level + 1, 3) / 2);
	}

	private static int getReceivedEngramPoints(int level)
	{
		if (level < 60) return level / 10 * 4 + 8;
		if (level < 100) return (level / 10 - 6) * 10 + 40;
		return 0;
	}

	@Override
	public long getXP()
	{
		return xp;
	}

	@Override
	public short getLevel()
	{
		return (short) level;
	}

	@Override
	public short getMaxLevel()
	{
		return maxLevel;
	}
}
