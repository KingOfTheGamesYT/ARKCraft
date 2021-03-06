/**
 * 
 */
package com.uberverse.arkcraft.client.gui.player;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.client.gui.component.GuiTranslatedButton.GuiCenteredTranslatedButton;
import com.uberverse.arkcraft.client.gui.scrollable.GUIScrollable;
import com.uberverse.arkcraft.common.arkplayer.ARKPlayer;
import com.uberverse.arkcraft.common.container.player.ContainerEngram;
import com.uberverse.arkcraft.common.container.player.ContainerEngram.EngramSlot;
import com.uberverse.arkcraft.common.engram.EngramManager.Engram;
import com.uberverse.arkcraft.common.inventory.InventoryEngram;
import com.uberverse.arkcraft.util.I18n;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * @author ERBF
 * @author Lewis_McReu
 */

public class GUIEngram extends GUIScrollable
{
	private static final ResourceLocation texture = new ResourceLocation(ARKCraft.MODID, "textures/gui/engram_gui.png");

	private InventoryEngram inventory;

	private GuiButton learn, close;

	private EntityPlayer player;

	public GUIEngram(EntityPlayer player)
	{
		super(new ContainerEngram(ARKPlayer.get(player).getEngramInventory()));
		this.inventory = ARKPlayer.get(player).getEngramInventory();
		this.player = player;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		super.initGui();
		Keyboard.enableRepeatEvents(true);

		learn = new GuiCenteredTranslatedButton(0, guiLeft + 76, guiTop + 136, "ark.engram.learn", 6);
		learn.enabled = false;
		close = new GuiCenteredTranslatedButton(1, guiLeft + 155, guiTop + 3, "ark.engram.close", 6);

		this.buttonList.add(learn);
		this.buttonList.add(close);
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

		// Draw background layer for engram slots (learned or not)
		for (Object o : inventorySlots.inventorySlots)
		{
			if (o instanceof EngramSlot)
			{
				EngramSlot slot = (EngramSlot) o;
				if (!ARKPlayer.get(player).hasLearnedEngram((short) slot.getSlotIndex()))
				{
					int x = guiLeft + slot.xPos, y = guiTop + slot.yPos, size = 18;
					Color base = Color.black;
					int opacity = 255 / 2; // value from 0 - 255 ; O is
											// completely invisible, 255 is
											// completely opaque
					Color over = new Color(base.getRed(), base.getGreen(), base.getBlue(), opacity);
					drawRect(x, y, x + size, y + size, over.getRGB());
				}
			}
		}
	}

	@Override
	public void updateScreen()
	{
		Engram e = ((ContainerEngram) inventorySlots).getSelectedEngram();
		short s = e != null ? e.getId() : -1;
		learn.enabled = ARKPlayer.get(Minecraft.getMinecraft().player).canLearnEngram(s);
		super.updateScreen();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		// draw text

		String s = I18n.format("gui.engram.text.engrampoints", ARKPlayer.get(player).getEngramPoints());
		fontRendererObj.drawString(s, getCenteredStringOffset(s, fontRendererObj, 80), -10, Color.gray.getRGB());

		if (getEngram() != null)
		{
			int descColor = Color.white.getRGB();
			String title = getEngram().getTitle();
			String description = getEngram().getDescription();
			String points = "Cost: " + getEngram().getPoints(); // TODO localization
			String level = "Level: " + getEngram().getLevel(); // TODO localization
			fontRendererObj.drawString(title, getCenteredStringOffset(title, fontRendererObj, 80), 5, descColor);
			fontRendererObj.drawString(description, getCenteredStringOffset(description, fontRendererObj, 80), 17,
					descColor);
			fontRendererObj.drawString(points, getCenteredStringOffset(points, fontRendererObj, 80), 29, descColor);
			fontRendererObj.drawString(level, getCenteredStringOffset(level, fontRendererObj, 80), 41, descColor);
			// 4210752 original value
		}
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		if (button == close)
		{
			mc.playerController.sendEnchantPacket(inventorySlots.windowId, 0);
		}
		else if (button == learn)
		{
			mc.playerController.sendEnchantPacket(inventorySlots.windowId, 1);
		}
	}

	private static int getCenteredStringOffset(String string, FontRenderer renderer, int center)
	{
		return center - renderer.getStringWidth(string) / 2;
	}

	public Engram getEngram()
	{
		return ((ContainerEngram) inventorySlots).getSelectedEngram();
	}

	public InventoryEngram getInventory()
	{
		return inventory;
	}

	@Override
	public int getScrollBarStartX()
	{
		return 163;
	}

	@Override
	public int getScrollBarStartY()
	{
		return 55;
	}

	@Override
	public int getScrollBarEndY()
	{
		return 133;
	}

	@Override
	public ResourceLocation getBackgroundResource()
	{
		return texture;
	}

	@Override
	public int getBackgroundWidth()
	{
		return 176;
	}

	@Override
	public int getBackgroundHeight()
	{
		return 134;
	}
}
