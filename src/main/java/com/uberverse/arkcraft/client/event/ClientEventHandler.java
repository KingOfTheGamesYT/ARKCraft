package com.uberverse.arkcraft.client.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.container.inventory.InventoryAttachment;
import com.uberverse.arkcraft.common.entity.data.ARKPlayer;
import com.uberverse.arkcraft.common.item.attachments.NonSupporting;
import com.uberverse.arkcraft.common.item.firearms.ItemRangedWeapon;
import com.uberverse.arkcraft.common.network.OpenAttachmentInventory;
import com.uberverse.arkcraft.common.network.OpenPlayerCrafting;
import com.uberverse.arkcraft.common.network.ReloadStarted;
import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.lib.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import com.uberverse.arkcraft.common.network.MessageHover.MessageHoverReq;
import com.uberverse.arkcraft.common.block.tile.IHoverInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.gui.FontRenderer;

public class ClientEventHandler {
	private static KeyBinding reload, attachment, playerPooping, harvestOverlay, playerCrafting;

	private static Minecraft mc = Minecraft.getMinecraft();

	private static Random random = new Random();

	private static int swayTicks;
	private static final int maxTicks = 20;
	private static float yawSway;
	private static float pitchSway;
	public static int disabledEquippItemAnimationTime=0;

	private ItemStack selected;
	private static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation(ARKCraft.MODID,
			"textures/gui/scope.png");
	public boolean showScopeOverlap = false;
	private int ticks = 0;

