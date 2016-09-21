/**
 * 
 */
package com.uberverse.arkcraft.client.achievement;

import net.minecraft.item.Item;
import net.minecraft.stats.Achievement;

/**
 * @author ERBF
 */
public class ARKAchievement extends Achievement
{

	public ARKAchievement(String id, String name, int x, int y, Item icon, Achievement parent)
	{
		this(id, name, x, y, icon, parent, false);
	}

	public ARKAchievement(String id, String name, int x, int y, Item icon, Achievement parent, boolean isHidden)
	{
		super(id, name, x, y, icon, parent);
		addAchievementToPage(isHidden);
	}

	private void addAchievementToPage(boolean isHidden)
	{
		if (!isHidden)
		{
			ARKCraftAchievements.achievementList.add(this);
		}
	}

}