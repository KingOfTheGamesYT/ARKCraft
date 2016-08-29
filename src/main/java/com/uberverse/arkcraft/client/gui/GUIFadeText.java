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

	public GUIFadeText(String format, String color, Minecraft minecraft)
	{
		ScaledResolution scaled = new ScaledResolution(minecraft, minecraft.displayWidth,
				minecraft.displayHeight);
		int height = scaled.getScaledHeight();
		minecraft.fontRendererObj.drawString(format(format),
				scaled.getScaledWidth() / 2 - minecraft.fontRendererObj
						.getStringWidth(format(format)) / 2,
				10, /* Integer.parseInt(color, 16) */ 255, true);
	}

	private String format(String format)
	{
		return I18n.format(format);
	}

}
