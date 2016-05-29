package com.uberverse.arkcraft.init;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.BlockDispenser;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.ModuleItemBalance;
import com.uberverse.arkcraft.common.entity.EntityAdvancedBullet;
import com.uberverse.arkcraft.common.entity.EntityGrenade;
import com.uberverse.arkcraft.common.entity.EntityMetalArrow;
import com.uberverse.arkcraft.common.entity.EntityRocketPropelledGrenade;
import com.uberverse.arkcraft.common.entity.EntitySimpleBullet;
import com.uberverse.arkcraft.common.entity.EntitySimpleRifleAmmo;
import com.uberverse.arkcraft.common.entity.EntitySimpleShotgunAmmo;
import com.uberverse.arkcraft.common.entity.EntitySpear;
import com.uberverse.arkcraft.common.entity.EntityStoneArrow;
import com.uberverse.arkcraft.common.entity.EntityTranqArrow;
import com.uberverse.arkcraft.common.entity.EntityTranquilizer;
import com.uberverse.arkcraft.common.entity.dispense.DispenseRocketPropelledGrenade;
import com.uberverse.arkcraft.common.entity.dispense.DispenseSimpleBullet;
import com.uberverse.arkcraft.common.entity.dispense.DispenseSimpleRifleAmmo;
import com.uberverse.arkcraft.common.entity.dispense.DispenseSimpleShotgunAmmo;
import com.uberverse.arkcraft.common.entity.dispense.DispenseTranquilizer;
import com.uberverse.arkcraft.common.handlers.EntityHandler;
import com.uberverse.arkcraft.common.item.ARKCraftItem;
import com.uberverse.arkcraft.common.item.ammo.ItemProjectile;
import com.uberverse.arkcraft.common.item.attachments.AttachmentType;
import com.uberverse.arkcraft.common.item.attachments.ItemAttachment;
import com.uberverse.arkcraft.common.item.explosives.ItemGrenade;
import com.uberverse.arkcraft.common.item.firearms.ItemARKBow;
import com.uberverse.arkcraft.common.item.firearms.ItemCompoundBow;
import com.uberverse.arkcraft.common.item.firearms.ItemCrossbow;
import com.uberverse.arkcraft.common.item.firearms.ItemFabricatedPistol;
import com.uberverse.arkcraft.common.item.firearms.ItemLongneckRifle;
import com.uberverse.arkcraft.common.item.firearms.ItemRangedWeapon;
import com.uberverse.arkcraft.common.item.firearms.ItemRocketLauncher;
import com.uberverse.arkcraft.common.item.firearms.ItemShotgun;
import com.uberverse.arkcraft.common.item.firearms.ItemSimplePistol;
import com.uberverse.arkcraft.common.item.firearms.ItemSlingshot;
import com.uberverse.arkcraft.common.item.melee.ItemSpear;
import com.uberverse.arkcraft.common.item.tools.ItemMetalHatchet;
import com.uberverse.arkcraft.common.item.tools.ItemMetalPick;
import com.uberverse.arkcraft.common.item.tools.ItemStoneHatchet;
import com.uberverse.arkcraft.common.item.tools.ItemStonePick;

public class ARKCraftItems {
	
	public static ARKCraftItem azul, stone, spy_glass;
	public static ItemGrenade grenade;
	public static ItemAttachment scope, flash_light, silencer, laser, holo_scope;
	public static ItemSlingshot slingshot;
	public static ItemSpear spear;
	public static ItemARKBow bow;
	public static ItemCompoundBow compound_bow;
	public static ItemProjectile tranquilizer, stone_arrow, tranq_arrow, metal_arrow,
			ballista_bolt, simple_bullet, simple_rifle_ammo,
			simple_shotgun_ammo, rocket_propelled_grenade, advanced_bullet;
	public static ItemRangedWeapon rocket_launcher, tranq_gun;
	public static ItemRangedWeapon simple_pistol;
	public static ItemRangedWeapon fabricated_pistol;
	public static ItemRangedWeapon longneck_rifle;
	public static ItemRangedWeapon shotgun;
	public static ItemRangedWeapon crossbow;
	public static ItemStonePick stone_pick;
	public static ItemStoneHatchet stone_hatchet;
	public static ItemMetalPick metal_pick;
	public static ItemMetalHatchet metal_hatchet;
	
	public static ToolMaterial METAL = EnumHelper.addToolMaterial("METAL_MAT",
			3, 1500, 6.0F, 0.8F, 8);
	public static ToolMaterial STONE = EnumHelper.addToolMaterial("STONE_MAT",
			2, 500, 3.5F, 0.4F, 13);
	
	public static Map<String, Item> allItems = new HashMap<String, Item>();

