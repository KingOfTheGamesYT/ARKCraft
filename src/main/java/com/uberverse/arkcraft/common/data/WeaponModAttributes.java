package com.uberverse.arkcraft.common.data;


import net.minecraft.entity.ai.attributes.BaseAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;

public class WeaponModAttributes
{
	// TODO check necessity
	public static  BaseAttribute RELOAD_TIME = (new RangedAttribute((BaseAttribute) null, "arkcraft.reloadTime",
			0D, 0D, Double.MAX_VALUE));
	public static  BaseAttribute CONSUME_ITEM = (new RangedAttribute((BaseAttribute) null, "arkcraft.consumeItem",
			0D, 0D, Double.MAX_VALUE));


}