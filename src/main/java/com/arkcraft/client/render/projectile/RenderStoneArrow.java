package com.arkcraft.client.render.projectile;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.entity.projectile.EntityStoneArrow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderStoneArrow extends Render<EntityStoneArrow> {
	private static final ResourceLocation texture = new ResourceLocation(ARKCraft.MODID,
			"textures/entity/stoneArrow.png");

	public RenderStoneArrow() {
		super(Minecraft.getMinecraft().getRenderManager());
	}

	@Override
	public void doRender(EntityStoneArrow p_180551_1_, double p_180551_2_, double p_180551_4_, double p_180551_6_,
						 float p_180551_8_, float p_180551_9_) {
		this.bindEntityTexture(p_180551_1_);
		// GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) p_180551_2_, (float) p_180551_4_, (float) p_180551_6_);
		GlStateManager.rotate(p_180551_1_.prevRotationYaw + (p_180551_1_.rotationYaw - p_180551_1_.prevRotationYaw)
				* p_180551_9_ - 90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(p_180551_1_.prevRotationPitch + (p_180551_1_.rotationPitch
				- p_180551_1_.prevRotationPitch) * p_180551_9_, 0.0F, 0.0F, 1.0F);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		byte b0 = 0;
		float f2 = 0.0F;
		float f3 = 0.5F;
		float f4 = (0 + b0 * 10) / 32.0F;
		float f5 = (5 + b0 * 10) / 32.0F;
		float f6 = 0.0F;
		float f7 = 0.15625F;
		float f8 = (5 + b0 * 10) / 32.0F;
		float f9 = (10 + b0 * 10) / 32.0F;
		float f10 = 0.05625F;
		GlStateManager.enableRescaleNormal();
		float f11 = p_180551_1_.arrowShake - p_180551_9_;

		if (f11 > 0.0F) {
			float f12 = -MathHelper.sin(f11 * 3.0F) * f11;
			GlStateManager.rotate(f12, 0.0F, 0.0F, 1.0F);
		}

		GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(f10, f10, f10);
		GlStateManager.translate(-4.0F, 0.0F, 0.0F);
		GL11.glNormal3f(f10, 0.0F, 0.0F);
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bufferBuilder.pos(-7.0D, -2.0D, -2.0D).tex(f6, f8).endVertex();
		bufferBuilder.pos(-7.0D, -2.0D, 2.0D).tex(f7, f8).endVertex();
		bufferBuilder.pos(-7.0D, 2.0D, 2.0D).tex(f7, f9).endVertex();
		bufferBuilder.pos(-7.0D, 2.0D, -2.0D).tex(f6, f9).endVertex();
		tessellator.draw();
		GL11.glNormal3f(-f10, 0.0F, 0.0F);
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bufferBuilder.pos(-7.0D, 2.0D, -2.0D).tex(f6, f8).endVertex();
		bufferBuilder.pos(-7.0D, 2.0D, 2.0D).tex(f7, f8).endVertex();
		bufferBuilder.pos(-7.0D, -2.0D, 2.0D).tex(f7, f9).endVertex();
		bufferBuilder.pos(-7.0D, -2.0D, -2.0D).tex(f6, f9).endVertex();
		tessellator.draw();

		for (int i = 0; i < 4; ++i) {
			GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
			GL11.glNormal3f(0.0F, 0.0F, f10);
			bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			bufferBuilder.pos(-8.0D, -2.0D, 0.0D).tex(f2, f4).endVertex();
			bufferBuilder.pos(8.0D, -2.0D, 0.0D).tex(f3, f4).endVertex();
			bufferBuilder.pos(8.0D, 2.0D, 0.0D).tex(f3, f5).endVertex();
			bufferBuilder.pos(-8.0D, 2.0D, 0.0D).tex(f2, f5).endVertex();
			tessellator.draw();
		}

		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		super.doRender(p_180551_1_, p_180551_2_, p_180551_4_, p_180551_6_, p_180551_8_, p_180551_9_);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityStoneArrow entity) {

		return texture;
	}
}