	public static Map<String, Item> getAllItems()
	{
		return allItems;
	}
	
	public static void init()
	{
		// weapon attachments
		scope = addItemAttachment("scope", AttachmentType.SCOPE);
		flash_light = addItemAttachment("flash_light", AttachmentType.FLASH);
		holo_scope = addItemAttachment("holo_scope", AttachmentType.HOLO_SCOPE);
		laser = addItemAttachment("laser", AttachmentType.LASER);
		silencer = addItemAttachment("silencer", AttachmentType.SILENCER);

		slingshot = addSlingshot("slingshot");
		grenade = addGrenade("grenade");
		stone = addItem("stone");
		spy_glass = addItem("spy_glass");
		// stoneSpear = addWeaponThrowable("stoneSpear", ToolMaterial.STONE);

		ballista_bolt = addItemProjectile("ballista_bolt");
		
		//Tools
		metal_pick = addMetalPick("metal_pick", METAL);
		metal_hatchet = addMetalHatchet("metal_hatchet", METAL);
		stone_hatchet = addStoneHatchet("stone_hatchet", STONE);
		stone_pick = addStonePick("stone_pick", STONE);

		// Bows
		compound_bow = new ItemCompoundBow();
		registerItem("compound_bow", compound_bow);

		bow = new ItemARKBow();
		registerItem("bow", bow);

		// Bullets
		metal_arrow = addItemProjectile("metal_arrow");
		tranq_arrow = addItemProjectile("tranq_arrow");
		stone_arrow = addItemProjectile("stone_arrow");

		spear = addSpearItem("spear", ToolMaterial.WOOD);
	//	wooden_club = addWoodenClub("wooden_club", ToolMaterial.WOOD);

		EntityHandler.registerModEntity(EntityGrenade.class, "grenade",
				ARKCraft.instance, 64, 10, true);
		EntityHandler.registerModEntity(EntitySpear.class, "spear",
				ARKCraft.instance, 64, 10, true);

		registerDispenseBehavior();
		registerWeaponEntities();
		addRangedWeapons();
	}

	private static ItemAttachment addItemAttachment(String name, AttachmentType type)
	{
		ItemAttachment i = new ItemAttachment(name, type);
		registerItem(name, i);
		return i;
	}

	private static void registerWeaponEntities()
	{
		if (ModuleItemBalance.WEAPONS.SIMPLE_PISTOL)
		{
			EntityHandler.registerModEntity(EntitySimpleBullet.class,
					"simple_bullet", ARKCraft.instance, 16, 20, true);
		}

		if (ModuleItemBalance.WEAPONS.SHOTGUN)
		{
			EntityHandler.registerModEntity(EntitySimpleShotgunAmmo.class,
					"simple_shotgun_ammo", ARKCraft.instance, 64, 10, true);
		}

		if (ModuleItemBalance.WEAPONS.LONGNECK_RIFLE)
		{
			EntityHandler.registerModEntity(EntitySimpleRifleAmmo.class,
					"simple_rifle_ammo", ARKCraft.instance, 64, 10, true);
			EntityHandler.registerModEntity(EntityTranquilizer.class,
					"tranquilizer_dart", ARKCraft.instance, 64, 10, true);
		}

		if (ModuleItemBalance.WEAPONS.FABRICATED_PISTOL)
		{
			EntityHandler.registerModEntity(EntityAdvancedBullet.class,
					"advanced_bullet", ARKCraft.instance, 64, 10, true);
		}

		if (ModuleItemBalance.WEAPONS.ROCKET_LAUNCHER)
		{
			EntityHandler
					.registerModEntity(EntityRocketPropelledGrenade.class,
							"rocket_propelled_grenade", ARKCraft.instance, 64,
							10, true);
		}

		EntityHandler.registerModEntity(EntityTranqArrow.class, "tranq_arrow",
				ARKCraft.instance, 64, 10, true);
		EntityHandler.registerModEntity(EntityStoneArrow.class, "stone_arrow",
				ARKCraft.instance, 64, 10, true);
		EntityHandler.registerModEntity(EntityMetalArrow.class, "metal_arrow",
				ARKCraft.instance, 64, 10, true);
	}

