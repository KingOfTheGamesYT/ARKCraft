package com.uberverse.arkcraft.client.render.tileentity;

import com.uberverse.arkcraft.ARKCraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TileEntityCrystalRenderer extends TileEntitySpecialRenderer
{
	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick, int destroyStage)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);

		// Your rendering code goes here

		WorldRenderer wr = Tessellator.getInstance().getWorldRenderer();

		wr.startDrawingQuads();

		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(ARKCraft.MODID,
				"blocks/crystal.png"));

		wr.setNormal(0, 0, -1);

		wr.addVertexWithUV(0, 0, 0, 0, 1);
		wr.addVertexWithUV(1, 0, 0, 0, 1);
		wr.addVertexWithUV(0, 1, 0, 0, 1);
		wr.addVertexWithUV(1, 1, 0, 0, 1);
		Tessellator.getInstance().draw();

		GlStateManager.popMatrix();
	}
}