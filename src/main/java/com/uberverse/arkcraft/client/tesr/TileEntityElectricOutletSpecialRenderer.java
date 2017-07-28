package com.uberverse.arkcraft.client.tesr;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import com.uberverse.arkcraft.common.tileentity.energy.TileEntityElectricOutlet;
import com.uberverse.arkcraft.init.ARKCraftBlocks;

public class TileEntityElectricOutletSpecialRenderer extends TileEntitySpecialRenderer<TileEntityElectricOutlet> {
	@Override
	public void renderTileEntityAt(TileEntityElectricOutlet te, double x, double y, double z, float partialTicks, int destroyStage) {
		if(te != null && te.hasWorld() && te.getPos() != null && te.getWorld().getBlockState(te.getPos()).getBlock() == ARKCraftBlocks.electricOutlet){
			GlStateManager.pushMatrix();
			GlStateManager.disableTexture2D();
			GlStateManager.disableLighting();
			Tessellator tessellator = Tessellator.getInstance();
			VertexBuffer vertexbuffer = tessellator.getBuffer();
			te.getConsumersClient().forEach(c -> {
				double xEnd = c.xCoord - TileEntityRendererDispatcher.staticPlayerX;
				double yEnd = c.yCoord - TileEntityRendererDispatcher.staticPlayerY;
				double zEnd = c.zCoord - TileEntityRendererDispatcher.staticPlayerZ;
				double xStart = x + .5;
				double yStart = y + .5;
				double zStart = z + .5;

				vertexbuffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
				vertexbuffer.pos(xStart, yStart, zStart).color(0, 0, 0, 255).endVertex();
				vertexbuffer.pos(xEnd, yEnd, zEnd).color(0, 0, 0, 255).endVertex();
				tessellator.draw();
			});
			GlStateManager.enableLighting();
			GlStateManager.enableTexture2D();
			GlStateManager.popMatrix();
		}
	}
}
