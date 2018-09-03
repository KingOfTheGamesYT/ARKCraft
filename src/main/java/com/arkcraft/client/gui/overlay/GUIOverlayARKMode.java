package com.arkcraft.client.gui.overlay;

import com.arkcraft.common.arkplayer.ARKPlayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GUIOverlayARKMode extends Gui
{
	private static final Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void renderGUIOverlay(RenderGameOverlayEvent.Post e)
	{
		if (ARKPlayer.isARKMode(mc.player))
		{
			if (e.getType().equals(ElementType.HELMET))
			{
				String text = "ARK Enabled";

				int x = 2;
				int y = 2;
				drawString(mc.fontRenderer, text, x, y, 0xFFFFFFFF);
			}
		}
	}
}
