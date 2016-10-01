/**
 * 
 */
package com.uberverse.arkcraft.client.achievement;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

/**
 * @author ERBF
 */
public class AchievementHelper
{

	public static void registerAchievement(EntityPlayer player, Achievement achievement)
	{
		registerAchievement(player, ARKCraftAchievements.page, achievement);
	}

	public static void registerAchievement(EntityPlayer player, AchievementPage page, Achievement achievement)
	{
		page.getAchievements().add(achievement);
		player.addStat(achievement, 1);
	}

	public static void registerAndDisplay(EntityPlayer player, Achievement achievement)
	{
		registerAndDisplay(player, ARKCraftAchievements.page, achievement);
	}

	public static void registerAndDisplay(EntityPlayer player, AchievementPage page, Achievement achievement)
	{
		registerAchievement(player, page, achievement);
		Minecraft.getMinecraft().guiAchievement.displayAchievement(achievement);
	}

	public static boolean containsAchievement(AchievementPage page, Achievement achievement)
	{
		return page.getAchievements().contains(achievement);
	}

	public static boolean containsAchievement(Achievement achievement)
	{
		return containsAchievement(ARKCraftAchievements.page, achievement);
	}

}
