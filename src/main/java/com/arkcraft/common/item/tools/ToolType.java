package com.arkcraft.common.item.tools;

public enum ToolType
{// TODO: Change to the appropriate values.
	PICKAXE(0.5F, 1.5F), HATCHET(1.5F, 0.5F), SICKLE(1, 1);
	private final float hatchetModifier, pickaxeModifier;

	private ToolType(float primaryModifier, float secondaryModifier)
	{
		this.hatchetModifier = primaryModifier;
		this.pickaxeModifier = secondaryModifier;
	}

	/** eg: Wood from a hatchet, Flint from a hatchet */
	public float getHatchetModifier()
	{
		return hatchetModifier;
	}

	/** eg: Thatch from a hatchet, Stone from a pickaxe */
	public float getPickaxeModifier()
	{
		return pickaxeModifier;
	}
}
