package com.arkcraft.common.data;

import com.arkcraft.common.entity.projectile.EntityProjectile;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;

public class WeaponDamageSource extends EntityDamageSourceIndirect {
	public WeaponDamageSource(String s, EntityProjectile projectile, Entity entity) {
		super(s, projectile, entity);
	}

	public static DamageSource causeProjectileWeaponDamage(EntityProjectile projectile, Entity entity) {
		return (new WeaponDamageSource("weapon", projectile, entity)).setProjectile();
	}
}