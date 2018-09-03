package com.arkcraft.client.gui.overlay;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.arkplayer.ARKPlayer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = ARKCraft.MODID, value = Side.CLIENT)
public class GUIOverlayARKMode {
	private static final Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public static void renderGUIOverlay(RenderGameOverlayEvent.Post e) {
		if (ARKPlayer.isARKMode(mc.player)) {
			if (e.getType().equals(ElementType.HELMET)) {
				String text = "ARK Enabled";

				int x = 2;
				int y = 2;
				mc.currentScreen.drawString(mc.fontRenderer, text, x, y, 0xFFFFFFFF);
			}
		}
	}
}
