package com.uberverse.arkcraft.common.item.ammo;

import com.uberverse.arkcraft.ARKCraft;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemArrow extends Item
{
	private String name;
	private double damage;

	public ItemArrow(String name, double damage)
	{
		this.name = name;
		this.setCreativeTab(ARKCraft.tabARK);
	}

	public String getName()
	{
		return name;
	}

	public double getDamage()
	{
		return damage;
	}
}
