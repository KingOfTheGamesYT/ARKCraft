package com.uberverse.arkcraft.init;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.config.ModuleItemBalance;
import com.uberverse.arkcraft.common.entity.projectile.EntityAdvancedBullet;
import com.uberverse.arkcraft.common.entity.projectile.EntityMetalArrow;
import com.uberverse.arkcraft.common.entity.projectile.EntityRocketPropelledGrenade;
import com.uberverse.arkcraft.common.entity.projectile.EntitySimpleBullet;
import com.uberverse.arkcraft.common.entity.projectile.EntitySimpleRifleAmmo;
import com.uberverse.arkcraft.common.entity.projectile.EntitySimpleShotgunAmmo;
import com.uberverse.arkcraft.common.entity.projectile.EntitySpear;
import com.uberverse.arkcraft.common.entity.projectile.EntityStone;
import com.uberverse.arkcraft.common.entity.projectile.EntityStoneArrow;
import com.uberverse.arkcraft.common.entity.projectile.EntityTranqArrow;
import com.uberverse.arkcraft.common.entity.projectile.EntityTranquilizer;
import com.uberverse.arkcraft.common.handlers.EntityHandler;
import com.uberverse.arkcraft.common.item.ammo.ItemArrow;
import com.uberverse.arkcraft.common.item.ammo.ItemProjectile;
import com.uberverse.arkcraft.common.item.attachments.AttachmentType;
import com.uberverse.arkcraft.common.item.attachments.ItemAttachment;
import com.uberverse.arkcraft.common.item.explosives.ItemRocketLauncher;
import com.uberverse.arkcraft.common.item.ranged.ItemARKBow;
import com.uberverse.arkcraft.common.item.ranged.ItemFabricatedPistol;
import com.uberverse.arkcraft.common.item.ranged.ItemLongneckRifle;
import com.uberverse.arkcraft.common.item.ranged.ItemRangedWeapon;
import com.uberverse.arkcraft.common.item.ranged.ItemShotgun;
import com.uberverse.arkcraft.common.item.ranged.ItemSimplePistol;
import com.uberverse.arkcraft.common.item.ranged.ItemSlingshot;

