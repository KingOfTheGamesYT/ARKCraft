package com.uberverse.arkcraft.common.item.tools;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.MathHelper;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum ToolLevel {//TODO: Fill the enum up with the appropriate values.
	PRIMITIVE("primitive", 0.8D),
	TIER2("tr2", 1),
	TIER3("tr3", 1.3)
	;
	private final double durrabilityModifier;
	public static final ToolLevel[] VALUES = values();
	public final String name;
	private ToolLevel(String name, double durrabilityModifier) {
		this.durrabilityModifier = durrabilityModifier;
		this.name = name;
	}
	public int getDurrability(int normal){
		return MathHelper.ceiling_double_int(normal * durrabilityModifier);
	}
	public String getName() {
		return name;
	}
	@SideOnly(Side.CLIENT)
	public String getTranslatedName(){//TODO: Setup the localization entries.
		return I18n.format("arkCraft.toolLevel." + name);
	}
}
