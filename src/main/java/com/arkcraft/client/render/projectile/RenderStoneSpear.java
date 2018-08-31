package com.arkcraft.client.render.projectile;

import com.arkcraft.ARKCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderStoneSpear extends Render {
	private static final ResourceLocation spearTextures = new ResourceLocation(ARKCraft.MODID
			+ ":textures/entity/stone_spear.png");

	public RenderStoneSpear() {
		super(Minecraft.getMinecraft().getRenderManager());
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
		this.getEntityTexture(entity);
		GL11.glPushMatrix();

		GL11.glTranslatef((float) x, (float) y, (float) z);
		GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks
				- 90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch)
				* partialTicks, 0.0F, 0.0F, 1.0F);

		GL11.glScalef(3F, 3F, 3F);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();

		float su1 = 0;
		float sv1 = 2F / 32F;
		float su2 = 19F / 32F;
		float sv2 = 3F / 32F;

		float f10 = 0.05625F;
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);

		GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(f10, f10, f10);
		GL11.glTranslatef(-4.0F, 0.0F, 0.0F);

		for (int i = 0; i < 4; ++i) {
			GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			GL11.glNormal3f(0.0F, 0.0F, f10);
			bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			bufferBuilder.pos(-10.0D, -2.0D, 0.0D).tex(su1, sv1).endVertex();
			bufferBuilder.pos(10.0D, -2.0D, 0.0D).tex(su2, sv1).endVertex();
			bufferBuilder.pos(10.0D, 2.0D, 0.0D).tex(su2, sv2).endVertex();
			bufferBuilder.pos(-10.0D, 2.0D, 0.0D).tex(su1, sv2).endVertex();
			tessellator.draw();
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return spearTextures;
	}
}