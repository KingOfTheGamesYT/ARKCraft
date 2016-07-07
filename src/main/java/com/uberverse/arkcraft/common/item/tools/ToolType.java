package com.uberverse.arkcraft.common.item.tools;

public enum ToolType
{//TODO: Change to the appropriate values.
	PICKAXE(0.5F, 1.5F), HATCHET(1.5F, 0.5F), SICKLE(1,1)
	;
	/**Primary: eg: Wood
	 * Secondary: eg: Thatch*/
	private final float primaryModifier, secondaryModifier;
	private ToolType(float primaryModifier, float secondaryModifier) {
		this.primaryModifier = primaryModifier;
		this.secondaryModifier = secondaryModifier;
	}
	public float getPrimaryModifier() {
		return primaryModifier;
	}
	public float getSecondaryModifier() {
		return secondaryModifier;
	}
}
