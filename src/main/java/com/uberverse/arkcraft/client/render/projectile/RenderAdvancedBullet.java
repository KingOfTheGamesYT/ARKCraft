package com.uberverse.arkcraft.client.render.projectile;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.entity.projectile.EntityAdvancedBullet;

public class RenderAdvancedBullet extends Render<EntityAdvancedBullet>
{
	private static final ResourceLocation texture = new ResourceLocation(ARKCraft.MODID
			+ ":textures/entity/bullet.png");

	public RenderAdvancedBullet()
	{
		super(Minecraft.getMinecraft().getRenderManager());
	}

	@Override
	public void doRender(EntityAdvancedBullet entity, double d, double d1, double d2, float f, float f1)
	{
		bindEntityTexture(entity);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d, (float) d1, (float) d2);
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer worldrenderer = tessellator.getBuffer();
		float f2 = 0.0F;
		float f3 = 5F / 16F;
		float f10 = 0.05625F;
		GL11.glEnable(32826 /* GL_RESCALE_NORMAL_EXT */);
		GL11.glScalef(0.07F, 0.07F, 0.07F);
		GL11.glNormal3f(f10, 0.0F, 0.0F);
		worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(0D, -0.5D, -0.5D).tex(f2, f2).endVertex();
		worldrenderer.pos(0D, -0.5D, 0.5D).tex(f3, f2).endVertex();
		worldrenderer.pos(0D, 0.5D, 0.5D).tex(f3, f3).endVertex();
		worldrenderer.pos(0D, 0.5D, -0.5D).tex(f2, f3).endVertex();
		tessellator.draw();
		GL11.glNormal3f(-f10, 0.0F, 0.0F);
		worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(0D, 0.5D, -0.5D).tex(f2, f2).endVertex();
		worldrenderer.pos(0D, 0.5D, 0.5D).tex(f3, f2).endVertex();
		worldrenderer.pos(0D, -0.5D, 0.5D).tex(f3, f3).endVertex();
		worldrenderer.pos(0D, -0.5D, -0.5D).tex(f2, f3).endVertex();
		tessellator.draw();
		for (int j = 0; j < 4; j++)
		{
			GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
			GL11.glNormal3f(0.0F, 0.0F, f10);
			worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			worldrenderer.pos(-0.5D, -0.5D, 0.0D).tex(f2, f2).endVertex();
			worldrenderer.pos(0.5D, -0.5D, 0.0D).tex(f3, f2).endVertex();
			worldrenderer.pos(0.5D, 0.5D, 0.0D).tex(f3, f3).endVertex();
			worldrenderer.pos(-0.5D, 0.5D, 0.0D).tex(f2, f3).endVertex();
			tessellator.draw();
		}

		GL11.glDisable(32826 /* GL_RESCALE_NORMAL_EXT */);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityAdvancedBullet entity)
	{
		return texture;
	}
}
