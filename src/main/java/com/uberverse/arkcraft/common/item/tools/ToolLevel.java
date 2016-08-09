package com.uberverse.arkcraft.common.item.tools;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.MathHelper;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum ToolLevel {//TODO: Fill the enum up with the appropriate values.
	PRIMITIVE("primitive", 0.8D),
	RAMSCHACKLE("ramshackle", 1),
	APPRENTICE("apprentice", 1.3),
	JOURNEYMAN("journeyman", 1.5),
	MASTERCRAFT("astercraft", 1.7),
	ASCENDANT("ascendant", 2.0);

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
		return I18n.format("arkcraft.toolLevel." + name);
	}
}
