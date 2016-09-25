package com.uberverse.arkcraft.client.gui.block;

import java.awt.Color;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.client.gui.GUIArkContainer;
import com.uberverse.arkcraft.common.container.block.ContainerCompostBin;
import com.uberverse.arkcraft.common.tileentity.crafter.TileEntityCompostBin;
import com.uberverse.arkcraft.util.I18n;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author wildbill22
 * @author Lewis_McReu
 */
@SideOnly(Side.CLIENT)
public class GUICompostBin extends GUIArkContainer
{
	private static final ResourceLocation texture = new ResourceLocation(ARKCraft.MODID,
			"textures/gui/compost_bin_gui.png");

	public GUICompostBin(EntityPlayer player, TileEntityCompostBin tileEntity)
	{
		super(new ContainerCompostBin(player, tileEntity));
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		String name = I18n.translate("tile.compost_bin.name");
		final int LABEL_YPOS = 7;
		final int LABEL_XPOS = (xSize / 2) - (name.length() * 5 / 2);
		this.fontRendererObj.drawString(name, LABEL_XPOS, LABEL_YPOS, Color.darkGray.getRGB());
	}

	@Override
	public ResourceLocation getBackgroundResource()
	{
		return texture;
	}

	@Override
	public int getBackgroundWidth()
	{
		return 175;
	}

	@Override
	public int getBackgroundHeight()
	{
		return 165;
	}
}
