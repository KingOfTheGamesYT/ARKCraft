package com.uberverse.arkcraft.client.gui.block;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import net.minecraftforge.fml.client.config.GuiButtonExt;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.client.gui.engram.GUIEngramCrafting;
import com.uberverse.arkcraft.common.container.block.ContainerFabricator;
import com.uberverse.arkcraft.common.tileentity.crafter.engram.TileEntityFabricator;

public class GuiFabricator  extends GUIEngramCrafting
{
	public static final ResourceLocation texture = new ResourceLocation(ARKCraft.MODID, "textures/gui/fabricator.png");

	public static final int scrollButtonWidth = 12;
	public static final int scrollButtonHeight = 15;
	public static final int scrollButtonPosX = 232;
	public static final int scrollButtonPosY = 0;
	public static final int scrollButtonPosXDisabled = scrollButtonPosX + 12;
	public static final int scrollButtonStartX = 154;
	public static final int scrollButtonStartY = 18;
	public static final int scrollButtonEndY = 106 - scrollButtonHeight;
	private GuiButton active;
	private TileEntityFabricator te;

	public GuiFabricator(EntityPlayer player, TileEntityFabricator tileEntity)
	{
		super(new ContainerFabricator(player, tileEntity));
		this.te = tileEntity;
	}

	@Override
	public int getC1ButtonX()
	{
		return 92;
	}

	@Override
	public int getC1ButtonY()
	{
		return 116;
	}

	@Override
	public int getCAButtonX()
	{
		return 130;
	}

	@Override
	public int getCAButtonY()
	{
		return 116;
	}

	@Override
	public int getScrollBarStartX()
	{
		return 180;
	}

	@Override
	public int getScrollBarStartY()
	{
		return 18;
	}

	@Override
	public int getScrollBarEndY()
	{
		return 108;
	}

	@Override
	public ResourceLocation getBackgroundResource()
	{
		return texture;
	}

	@Override
	public int getBackgroundWidth()
	{
		return 200;
	}

	@Override
	public int getBackgroundHeight()
	{
		return 222;
	}
	@Override
	public void initGui() {
		super.initGui();
		active = new GuiButtonExt(100, guiLeft + 7, guiTop + 4, 30, 12, "OFF");
		buttonList.add(active);
	}
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button.id == 100){
			mc.playerController.sendEnchantPacket(inventorySlots.windowId, te.isActive() ? 4 : 3);
		}else
			super.actionPerformed(button);
	}
	@Override
	public void updateScreen() {
		super.updateScreen();
		active.displayString = te.isActive() ? TextFormatting.GREEN + "ON" : TextFormatting.RED + "OFF";
	}
}