	public static void init() {
		ClientEventHandler handler = new ClientEventHandler();
		FMLCommonHandler.instance().bus().register(handler);
		MinecraftForge.EVENT_BUS.register(handler);

		reload = new KeyBinding("key.arkcraft.reload", Keyboard.KEY_R, ARKCraft.NAME);
		ClientRegistry.registerKeyBinding(reload);

		playerPooping = new KeyBinding("key.arkcraft.playerPooping", Keyboard.KEY_Z, ARKCraft.NAME);
		ClientRegistry.registerKeyBinding(playerPooping);

		playerCrafting = new KeyBinding("key.arkcraft.playerCrafting", Keyboard.KEY_I, ARKCraft.NAME);
		ClientRegistry.registerKeyBinding(playerCrafting);

		attachment = new KeyBinding("key.attachment", Keyboard.KEY_M, ARKCraft.NAME);
		ClientRegistry.registerKeyBinding(attachment);

		harvestOverlay = new KeyBinding("key.harvestOverlay", Keyboard.KEY_P, ARKCraft.NAME);
		ClientRegistry.registerKeyBinding(harvestOverlay);
	}
	
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event)
	{
		//Reduces the disabledEquippItemAnimationTime value
		/*
		if(disabledEquippItemAnimationTime>0)disabledEquippItemAnimationTime--;
		else if(disabledEquippItemAnimationTime<0)disabledEquippItemAnimationTime=0;	*/
	}

	@SubscribeEvent
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
		// Update CraftingInventory
		if (ARKPlayer.get(event.player).getInventoryBlueprints().isCrafting()) {
			ARKPlayer.get(event.player).getInventoryBlueprints().update();
		}
	}

	public Vec3 getPositionEyes(EntityPlayer player, float partialTick) {
		if (partialTick == 1.0F) {
			return new Vec3(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ);
		} else {
			double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) partialTick;
			double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) partialTick
					+ (double) player.getEyeHeight();
			double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) partialTick;
			return new Vec3(d0, d1, d2);
		}
	}

	public MovingObjectPosition rayTrace(EntityPlayer player, double distance, float partialTick) {
		Vec3 vec3 = getPositionEyes(player, partialTick);
		Vec3 vec31 = player.getLook(partialTick);
		Vec3 vec32 = vec3.addVector(vec31.xCoord * distance, vec31.yCoord * distance, vec31.zCoord * distance);
		return player.worldObj.rayTraceBlocks(vec3, vec32, false, false, true);
	}

	@SubscribeEvent
	public void onMouseEvent(MouseEvent evt) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer thePlayer = mc.thePlayer;

		if (evt.button == 0) {
			ItemStack stack = thePlayer.getCurrentEquippedItem();
			InventoryAttachment att = InventoryAttachment.create(stack);
			if (att != null) {
				showScopeOverlap = stack != null
						&& (att.isScopePresent() || stack.getItem().equals(ARKCraftItems.spy_glass)) && evt.buttonstate;
				selected = stack;
				if (showScopeOverlap)
					evt.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onFOVUpdate(FOVUpdateEvent evt) {
		if (mc.gameSettings.thirdPersonView == 0 && showScopeOverlap) {
			evt.newfov = 1 / 6.0F;
		}
	}

	@SubscribeEvent
	public void onRenderHand(RenderHandEvent evt) {
		if (showScopeOverlap) {
			evt.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void renderWorldLast(RenderWorldLastEvent e)
	{
		EntityPlayer player = mc.thePlayer;
		//Calling the hack for the Item Swap animation
		/*
		if(disabledEquippItemAnimationTime>0){
			Utils.setItemRendererEquippProgress(1, false);
			player.isSwingInProgress=false;
		}	*/
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onRender(RenderGameOverlayEvent evt) {
		Minecraft mc = Minecraft.getMinecraft();
		int ticksOld = ticks;
		if (evt.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS) ticks = 0;
		if (showScopeOverlap && (mc.thePlayer.getCurrentEquippedItem() != selected
				|| !Mouse.isButtonDown(0))) {
			showScopeOverlap = false;
		}
		if (showScopeOverlap) {
			// Render scope
			if (evt.type == RenderGameOverlayEvent.ElementType.HELMET) {
				if (mc.gameSettings.thirdPersonView == 0) {
					evt.setCanceled(true);
					showScope();
				}
			}
			// Remove crosshairs
			else if (evt.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS && showScopeOverlap)
				evt.setCanceled(true);
		}else if (evt.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS){
			MovingObjectPosition mop = rayTrace(mc.thePlayer, 8, evt.partialTicks);
			if(mop.typeOfHit == MovingObjectType.BLOCK){
				TileEntity tile = mc.theWorld.getTileEntity(mop.getBlockPos());
				if(tile instanceof IHoverInfo){
					ticks = ticksOld + 1;
					if(ticks > 30){
						ticks = 0;
						ARKCraft.modChannel.sendToServer(new MessageHoverReq(mop.getBlockPos()));
					}
					List<String> list = new ArrayList<String>();
					((IHoverInfo)tile).addInformation(list);
					int width = evt.resolution.getScaledWidth();
					int height = evt.resolution.getScaledHeight();
					GL11.glPushMatrix();
					mc.entityRenderer.setupOverlayRendering();
					GL11.glEnable(GL11.GL_BLEND);
					OpenGlHelper.glBlendFunc(770, 771, 1, 0);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glDisable(GL11.GL_ALPHA_TEST);
					drawHoveringText(list, width / 2, height / 2, mc.fontRendererObj, width, height);
					GL11.glPopMatrix();
				}
			}
		}
	}

	@SubscribeEvent
	public void holding(RenderLivingEvent.Pre event) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer thePlayer = mc.thePlayer;
		ItemStack stack = thePlayer.getCurrentEquippedItem();
		if (!event.isCanceled() && event.entity.equals(thePlayer) && stack != null) {
			if (stack.getItem() instanceof ItemRangedWeapon) {
				ModelPlayer model = (ModelPlayer) event.renderer.getMainModel();
				model.aimedBow = true;
			}
		}
	}

	public void showScope() {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer thePlayer = mc.thePlayer;

		// add sway
		swayTicks++;
		if (swayTicks > maxTicks) {
			swayTicks = 0;
			if (!thePlayer.isSneaking()) {
				yawSway = ((random.nextFloat() * 2 - 1) / 5) / maxTicks;
				pitchSway = ((random.nextFloat() * 2 - 1) / 5) / maxTicks;
			} else {
				yawSway = ((random.nextFloat() * 2 - 1) / 16) / maxTicks;
				pitchSway = ((random.nextFloat() * 2 - 1) / 16) / maxTicks;
			}
		}

		EntityPlayer p = mc.thePlayer;
		p.rotationPitch += yawSway;
		p.rotationYaw += pitchSway;

		GL11.glPushMatrix();
		mc.entityRenderer.setupOverlayRendering();
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);

		mc.renderEngine.bindTexture(OVERLAY_TEXTURE);

		ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		double width = res.getScaledWidth_double();
		double height = res.getScaledHeight_double();

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();

		worldrenderer.startDrawingQuads();
		worldrenderer.addVertexWithUV(0.0D, height, -90.0D, 0.0D, 1.0D);
		worldrenderer.addVertexWithUV(width, height, -90.0D, 1.0D, 1.0D);
		worldrenderer.addVertexWithUV(width, 0.0D, -90.0D, 1.0D, 0.0D);
		worldrenderer.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
		tessellator.draw();

		GL11.glPopMatrix();
	}

	public static void doReload() {
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		ItemStack stack = player.getCurrentEquippedItem();
		if (stack != null && stack.getItem() instanceof ItemRangedWeapon) {
			ItemRangedWeapon weapon = (ItemRangedWeapon) stack.getItem();
			if (!weapon.isReloading(stack) && weapon.canReload(stack, player)) {
				ARKCraft.modChannel.sendToServer(new ReloadStarted());
				weapon.setReloading(stack, player, true);
			}
		}
	}

	public static boolean openOverlay;
	public int count = 0;

	public static boolean openOverlay() {
		return openOverlay;
	}

	@SubscribeEvent
	public void onPlayerKeypressed(InputEvent.KeyInputEvent event) {
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		if (playerPooping.isPressed()) {
			ARKPlayer.get(player).poop();
		} else if (playerCrafting.isPressed()) {
			player.openGui(ARKCraft.instance(), ARKCraft.GUI.PLAYER.getID(), player.worldObj, 0, 0, 0);
			ARKCraft.modChannel.sendToServer(new OpenPlayerCrafting(true));
		} else if (reload.isPressed()) {
			doReload();
		} else if (attachment.isPressed()) {
			if (player.getCurrentEquippedItem() != null
					&& player.getCurrentEquippedItem().getItem() instanceof ItemRangedWeapon
					&& !(player.getCurrentEquippedItem().getItem() instanceof NonSupporting)) {
				player.openGui(ARKCraft.instance, ARKCraft.GUI.ATTACHMENT_GUI.getID(), player.worldObj, 0, 0, 0);
				ARKCraft.modChannel.sendToServer(new OpenAttachmentInventory());
			}
		} else if (harvestOverlay.isPressed()) {
			if (count == 1) {
				count = count - 1;
			} else {
				count++;
			}
		}
		if (count == 1) {
			openOverlay = true;
		} else {
			openOverlay = false;
		}
	}
	protected void drawHoveringText(List textLines, int x, int y, FontRenderer font, int width, int height)
	{
		if (!textLines.isEmpty())
		{
			GlStateManager.disableRescaleNormal();
			//RenderHelper.disableStandardItemLighting();
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			int k = 0;
			Iterator iterator = textLines.iterator();

			while (iterator.hasNext())
			{
				String s = (String)iterator.next();
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

			this.zLevel = 300.0F;
			int j1 = -267386864;
			this.drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
			this.drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
			this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
			this.drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
			this.drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
			int k1 = 1347420415;
			int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
			this.drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
			this.drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
			this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
			this.drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

			for (int i2 = 0; i2 < textLines.size(); ++i2)
			{
				String s1 = (String)textLines.get(i2);
				int color = -1;
				if(s1.startsWith("#")){
					String c = s1.substring(1, 7);
					s1 = s1.substring(7);
					try{
						color = Integer.parseInt(c, 16);
					}catch(NumberFormatException e){
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

			this.zLevel = 0.0F;
			//GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			//RenderHelper.enableStandardItemLighting();
			GlStateManager.enableRescaleNormal();
		}
	}
	/**
	 * Draws a rectangle with a vertical gradient between the specified colors (ARGB format). Args : x1, y1, x2, y2,
	 * topColor, bottomColor
	 */
	protected void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor)
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
		worldrenderer.addVertex(right, top, this.zLevel);
		worldrenderer.addVertex(left, top, this.zLevel);
		worldrenderer.setColorRGBA_F(f5, f6, f7, f4);
		worldrenderer.addVertex(left, bottom, this.zLevel);
		worldrenderer.addVertex(right, bottom, this.zLevel);
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}
	private float zLevel = 0F;
}
