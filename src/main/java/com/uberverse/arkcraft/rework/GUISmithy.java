package com.uberverse.arkcraft.rework;

import com.uberverse.arkcraft.ARKCraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GUISmithy extends GUIEngramCrafting
{
	public static final ResourceLocation texture = new ResourceLocation(ARKCraft.MODID,
			"textures/gui/smithy_old.png");
	public static final ResourceLocation scrollButton = new ResourceLocation(
			"textures/gui/container/creative_inventory/tabs.png");

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
		return 84;
	}

	@Override
	public int getC1ButtonY()
	{
		return 111;
	}

	@Override
	public int getC1ButtonU()
	{
		return 176;
	}

	@Override
	public int getC1ButtonV()
	{
		return 42;
	}

	@Override
	public int getC1ButtonWidth()
	{
		return 32;
	}

	@Override
	public int getC1ButtonHeight()
	{
		return 14;
	}

	@Override
	public ResourceLocation getC1ButtonResource()
	{
		return texture;
	}

	@Override
	public int getCAButtonX()
	{
		return 120;
	}

	@Override
	public int getCAButtonY()
	{
		return 111;
	}

	@Override
	public int getCAButtonU()
	{
		return 176;
	}

	@Override
	public int getCAButtonV()
	{
		return 0;
	}

	@Override
	public int getCAButtonWidth()
	{
		return 47;
	}

	@Override
	public int getCAButtonHeight()
	{
		return 14;
	}

	@Override
	public ResourceLocation getCAButtonResource()
	{
		return texture;
	}

	@Override
	public int getScrollButtonWidth()
	{
		return 12;
	}

	@Override
	public int getScrollButtonHeight()
	{
		return 15;
	}

	@Override
	public int getScrollButtonU()
	{
		return 232;
	}

	@Override
	public int getScrollButtonV()
	{
		return 0;
	}

	@Override
	public int getScrollButtonDisabledU()
	{
		return getScrollButtonU() + getScrollButtonWidth();
	}

	@Override
	public int getScrollButtonDisabledV()
	{
		return 0;
	}

	@Override
	public ResourceLocation getScrollButtonResource()
	{
		return scrollButton;
	}

	@Override
	public int getScrollBarStartX()
	{
		return 154;
	}

	@Override
	public int getScrollBarStartY()
	{
		return 18;
	}

	@Override
	public int getScrollBarEndY()
	{
		return 106 - getScrollButtonHeight();
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
		return 222;
	}
}
