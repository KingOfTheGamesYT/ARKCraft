/**
 * 
 */
package com.uberverse.arkcraft.client.gui;

import org.lwjgl.input.Keyboard;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.block.container.ContainerEngram;
import com.uberverse.arkcraft.common.inventory.InventoryEngram;
import com.uberverse.arkcraft.common.network.OpenPlayerCrafting;
import com.uberverse.arkcraft.common.network.UnlockEngram;
import com.uberverse.arkcraft.rework.arkplayer.ARKPlayer;
import com.uberverse.arkcraft.rework.gui.GUIScrollable;
import com.uberverse.arkcraft.rework.gui.GuiTranslatedButton;
import com.uberverse.arkcraft.rework.gui.GuiTranslatedButton.GuiCenteredTranslatedButton;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * @author ERBF
 */

public class GUIEngram extends GUIScrollable
{
	private static final ResourceLocation texture = new ResourceLocation(ARKCraft.MODID, "textures/gui/engram_gui.png");

	private InventoryEngram inventory;

	private GuiButton learn, close;

	private EntityPlayer player;

	private static String engramTitle = "(null)";
	private static String engramDesc = "(null)";

	public GUIEngram(EntityPlayer player)
	{
		super(new ContainerEngram(ARKPlayer.get(player).getEngramInventory(), player));
		this.inventory = ARKPlayer.get(player).getEngramInventory();
		this.player = player;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		super.initGui();
		Keyboard.enableRepeatEvents(true);

		learn = new GuiCenteredTranslatedButton(0, guiLeft + 76, guiTop + 125, "ark.engram.learn", 6);
		learn.enabled = false;
		close = new GuiTranslatedButton(1, guiLeft + 144, guiTop + 3, "ark.engram.close", 6);

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
		this.drawWorldBackground(8);
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
	}

	@Override
	public void updateScreen()
	{
		short s = ((ContainerEngram) inventorySlots).getSelected();
		learn.enabled = ARKPlayer.get(Minecraft.getMinecraft().thePlayer).canLearnEngram(s);
		super.updateScreen();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		String s = "Engram Points: " + ARKPlayer.get(player).getEngramPoints();
		fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, -10, 4210752);
		fontRendererObj.drawString(engramTitle, drawHalfWidth(engramTitle, fontRendererObj, xSize), 6, 4210752);
		fontRendererObj.drawString(engramDesc, drawHalfWidth(engramDesc, fontRendererObj, xSize), 18, 4210752);
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		if (button == close)
		{
			ARKCraft.modChannel.sendToServer(new OpenPlayerCrafting());
		}
		else if (button == learn)
		{
			ARKCraft.modChannel.sendToServer(new UnlockEngram(((ContainerEngram) inventorySlots).getSelected()));
		}
	}

	private static int drawHalfWidth(String string, FontRenderer renderer, int xSize)
	{
		return xSize / 2 - renderer.getStringWidth(string);
	}

	public static String getEngramTitle()
	{
		return engramTitle;
	}

	public static void setEngramTitle(String newTitle)
	{
		engramTitle = newTitle;
	}

	public static String getEngramDescription()
	{
		return engramDesc;
	}

	public static void setEngramDescription(String newDesc)
	{
		engramDesc = newDesc;
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
		return 44;
	}

	@Override
	public int getScrollBarEndY()
	{
		return 120 - getScrollButtonHeight();
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