	public static void addRangedWeapons()
	{
		if (ModuleItemBalance.WEAPONS.LONGNECK_RIFLE)
		{
			simple_rifle_ammo = addItemProjectile("simple_rifle_ammo");
			tranquilizer = addItemProjectile("tranquilizer");
			longneck_rifle = addShooter(new ItemLongneckRifle());
			longneck_rifle.registerProjectile(simple_rifle_ammo);
			longneck_rifle.registerProjectile(tranquilizer);
		}
		if (ModuleItemBalance.WEAPONS.SHOTGUN)
		{
			shotgun = addShooter(new ItemShotgun());
			simple_shotgun_ammo = addItemProjectile("simple_shotgun_ammo");
			shotgun.registerProjectile(simple_shotgun_ammo);
		}
		if (ModuleItemBalance.WEAPONS.SIMPLE_PISTOL)
		{
			simple_pistol = addShooter(new ItemSimplePistol());
			simple_bullet = addItemProjectile("simple_bullet");
			simple_pistol.registerProjectile(simple_bullet);
		}
		if (ModuleItemBalance.WEAPONS.ROCKET_LAUNCHER)
		{
			rocket_launcher = addShooter(new ItemRocketLauncher());
			rocket_propelled_grenade = addItemProjectile("rocket_propelled_grenade");
			rocket_launcher.registerProjectile(rocket_propelled_grenade);
		}
		if (ModuleItemBalance.WEAPONS.FABRICATED_PISTOL)
		{
			fabricated_pistol = addShooter(new ItemFabricatedPistol());
			advanced_bullet = addItemProjectile("advanced_bullet");
			fabricated_pistol.registerProjectile(advanced_bullet);
		}
		if (ModuleItemBalance.WEAPONS.CROSSBOW)
		{
			crossbow = addShooter(new ItemCrossbow());
			crossbow.registerProjectile(metal_arrow);
			crossbow.registerProjectile(tranq_arrow);
			crossbow.registerProjectile(stone_arrow);
		}
	}

	public static void registerDispenseBehavior()
	{
		if (simple_bullet != null)
		{
			BlockDispenser.dispenseBehaviorRegistry.putObject(simple_bullet,
					new DispenseSimpleBullet());
		}
		if (simple_shotgun_ammo != null)
		{
			BlockDispenser.dispenseBehaviorRegistry.putObject(
					simple_shotgun_ammo, new DispenseSimpleShotgunAmmo());
		}
		if (simple_rifle_ammo != null)
		{
			BlockDispenser.dispenseBehaviorRegistry.putObject(
					simple_rifle_ammo, new DispenseSimpleRifleAmmo());
		}
		if (tranquilizer != null)
		{
			BlockDispenser.dispenseBehaviorRegistry.putObject(tranquilizer,
					new DispenseTranquilizer());
		}
		if (rocket_propelled_grenade != null)
		{
			BlockDispenser.dispenseBehaviorRegistry.putObject(
					rocket_propelled_grenade,
					new DispenseRocketPropelledGrenade());
		}
	}

	protected static ItemProjectile addItemProjectile(String name)
	{
		ItemProjectile i = new ItemProjectile();
		registerItem(name, i);
		return i;
	}

	protected static ItemRangedWeapon addShooter(ItemRangedWeapon weapon)
	{
		registerItem(weapon.getUnlocalizedName(), weapon);
		return weapon;
	}

	protected static ItemSlingshot addSlingshot(String name)
	{
		ItemSlingshot slingshot = new ItemSlingshot();
		registerItem(name, slingshot);
		return slingshot;
	}

	protected static ItemGrenade addGrenade(String name)
	{
		ItemGrenade slingshot = new ItemGrenade();
		registerItem(name, slingshot);
		return slingshot;
	}

	public static ItemSpear addSpearItem(String name, ToolMaterial mat)
	{
		ItemSpear weapon = new ItemSpear(mat);
		registerItem(name, weapon);
		return weapon;
	}
	
	public static ItemMetalPick addMetalPick(String name, ToolMaterial m)
	{
		ItemMetalPick i = new ItemMetalPick(m);
		registerItem(name, i);
		return i;
	}

	public static ItemStonePick addStonePick(String name, ToolMaterial m)
	{
		ItemStonePick i = new ItemStonePick(m);
		registerItem(name, i);
		return i;
	}

	public static ItemStoneHatchet addStoneHatchet(String name, ToolMaterial m)
	{
		ItemStoneHatchet i = new ItemStoneHatchet(m);
		registerItem(name, i);
		return i;
	}

	public static ItemMetalHatchet addMetalHatchet(String name, ToolMaterial m)
	{
		ItemMetalHatchet i = new ItemMetalHatchet(m);
		registerItem(name, i);
		return i;
	}
	
	public static ARKCraftItem addItem(String name)
	{
		ARKCraftItem i = new ARKCraftItem();
		registerItem(name, i);
		return i;
	}
	
	public static void registerItem(String name, Item item)
	{
		allItems.put(name, item);
		item.setUnlocalizedName(name);
		GameRegistry.registerItem(item, name);
		item.setCreativeTab(ARKCraft.tabARK);
	}

}
