/**
 * 
 */
package com.uberverse.arkcraft.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.block.container.ContainerEngram;
import com.uberverse.arkcraft.common.container.scrollable.IContainerScrollable;
import com.uberverse.arkcraft.common.container.scrollable.IGuiScrollable;
import com.uberverse.arkcraft.common.entity.data.ARKPlayer;
import com.uberverse.arkcraft.common.inventory.InventoryPlayerEngram;
import com.uberverse.arkcraft.common.item.engram.ARKCraftEngrams;
import com.uberverse.arkcraft.common.item.engram.Engram;
import com.uberverse.arkcraft.common.network.ScrollingMessage;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * @author ERBF
 */

public class GUIEngram extends GuiContainer implements IGuiScrollable
{
	private static final ResourceLocation texture = new ResourceLocation(ARKCraft.MODID,
			"textures/gui/engram_gui.png");

	private InventoryPlayerEngram inventory;

	private GuiButton learn, close;

	private EntityPlayer player;

	private static String engramTitle = "(null)";
	private static String engramDesc = "(null)";

	private int scrollPosition;
	private boolean scrollClicked = false;

	public GUIEngram(EntityPlayer player)
	{
		super(new ContainerEngram(ARKPlayer.get(player).getEngramInventory(), player));
		this.inventory = ARKPlayer.get(player).getEngramInventory();
		this.player = player;
		this.xSize = 170;
		this.ySize = 134;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		super.initGui();
		Keyboard.enableRepeatEvents(true);

		learn = new GuiButton(0,
				guiLeft + 79 - (mc.fontRendererObj
						.getStringWidth(I18n.format("ark.engram.learn")) / 2),
				guiTop + 125,
				mc.fontRendererObj.getStringWidth(I18n.format("ark.engram.learn")) + 6, 11,
				I18n.format("ark.engram.learn"));
		close = new GuiButton(1,
				guiLeft + 154 - (mc.fontRendererObj
						.getStringWidth(I18n.format("ark.engram.close")) / 2),
				guiTop + 3, mc.fontRendererObj.getStringWidth(I18n.format("ark.engram.close")) + 6,
				11, I18n.format("ark.engram.close"));

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
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY)
	{
		this.drawWorldBackground(8);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String s = "Engram Points: " + ARKPlayer.get(player).getEngramPoints();
		fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, -10,
				4210752);
		fontRendererObj.drawString(engramTitle, drawHalfWidth(engramTitle, fontRendererObj, xSize),
				6, 4210752);
		fontRendererObj.drawString(engramDesc, drawHalfWidth(engramDesc, fontRendererObj, xSize),
				18, 4210752);
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		if (button == close)
		{
			player.openGui(ARKCraft.instance, ARKCraft.GUI.PLAYER.getID(), player.worldObj, 0, 0,
					0);
		}
		else if (button == learn)
		{
			Engram selectedEngram = ARKCraftEngrams.getEngramByLocalizedName(getEngramTitle()); // selected
																								// engram

			// DEBUGGING
			System.out.println(selectedEngram != null ? selectedEngram.getFormattedName() : "null");
			System.out.println(selectedEngram != null ? selectedEngram.getFormattedDesc() : "null");

			if (ARKPlayer.get(player).getEngramPoints() >= selectedEngram.getRequiredPoints())
			{
				selectedEngram.setLearned();

				int pos = 0;
				for (Engram engram : ARKCraftEngrams.engramList)
				{
					if (engram == selectedEngram)
					{
						ARKPlayer.get(player).addLearnedEngram(selectedEngram, pos);
						break;
					}
					pos++;
				}
			}
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

	public InventoryPlayerEngram getInventory()
	{
		return inventory;
	}

	@Override
	public int getScrollPosition()
	{
		return scrollPosition;
	}

	@Override
	public void handleMouseInput() throws IOException
	{
		int scrollAmount = Mouse.getEventDWheel();
		if (scrollAmount != 0 && canScroll())
		{
			if (scrollAmount > 0) scrollAmount = -1;
			else if (scrollAmount < 0) scrollAmount = 1;
			adjustScroll(scrollAmount);
		}
		else super.handleMouseInput();
	}

	private void adjustScroll(int scrollAmount)
	{
		((IContainerScrollable) this.inventorySlots).scroll(scrollAmount);
		ARKCraft.modChannel.sendToServer(new ScrollingMessage(scrollAmount));
	}

	@Override
	public boolean canScroll()
	{
		return ((IContainerScrollable) this.inventorySlots).canScroll();
	}
}
