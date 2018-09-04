package com.arkcraft.registry;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.item.ammo.ItemArrow;
import com.arkcraft.common.item.ammo.ItemProjectile;
import com.arkcraft.common.item.attachments.ItemAttachment;
import com.arkcraft.common.item.melee.ItemPike;
import com.arkcraft.common.item.melee.ItemSpear;
import com.arkcraft.common.item.ranged.ItemARKBow;
import com.arkcraft.common.item.ranged.ItemRangedWeapon;
import com.arkcraft.common.item.ranged.ItemSlingshot;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(ARKCraft.MODID)
public class WeaponRegistry {
	@GameRegistry.ObjectHolder("spear")
	public static final ItemSpear SPEAR = null;
	@GameRegistry.ObjectHolder("pike")
	public static final ItemPike PIKE = null;

	@GameRegistry.ObjectHolder("scope")
	public static final ItemAttachment SCOPE = null;
	@GameRegistry.ObjectHolder("flash_light")
	public static final ItemAttachment FLASH_LIGHT = null;
	@GameRegistry.ObjectHolder("silencer")
	public static final ItemAttachment SILENCER = null;
	@GameRegistry.ObjectHolder("laser")
	public static final ItemAttachment LASER = null;
	@GameRegistry.ObjectHolder("holo_scope")
	public static final ItemAttachment HOLO_SCOPE = null;

	@GameRegistry.ObjectHolder("tranquilizer")
	public static final ItemProjectile TRANQUILIZER = null;
	@GameRegistry.ObjectHolder("simple_bullet")
	public static final ItemProjectile SIMPLE_BULLET = null;
	@GameRegistry.ObjectHolder("simple_rifle_ammo")
	public static final ItemProjectile SIMPLE_RIFLE_AMMO = null;
	@GameRegistry.ObjectHolder("simple_shotgun_ammo")
	public static final ItemProjectile SIMPLE_SHOTGUN_AMMO = null;
	@GameRegistry.ObjectHolder("rocket_propelled_grenade")
	public static final ItemProjectile ROCKET_PROPELLED_GRENADE = null;
	@GameRegistry.ObjectHolder("advanced_bullet")
	public static final ItemProjectile ADVANCED_BULLET = null;
	@GameRegistry.ObjectHolder("stone_arrow")
	public static final ItemArrow STONE_ARROW = null;
	@GameRegistry.ObjectHolder("metal_arrow")
	public static final ItemArrow METAL_ARROW = null;
	@GameRegistry.ObjectHolder("tranq_arrow")
	public static final ItemArrow TRANQUILIZING_ARROW = null;

	@GameRegistry.ObjectHolder("rocket_launcher")
	public static final ItemRangedWeapon ROCKET_LAUNCHER = null;
	@GameRegistry.ObjectHolder("simple_pistol")
	public static final ItemRangedWeapon SIMPLE_PISTOL = null;
	@GameRegistry.ObjectHolder("fabricated_pistol")
	public static final ItemRangedWeapon FABRICATED_PISTOL = null;
	@GameRegistry.ObjectHolder("longneck_rifle")
	public static final ItemRangedWeapon LONGNECK_RIFLE = null;
	@GameRegistry.ObjectHolder("shotgun")
	public static final ItemRangedWeapon SHOTGUN = null;
	@GameRegistry.ObjectHolder("crossbow")
	public static final ItemRangedWeapon CROSSBOW = null;
	@GameRegistry.ObjectHolder("slingshot")
	public static final ItemSlingshot SLINGSHOT = null;
	@GameRegistry.ObjectHolder("bow")
	public static final ItemARKBow BOW = null;
}
