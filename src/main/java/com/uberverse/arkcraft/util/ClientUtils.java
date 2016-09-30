package com.uberverse.arkcraft.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.network.MessageHover.MessageHoverReq;
import com.uberverse.arkcraft.common.tileentity.IHoverInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@SuppressWarnings({ "rawtypes" })
public class ClientUtils
{
	private static int ticks = 0;

	public static void drawIHoverInfoTooltip(IHoverInfo info,
			FontRenderer fontRenderer, RenderGameOverlayEvent event,
			BlockPos target)
	{
		// TODO replace the information syncing by updating natively in the
		// tileentity and updating info based on that
		ticks++;
		if (ticks > 30)
		{
			ticks = 0;
//			ARKCraft.modChannel.sendToServer(new MessageHoverReq(target));
		}
		List<String> list = new ArrayList<String>();
		info.addInformation(list);
		int width = event.resolution.getScaledWidth();
		int height = event.resolution.getScaledHeight();

		int boxWidth = 0;

		for (String s : list)
		{
			int sW = fontRenderer.getStringWidth(s);
			boxWidth = boxWidth > sW ? boxWidth : sW;
		}

		boxWidth += 16;

		int boxHeight = list.size() * (fontRenderer.FONT_HEIGHT + 2) + 16;

		int y = height - boxHeight;
		int x = width - boxWidth - 10;

		GL11.glPushMatrix();
		Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		ClientUtils.drawHoveringText(list, x, y, fontRenderer, width, height);
		GL11.glPopMatrix();
	}

	public static void drawHoveringText(List textLines, int x, int y,
			FontRenderer font, int width, int height)
	{
		if (!textLines.isEmpty())
		{
			GlStateManager.disableRescaleNormal();
			// RenderHelper.disableStandardItemLighting();
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			int k = 0;
			Iterator iterator = textLines.iterator();

			while (iterator.hasNext())
			{
				String s = (String) iterator.next();
				int l = font.getStringWidth(s);

				if (l > k)
				{
					k = l;
				}
			}

			int j2 = x + 12;
			int k2 = y - 12;
			int i1 = 8;

			if (textLines.size() > 1)
			{
				i1 += 2 + (textLines.size() - 1) * 10;
			}

			if (j2 + k > width)
			{
				j2 -= 28 + k;
			}

			if (k2 + i1 + 6 > height)
			{
				k2 = height - i1 - 6;
			}

			zLevel = 300.0F;
			int j1 = -267386864;
			drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
			drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1,
					j1);
			drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
			drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
			drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1,
					j1);
			int k1 = 1347420415;
			int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
			drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1,
					k1, l1);
			drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3,
					k2 + i1 + 3 - 1, k1, l1);
			drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
			drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1,
					l1);

			for (int i2 = 0; i2 < textLines.size(); ++i2)
			{
				String s1 = (String) textLines.get(i2);
				int color = -1;
				if (s1.startsWith("#"))
				{
					String c = s1.substring(1, 7);
					s1 = s1.substring(7);
					try
					{
						color = Integer.parseInt(c, 16);
					}
					catch (NumberFormatException e)
					{
						color = -1;
					}
				}
				font.drawStringWithShadow(s1, j2, k2, color);

				if (i2 == 0)
				{
					k2 += 2;
				}

				k2 += 10;
			}

			zLevel = 0.0F;
			// GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			// RenderHelper.enableStandardItemLighting();
			GlStateManager.enableRescaleNormal();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	/**
	 * Draws a rectangle with a vertical gradient between the specified colors
	 * (ARGB format). Args : x1, y1, x2, y2, topColor, bottomColor
	 */
	public static void drawGradientRect(int left, int top, int right,
			int bottom, int startColor, int endColor)
	{
		float f = (startColor >> 24 & 255) / 255.0F;
		float f1 = (startColor >> 16 & 255) / 255.0F;
		float f2 = (startColor >> 8 & 255) / 255.0F;
		float f3 = (startColor & 255) / 255.0F;
		float f4 = (endColor >> 24 & 255) / 255.0F;
		float f5 = (endColor >> 16 & 255) / 255.0F;
		float f6 = (endColor >> 8 & 255) / 255.0F;
		float f7 = (endColor & 255) / 255.0F;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.startDrawingQuads();
		worldrenderer.setColorRGBA_F(f1, f2, f3, f);
		worldrenderer.addVertex(right, top, zLevel);
		worldrenderer.addVertex(left, top, zLevel);
		worldrenderer.setColorRGBA_F(f5, f6, f7, f4);
		worldrenderer.addVertex(left, bottom, zLevel);
		worldrenderer.addVertex(right, bottom, zLevel);
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}

	private static float zLevel = 0F;
}
