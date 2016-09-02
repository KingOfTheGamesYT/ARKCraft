package com.uberverse.arkcraft.util;

import net.minecraftforge.fml.common.registry.LanguageRegistry;

public class I18n
{
	public static String format(String key, Object... args)
	{
		return String.format(translate(key), args);
	}

	public static String translate(String key)
	{
		return LanguageRegistry.instance().getStringLocalization(key);
	}
}
