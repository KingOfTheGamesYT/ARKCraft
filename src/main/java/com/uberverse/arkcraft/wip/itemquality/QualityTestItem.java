package com.uberverse.arkcraft.wip.itemquality;

import com.google.common.collect.Multimap;
import com.uberverse.arkcraft.ARKCraft;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class QualityTestItem extends Item implements Qualitable
{
	public QualityTestItem()
	{
		super();
		setCreativeTab(ARKCraft.tabARK);
		setMaxDamage(100);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		stack.damageItem(1, playerIn);

		System.out.println(Qualitable.get(stack));
		ItemQuality q = Qualitable.get(stack);
		switch (q)
		{
			case PRIMITIVE:
				q = ItemQuality.RAMSCHACKLE;
			case RAMSCHACKLE:
				q = ItemQuality.APPRENTICE;
			case APPRENTICE:
				q = ItemQuality.JOURNEYMAN;
			case JOURNEYMAN:
				q = ItemQuality.MASTERCRAFT;
			case MASTERCRAFT:
				q = ItemQuality.ASCENDANT;
			case ASCENDANT:
				q = ItemQuality.PRIMITIVE;
			default:
				q = ItemQuality.ASCENDANT;
		}
		Qualitable.set(stack, q);
		System.out.println(stack.getMaxDamage());
		System.out.println(Qualitable.get(stack));

		return stack;
	}

	@Override
	public Multimap getAttributeModifiers(ItemStack stack)
	{
		Multimap map = super.getAttributeModifiers(stack);
		map.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(),
				new AttributeModifier(itemModifierUUID, "attack_damage", Qualitable.get(stack).multiplierTreshold, 0));
		return map;
	}

}
