package com.uberverse.arkcraft.client.gui.component;

import com.uberverse.arkcraft.util.I18n;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiButtonExt;

/**
 * @author Lewis_McReu
 */
public class GuiTranslatedButton extends GuiButtonExt
{
	// TODO maybe change current buttons to translated ones (display issues with overlap)
	public GuiTranslatedButton(int buttonId, int x, int y, String textKey, int spacing)
	{
		super(buttonId, x, y, Minecraft.getMinecraft().fontRendererObj.getStringWidth(I18n.format(textKey)) + spacing,
				Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + spacing, I18n.format(textKey));
	}

	public static class GuiCenteredTranslatedButton extends GuiTranslatedButton
	{
		public GuiCenteredTranslatedButton(int buttonId, int x, int y, String textKey, int spacing)
		{
			super(buttonId, x - (Minecraft.getMinecraft().fontRendererObj.getStringWidth(I18n.translate(textKey)) / 2), y, textKey, spacing);
		}
	}
}
