package com.uberverse.arkcraft.rework.gui;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.rework.container.ContainerSmithy;
import com.uberverse.arkcraft.rework.tileentity.TileEntitySmithy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GUISmithy extends GUIEngramCrafting
{
	public static final ResourceLocation texture = new ResourceLocation(ARKCraft.MODID, "textures/gui/smithy_old.png");

	public static final int scrollButtonWidth = 12;
	public static final int scrollButtonHeight = 15;
	public static final int scrollButtonPosX = 232;
	public static final int scrollButtonPosY = 0;
	public static final int scrollButtonPosXDisabled = scrollButtonPosX + 12;
	public static final int scrollButtonStartX = 154;
	public static final int scrollButtonStartY = 18;
	public static final int scrollButtonEndY = 106 - scrollButtonHeight;

	public GUISmithy(EntityPlayer player, TileEntitySmithy tileEntity)
	{
		super(new ContainerSmithy(player, tileEntity));
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
}
