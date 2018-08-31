package com.arkcraft.client.render.projectile;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.entity.projectile.EntitySimpleRifleAmmo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderSimpleRifleAmmo extends Render {
	private static final ResourceLocation texture = new ResourceLocation(ARKCraft.MODID
			+ ":textures/entity/bullet.png");

	public RenderSimpleRifleAmmo() {
		super(Minecraft.getMinecraft().getRenderManager());
	}

	public void doRender(EntitySimpleRifleAmmo entityarrow, double d, double d1, double d2, float f, float f1) {
		bindEntityTexture(entityarrow);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d, (float) d1, (float) d2);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		float f2 = 0.0F;
		float f3 = 5F / 16F;
		float f10 = 0.05625F;
		GL11.glEnable(32826 /* GL_RESCALE_NORMAL_EXT */);
		GL11.glScalef(0.07F, 0.07F, 0.07F);
		GL11.glNormal3f(f10, 0.0F, 0.0F);
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bufferBuilder.pos(0D, -0.5D, -0.5D).tex(f2, f2).endVertex();
		bufferBuilder.pos(0D, -0.5D, 0.5D).tex(f3, f2).endVertex();
		bufferBuilder.pos(0D, 0.5D, 0.5D).tex(f3, f3).endVertex();
		bufferBuilder.pos(0D, 0.5D, -0.5D).tex(f2, f3).endVertex();
		tessellator.draw();
		GL11.glNormal3f(-f10, 0.0F, 0.0F);
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bufferBuilder.pos(0D, 0.5D, -0.5D).tex(f2, f2).endVertex();
		bufferBuilder.pos(0D, 0.5D, 0.5D).tex(f3, f2).endVertex();
		bufferBuilder.pos(0D, -0.5D, 0.5D).tex(f3, f3).endVertex();
		bufferBuilder.pos(0D, -0.5D, -0.5D).tex(f2, f3).endVertex();
		tessellator.draw();
		for (int j = 0; j < 4; j++) {
			GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
			GL11.glNormal3f(0.0F, 0.0F, f10);
			bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			bufferBuilder.pos(-0.5D, -0.5D, 0.0D).tex(f2, f2).endVertex();
			bufferBuilder.pos(0.5D, -0.5D, 0.0D).tex(f3, f2).endVertex();
			bufferBuilder.pos(0.5D, 0.5D, 0.0D).tex(f3, f3).endVertex();
			bufferBuilder.pos(-0.5D, 0.5D, 0.0D).tex(f2, f3).endVertex();
			tessellator.draw();
		}

		GL11.glDisable(32826 /* GL_RESCALE_NORMAL_EXT */);
		GL11.glPopMatrix();
	}

	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		doRender((EntitySimpleRifleAmmo) entity, d, d1, d2, f, f1);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return texture;
	}
}
