package com.uberverse.arkcraft.util;

public class I18n
{
    public static String format(String key, Object... args)
    {
        return I18n.format(key, args);
    }

    public static String translate(String key)
    {
        return format(key);
    }
}
