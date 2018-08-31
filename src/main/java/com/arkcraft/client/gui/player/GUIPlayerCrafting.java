package com.arkcraft.client.gui.player;

import java.awt.Color;
import java.io.IOException;

import com.arkcraft.ARKCraft;
import com.arkcraft.client.gui.component.GuiTexturedButton;
import com.arkcraft.client.gui.engram.GUIEngramCrafting;
import com.arkcraft.common.arkplayer.ARKPlayer;
import com.arkcraft.common.container.player.ContainerPlayerCrafting;
import com.arkcraft.util.I18n;
import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class GUIPlayerCrafting extends GUIEngramCrafting
{
	private static final ResourceLocation background = new ResourceLocation(ARKCraft.MODID,
			"textures/gui/player_crafting.png");
	private static final ResourceLocation xpBar = new ResourceLocation(ARKCraft.MODID, "textures/gui/buttons.png");
	private static final int barX = 33;
	private static final int barY = 159;
	private static final int barU = 0;
	private static final int barV = 136;
	private static final int barWidth = 141;
	private static final int barHeight = 6;

	private GuiButton openEngrams;

	public GUIPlayerCrafting(ContainerPlayerCrafting container)
	{
		super(container);
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.openEngrams = new GuiTexturedButton(buttonCounter++, guiLeft + 128, guiTop + 17, 47, 14, buttons, 0, 84);
		buttonList.add(openEngrams);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		mc.getTextureManager().bindTexture(xpBar);
		drawTexturedModalRect(guiLeft + barX, guiTop + barY, barU, barV, (int) (barWidth * ARKPlayer.get(mc.player)
				.getRelativeXP()), barHeight);
	}

	private boolean dragModel = false;
	private final int xPos = 151;
	private final int yPos = 117;
	private int xLook = 0;
	private int yLook = 0;

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		int x = 127, y = 54, width = 49, height = 70;
		if (mouseButton == 0 && mc.player.inventory.getItemStack() == null && isPointInRegion(x, y, width, height,
				mouseX, mouseY)) dragModel = true;
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
	{
		if (dragModel)
		{
			xLook -= Mouse.getDX();
			yLook += Mouse.getDY();

			if (xLook > 180) xLook -= 360;
			else if (xLook < -180) xLook += 360;
			if (yLook > 180) yLook -= 360;
			else if (yLook < -180) yLook += 360;
		}
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state)
	{
		if (dragModel)
		{
			dragModel = false;
		}
		super.mouseReleased(mouseX, mouseY, state);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		renderEntityWithFullXRotation(xPos, yPos, 30, xLook, yLook, this.mc.player);

		String level = I18n.format("gui.playercrafting.level", ARKPlayer.get(Minecraft.getMinecraft().player)
				.getLevel());
		this.drawString(mc.fontRendererObj, level, 151 - mc.fontRendererObj.getStringWidth(level) / 2, 40, Color.white
				.getRGB());

		if (isPointInRegion(barX, barY, barWidth, barHeight, mouseX, mouseY))
		{
			ARKPlayer p = ARKPlayer.get(mc.player);
			String xp = Math.round(p.getXP() * 100d) / 100d + "/" + Math.round(p.getRequiredXP() * 100d) / 100d;
			drawHoveringText(Lists.newArrayList(new String[] { xp }), mouseX - guiLeft, mouseY - guiTop,
					fontRendererObj);
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button == openEngrams)
		{
			mc.playerController.sendEnchantPacket(inventorySlots.windowId, 2);
		}
		else super.actionPerformed(button);
	}

	@Override
	public int getC1ButtonX()
	{
		return 10;
	}

	@Override
	public int getC1ButtonY()
	{
		return 134;
	}

	@Override
	public int getCAButtonX()
	{
		return 48;
	}

	@Override
	public int getCAButtonY()
	{
		return 134;
	}

	@Override
	public int getScrollBarStartX()
	{
		return 8;
	}

	@Override
	public int getScrollBarStartY()
	{
		return 18;
	}

	@Override
	public int getScrollBarEndY()
	{
		return 126;
	}

	@Override
	public ResourceLocation getBackgroundResource()
	{
		return background;
	}

	@Override
	public int getBackgroundWidth()
	{
		return 256;
	}

	@Override
	public int getBackgroundHeight()
	{
		return 256;
	}

	public static void renderEntityWithFullXRotation(int xPos, int yPos, int size, float rotationX, float rotationY,
			EntityLivingBase entity)
	{
		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) xPos, (float) yPos, 50.0F);
		GlStateManager.scale((float) (-size), (float) size, (float) size);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		float f2 = entity.renderYawOffset;
		float f3 = entity.rotationYaw;
		float f4 = entity.rotationPitch;
		float f5 = entity.prevRotationYawHead;
		float f6 = entity.rotationYawHead;
		GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-((float) Math.atan((double) (rotationY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
		entity.renderYawOffset = rotationX;// (float) Math.atan((double)
											// (rotationX / 40.0F)) * 20.0F;
		entity.rotationYaw = rotationX;// (float) Math.atan((double) (rotationX
										// / 40.0F)) * 40.0F;
		entity.rotationPitch = 0;
		entity.rotationYawHead = entity.rotationYaw;
		entity.prevRotationYawHead = entity.rotationYaw;
		GlStateManager.translate(0.0F, 0.0F, 0.0F);
		RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
		rendermanager.setPlayerViewY(180.0F);
		rendermanager.setRenderShadow(false);
		rendermanager.doRenderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, true);
		rendermanager.setRenderShadow(true);
		entity.renderYawOffset = f2;
		entity.rotationYaw = f3;
		entity.rotationPitch = f4;
		entity.prevRotationYawHead = f5;
		entity.rotationYawHead = f6;
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}
}
