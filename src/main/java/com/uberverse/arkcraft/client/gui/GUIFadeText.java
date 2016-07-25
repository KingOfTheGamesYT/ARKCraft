/**
 * 
 */
package com.uberverse.arkcraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;

/**
 * @author ERBF
 *
 */
public class GUIFadeText extends Gui 
{
	
	/*public GUIFadeText(String format, String color, Minecraft minecraft) 
	{
		
	}*/
	
	public static boolean drawMovingText(String format, String color, Minecraft minecraft)
	{
		ScaledResolution scaled = new ScaledResolution(minecraft, minecraft.displayWidth, minecraft.displayHeight);
		int height = scaled.getScaledHeight();
		
		for(int i = height - 10; height >= (height / 2) + 10; height--)
		{
			minecraft.fontRendererObj.drawString(format(format), scaled.getScaledWidth() / 2 - minecraft.fontRendererObj.getStringWidth(format(format)) / 2, i, Integer.parseInt(color, 16), true);
		}
		return true;
	}
	
	private static String format(String format)
	{
		return I18n.format(format);
	}
	
}
