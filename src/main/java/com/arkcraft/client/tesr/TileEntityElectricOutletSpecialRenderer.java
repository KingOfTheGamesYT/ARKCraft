package com.arkcraft.client.tesr;

import com.arkcraft.common.tileentity.energy.TileEntityElectricOutlet;
import com.arkcraft.init.ARKCraftBlocks;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class TileEntityElectricOutletSpecialRenderer extends TileEntitySpecialRenderer<TileEntityElectricOutlet> {
	@Override
	public void render(TileEntityElectricOutlet te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if (te != null && te.hasWorld() && te.getPos() != null && te.getWorld().getBlockState(te.getPos()).getBlock() == ARKCraftBlocks.electricOutlet) {
			GlStateManager.pushMatrix();
			GlStateManager.disableTexture2D();
			GlStateManager.disableLighting();
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferBuilder = tessellator.getBuffer();
			te.getConsumersClient().forEach(c -> {
				double xEnd = c.x - TileEntityRendererDispatcher.staticPlayerX;
				double yEnd = c.y - TileEntityRendererDispatcher.staticPlayerY;
				double zEnd = c.z - TileEntityRendererDispatcher.staticPlayerZ;
				double xStart = x + .5;
				double yStart = y + .5;
				double zStart = z + .5;

				bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
				bufferBuilder.pos(xStart, yStart, zStart).color(0, 0, 0, 255).endVertex();
				bufferBuilder.pos(xEnd, yEnd, zEnd).color(0, 0, 0, 255).endVertex();
				tessellator.draw();
			});
			GlStateManager.enableLighting();
			GlStateManager.enableTexture2D();
			GlStateManager.popMatrix();
		}
	}
}
