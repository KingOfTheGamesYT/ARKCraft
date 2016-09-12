package com.uberverse.arkcraft.wip.itemquality;

import com.google.common.collect.Multimap;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;

public abstract class ItemToolBase extends ItemQualitable
{
	private final double baseBreakSpeed;
	private final double baseDamage;

	public ItemToolBase(int baseDurability, double baseBreakSpeed, double baseDamage, ItemType type)
	{
		super(baseDurability, type);
		this.baseBreakSpeed = baseBreakSpeed;
		this.baseDamage = baseDamage;
	}

	public double getAttackDamage(ItemStack stack)
	{
		return Qualitable.get(stack).multiplierTreshold * baseDamage;
	}

	public double getBreakSpeed(ItemStack stack)
	{
		return Qualitable.get(stack).multiplierTreshold * baseBreakSpeed;
	}

	@Override
	public Multimap getAttributeModifiers(ItemStack stack)
	{
		Multimap map = super.getAttributeModifiers(stack);
		map.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(),
				new AttributeModifier(itemModifierUUID, "attack_damage", getAttackDamage(stack), 0));
		return map;
	}
}
