package com.arkcraft.common.block.crafter;

import net.minecraft.util.IStringSerializable;

public enum EnumPart implements IStringSerializable
{
	LEFT("left"), RIGHT("right");
	private final String name;

	private EnumPart(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return this.name;
	}

	@Override
	public String getName()
	{
		return this.name;
	}
}