import net.minecraft.block.BlockDispenser;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class ARKCraftRangedWeapons
{

	public static ItemAttachment scope, flash_light, silencer, laser, holo_scope;
	public static ItemProjectile tranquilizer, simple_bullet, simple_rifle_ammo, simple_shotgun_ammo,
			rocket_propelled_grenade, advanced_bullet;
	public static ItemRangedWeapon rocket_launcher, tranq_gun;
	public static ItemRangedWeapon simple_pistol;
	public static ItemRangedWeapon fabricated_pistol;
	public static ItemRangedWeapon longneck_rifle;
	public static ItemRangedWeapon shotgun;
	public static ItemRangedWeapon crossbow;
	public static ItemSlingshot slingshot;
	public static ItemArrow stone_arrow, metal_arrow, tranq_arrow;
	public static ItemARKBow bow;

	public static ToolMaterial METAL = EnumHelper.addToolMaterial("METAL_MAT", 3, 1500, 6.0F, 0.8F, 8);
	public static ToolMaterial STONE = EnumHelper.addToolMaterial("STONE_MAT", 2, 500, 3.5F, 0.4F, 13);

	public static void init()
	{
		InitializationManager init = InitializationManager.instance();

		// weapon attachments
		scope = addItemAttachment("scope", AttachmentType.SCOPE);
		flash_light = addItemAttachment("flash_light", AttachmentType.FLASH);
		holo_scope = addItemAttachment("holo_scope", AttachmentType.HOLO_SCOPE);
		laser = addItemAttachment("laser", AttachmentType.LASER);
		silencer = addItemAttachment("silencer", AttachmentType.SILENCER);

		stone_arrow = addItemArrow("stone_arrow", 3); // TODO tweak!
		metal_arrow = addItemArrow("metal_arrow", 6);
		tranq_arrow = addItemArrow("tranq_arrow", 1);

		bow = init.registerItem("bow", new ItemARKBow(1), "bow", "bow_pulling_0", "bow_pulling_1", "bow_pulling_2");
		slingshot = init.registerItem("slingshot", new ItemSlingshot(), "slingshot_pulled", "slingshot");

		EntityHandler.registerModEntity(EntityStone.class, "stone", ARKCraft.instance(), 64, 10, true);
		EntityHandler.registerModEntity(EntitySpear.class, "spear", ARKCraft.instance(), 16, 20, true);
		EntityHandler.registerModEntity(EntityStoneArrow.class, "stone_arrow", ARKCraft.instance(), 64, 10, true);
		EntityHandler.registerModEntity(EntityMetalArrow.class, "metal_arrow", ARKCraft.instance(), 64, 10, true);
		EntityHandler.registerModEntity(EntityTranqArrow.class, "tranq_arrow", ARKCraft.instance(), 64, 10, true);

		registerWeaponEntities();
		addRangedWeapons();
	}

	private static ItemAttachment addItemAttachment(String name, AttachmentType type)
	{
		return InitializationManager.instance().registerItem(name, new ItemAttachment(name, type));
	}

	private static void registerWeaponEntities()
	{
		if (ModuleItemBalance.WEAPONS.SIMPLE_PISTOL)
		{
			EntityHandler.registerModEntity(EntitySimpleBullet.class, "simple_bullet", ARKCraft.instance(), 16, 20,
					true);
		}

		if (ModuleItemBalance.WEAPONS.SHOTGUN)
		{
			EntityHandler.registerModEntity(EntitySimpleShotgunAmmo.class, "simple_shotgun_ammo", ARKCraft.instance(),
					64, 10, true);
		}

		if (ModuleItemBalance.WEAPONS.LONGNECK_RIFLE)
		{
			EntityHandler.registerModEntity(EntitySimpleRifleAmmo.class, "simple_rifle_ammo", ARKCraft.instance(), 64,
					10, true);
			EntityHandler.registerModEntity(EntityTranquilizer.class, "tranquilizer_dart", ARKCraft.instance(), 64, 10,
					true);
		}

		if (ModuleItemBalance.WEAPONS.FABRICATED_PISTOL)
		{
			EntityHandler.registerModEntity(EntityAdvancedBullet.class, "advanced_bullet", ARKCraft.instance(), 64, 10,
					true);
		}

		if (ModuleItemBalance.WEAPONS.ROCKET_LAUNCHER)
		{
			EntityHandler.registerModEntity(EntityRocketPropelledGrenade.class, "rocket_propelled_grenade", ARKCraft
					.instance(), 64, 10, true);
		}
	}

	public static void addRangedWeapons()
	{
		InitializationManager init = InitializationManager.instance();

		if (ModuleItemBalance.WEAPONS.LONGNECK_RIFLE)
		{
			simple_rifle_ammo = addItemProjectile("simple_rifle_ammo");
			tranquilizer = addItemProjectile("tranquilizer");
			longneck_rifle = init.registerItem("longneck_rifle", "weapons/", new ItemLongneckRifle(), "longneck_rifle",
					"longneck_rifle_scope", "longneck_rifle_scope_reload", "longneck_rifle_reload",
					"longneck_rifle_flashlight", "longneck_rifle_flashlight_reload", "longneck_rifle_laser",
					"longneck_rifle_laser_reload", "longneck_rifle_silencer", "longneck_rifle_silencer_reload");
			longneck_rifle.registerProjectile(simple_rifle_ammo);
			longneck_rifle.registerProjectile(tranquilizer);
		}
		if (ModuleItemBalance.WEAPONS.SHOTGUN)
		{
			shotgun = init.registerItem("shotgun", "weapons/", new ItemShotgun(), "shotgun", "shotgun_reload");
			simple_shotgun_ammo = addItemProjectile("simple_shotgun_ammo");
			shotgun.registerProjectile(simple_shotgun_ammo);
		}
		if (ModuleItemBalance.WEAPONS.SIMPLE_PISTOL)
		{
			simple_pistol = init.registerItem("simple_pistol", "weapons/", new ItemSimplePistol(), "simple_pistol",
					"simple_pistol_scope", "simple_pistol_reload", "simple_pistol_scope_reload",
					"simple_pistol_flashlight", "simple_pistol_flashlight_reload", "simple_pistol_laser",
					"simple_pistol_laser_reload", "simple_pistol_silencer", "simple_pistol_silencer_reload");
			simple_bullet = addItemProjectile("simple_bullet");
			simple_pistol.registerProjectile(simple_bullet);
		}
		if (ModuleItemBalance.WEAPONS.ROCKET_LAUNCHER)
		{
			rocket_launcher = init.registerItem("rocket_launcher", "weapons/", new ItemRocketLauncher());
			rocket_propelled_grenade = addItemProjectile("rocket_propelled_grenade");
			rocket_launcher.registerProjectile(rocket_propelled_grenade);
		}
		if (ModuleItemBalance.WEAPONS.FABRICATED_PISTOL)
		{
			fabricated_pistol = init.registerItem("fabricated_pistol", "weapons/", new ItemFabricatedPistol(),
					"fabricated_pistol", "fabricated_pistol_scope", "fabricated_pistol_reload",
					"fabricated_pistol_scope_reload", "fabricated_pistol_flashlight",
					"fabricated_pistol_flashlight_reload", "fabricated_pistol_laser", "fabricated_pistol_laser_reload",
					"fabricated_pistol_silencer", "fabricated_pistol_silencer_reload", "fabricated_pistol_holo_scope",
					"fabricated_pistol_holo_scope_reload");
			advanced_bullet = addItemProjectile("advanced_bullet");
			fabricated_pistol.registerProjectile(advanced_bullet);
		}
		if (ModuleItemBalance.WEAPONS.CROSSBOW)
		{
			// crossbow = addShooter(new ItemCrossbow());
		}
	}

	protected static ItemProjectile addItemProjectile(String name)
	{
		return InitializationManager.instance().registerItem(name, new ItemProjectile());
	}

	protected static ItemArrow addItemArrow(String name, double damage)
{
	return InitializationManager.instance().registerItem(name, new ItemArrow());
	}
}
