/**
 * 
 */
package com.uberverse.arkcraft.client.gui;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.block.container.ContainerEngram;
import com.uberverse.arkcraft.common.entity.data.ARKPlayer;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * @author ERBF
 */

public class GUIEngram extends GuiContainer 
{

	private static final ResourceLocation texture = new ResourceLocation(ARKCraft.MODID, "textures/gui/engram_gui.png");
	
	//private InventoryEngram inventory;
	
	public GUIEngram(EntityPlayer player, InventoryPlayer inventory, ARKPlayer arkPlayer)
	{
		super(new ContainerEngram(inventory, player));
		//this.inventory = inventory;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) 
	{
		
	}
	
}
