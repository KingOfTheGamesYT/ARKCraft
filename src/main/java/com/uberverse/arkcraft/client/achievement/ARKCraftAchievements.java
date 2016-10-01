/**
 * 
 */
package com.uberverse.arkcraft.client.achievement;

import java.util.ArrayList;
import java.util.List;

import com.uberverse.arkcraft.init.ARKCraftRangedWeapons;

import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

/**
 * @author ERBF
 */
public class ARKCraftAchievements
{
	public static AchievementPage page;

	public static Achievement achievementMichaelBay;

	public static List<ARKAchievement> achievementList = new ArrayList<ARKAchievement>();

	public static void init()
	{
		achievementMichaelBay = new Achievement("achievement.michaelBay", "theBigBang", 0, 0,
				ARKCraftRangedWeapons.rocket_propelled_grenade, (Achievement) null).setSpecial();

		page = new AchievementPage("ARKCraft", getList());

		AchievementPage.registerAchievementPage(page);
	}

	private static Achievement[] getList()
	{
		Achievement[] a = new Achievement[achievementList.size()];
		for (int i = 0; i < a.length; i++)
		{
			a[i] = achievementList.get(i);
		}
		return a;
	}

}