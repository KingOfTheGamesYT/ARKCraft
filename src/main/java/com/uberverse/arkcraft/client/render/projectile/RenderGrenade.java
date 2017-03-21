package com.uberverse.arkcraft.client.render.projectile;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.entity.projectile.EntityGrenade;

public class RenderGrenade extends Render<EntityGrenade>
{
	private static final ResourceLocation texture = new ResourceLocation(ARKCraft.MODID,
			"textures/entity/MetalArrow.png");

	public RenderGrenade()
	{
		super(Minecraft.getMinecraft().getRenderManager());
		shadowSize = 0.5F;

	}

	public void render(EntityGrenade grenade, double d, double d1, double d2, float f, float f1)
	{
		bindEntityTexture(grenade);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d, (float) d1, (float) d2);
		if (grenade.onGround)
		{
			GL11.glRotatef(180F - grenade.getRotationYawHead(), 0.0F, 1.0F, 0.0F);
		}
		else
		{
			float dYaw = (grenade.getRotationYawHead() - grenade.prevRotationYaw);
			for (; dYaw > 180F; dYaw -= 360F)
			{}
			for (; dYaw <= -180F; dYaw += 360F)
			{}
			GL11.glRotatef(180F - grenade.prevRotationYaw - dYaw * f1, 0.0F, 1.0F, 0.0F);
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityGrenade entity)
	{
		return texture;
	}
}