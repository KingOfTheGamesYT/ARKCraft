package com.uberverse.arkcraft.rework.gui;

import java.awt.Color;
import java.io.IOException;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.I18n;
import com.uberverse.arkcraft.rework.arkplayer.ARKPlayer;
import com.uberverse.arkcraft.rework.container.ContainerPlayerCrafting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GUIPlayerCrafting extends GUIEngramCrafting
{
	private static final ResourceLocation background = new ResourceLocation(ARKCraft.MODID, "textures/gui/player_crafting.png");

	private GuiButton openEngrams;

	public GUIPlayerCrafting(ContainerPlayerCrafting container)
	{
		super(container);
	}

	@Override
	public void initGui()
	{
		super.initGui();

		this.openEngrams = new GuiTexturedButton(buttonCounter++, guiLeft + 113, guiTop + 134, 47, 14, buttons, 0, 84);
		buttonList.add(openEngrams);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		GuiInventory.drawEntityOnScreen(136, 100, 30, 0, 0, this.mc.thePlayer);

		String level = I18n.format("gui.text.level", ARKPlayer.get(Minecraft.getMinecraft().thePlayer).getLevel());
		this.drawString(mc.fontRendererObj, level, 136 - mc.fontRendererObj.getStringWidth(level) / 2, 25, Color.white.getRGB());
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button == openEngrams)
		{
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			player.openGui(ARKCraft.instance, ARKCraft.GUI.ENGRAM_GUI.getID(), player.worldObj, 0, 0, 0);
		}
		else super.actionPerformed(button);
	}

	@Override
	public int getC1ButtonX()
	{
		return 17;
	}

	@Override
	public int getC1ButtonY()
	{
		return 134;
	}

	@Override
	public int getCAButtonX()
	{
		return 53;
	}

	@Override
	public int getCAButtonY()
	{
		return 134;
	}

	@Override
	public int getScrollBarStartX()
	{
		return 8;
	}

	@Override
	public int getScrollBarStartY()
	{
		return 18;
	}

	@Override
	public int getScrollBarEndY()
	{
		return 124 - getScrollButtonHeight();
	}

	@Override
	public ResourceLocation getBackgroundResource()
	{
		return background;
	}

	@Override
	public int getBackgroundWidth()
	{
		return 230;
	}

	@Override
	public int getBackgroundHeight()
	{
		return 256;
	}
}
