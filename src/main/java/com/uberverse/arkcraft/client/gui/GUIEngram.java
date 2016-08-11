/**
 * 
 */
package com.uberverse.arkcraft.client.gui;

import org.lwjgl.opengl.GL11;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.block.container.ContainerEngram;
import com.uberverse.arkcraft.common.entity.data.ARKPlayer;
import com.uberverse.arkcraft.common.inventory.InventoryPlayerEngram;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * @author ERBF
 */

public class GUIEngram extends GuiContainer 
{

	private static final ResourceLocation texture = new ResourceLocation(ARKCraft.MODID, "textures/gui/engram_gui.png");
	
	private InventoryPlayerEngram inventory;
	
	private GuiButton learn, close;
	
	private EntityPlayer player;
	
	private static String engramTitle = "null";
	private static String engramDesc = "null";
	private int engramPoints;
	
	public GUIEngram(EntityPlayer player, InventoryPlayer inventory, int engramPoints)
	{
		super(new ContainerEngram(inventory, player));
		//this.inventory = inventory;
		this.player = player;
		this.engramPoints = engramPoints;
		this.xSize = 170;
		this.ySize = 134;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		super.initGui();
		
		learn = new GuiButton(0, guiLeft + 79 - (mc.fontRendererObj.getStringWidth(I18n.format("ark.engram.learn")) / 2), guiTop + 125, mc.fontRendererObj.getStringWidth(I18n.format("ark.engram.learn")) + 6, 11, I18n.format("ark.engram.learn"));
		close = new GuiButton(1, guiLeft + 154 - (mc.fontRendererObj.getStringWidth(I18n.format("ark.engram.close")) / 2), guiTop + 3, mc.fontRendererObj.getStringWidth(I18n.format("ark.engram.close")) + 6, 11, I18n.format("ark.engram.close"));
		
		this.buttonList.add(learn);
		this.buttonList.add(close);
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
		String s = "Engram Points: " + engramPoints;
		fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, -10, 4210752);
		fontRendererObj.drawString(engramTitle, xSize / 2 - fontRendererObj.getStringWidth(engramTitle), 6, 4210752);
		fontRendererObj.drawString(engramDesc, xSize / 2 - fontRendererObj.getStringWidth(engramDesc), 18, 4210752);
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		if(button == close) {
			player.openGui(ARKCraft.instance, ARKCraft.GUI.PLAYER.getID(), player.worldObj, 0, 0, 0);
		} else if(button == learn) {
			//oh boy
			this.engramPoints = ARKPlayer.get(player).getEngramPoints(); //Updates engram count
		}
	}
	
}
