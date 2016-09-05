package com.uberverse.arkcraft.client.gui.overlay;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.client.event.ClientEventHandler;
import com.uberverse.arkcraft.common.arkplayer.ARKPlayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GUIOverlayARKMode extends Gui
{
	private static final Minecraft mc = Minecraft.getMinecraft();
	private static final TextureMap iconLocation = new TextureMap(ARKCraft.MODID + "textures/items/azul.png");
	public static int count = 0;

	/*
	 * public void test() {
	 * System.out.println(ClientEventHandler.openOverlay());
	 * //ClientEventHandler evt = new ClientEventHandler(); if (allowGuiOpen !=
	 * ClientEventHandler.openOverlay()) { allowGuiOpen = true;
	 * System.out.println(allowGuiOpen); } }
	 */

	@SubscribeEvent
	public void renderGUIOverlay(RenderGameOverlayEvent.Post e)
	{
		if (ARKPlayer.isARKMode(Minecraft.getMinecraft().thePlayer))
		{
			EntityPlayer p = mc.thePlayer;
			ItemStack stack = p.getCurrentEquippedItem();
			ClientEventHandler handler = new ClientEventHandler();

			if (e.type.equals(ElementType.HELMET))
			{
				String text = "ARK Enabled";

				// this.mc.renderEngine.bindTexture(new
				// ResourceLocation(ARKCraft.MODID,
				// "textures/items/azul.png"));
				// this.drawTexturedModalRect(8, 8, 0, 0, 256, 256);
				// this.drawTexturedModalRect(posX + 8, posY + 8, 0, 0, 256,
				// 256);
				// this.drawTexturedModalRect(xCoord, yCoord, textureSprite,
				// p_175175_4_, p_175175_5_);
				int x = 2;
				int y = 2;
				drawString(mc.fontRendererObj, text, x, y, 0xFFFFFFFF);
				// this.drawTexturedModalRect(x + 8, y + 8, 0, 0, 32, 32);
			}
		}
	}
}
