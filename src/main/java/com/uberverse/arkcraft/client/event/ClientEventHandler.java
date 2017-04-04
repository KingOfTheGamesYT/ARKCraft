package com.uberverse.arkcraft.client.event;

import java.awt.event.MouseListener;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.client.easter.Easter;
import com.uberverse.arkcraft.client.model.ModelDodo;
import com.uberverse.arkcraft.client.render.creature.RenderDodo;
import com.uberverse.arkcraft.common.arkplayer.ARKPlayer;
import com.uberverse.arkcraft.common.arkplayer.PlayerWeightCalculator;
import com.uberverse.arkcraft.common.block.crafter.BlockRefiningForge;
import com.uberverse.arkcraft.common.config.WeightsConfig;
import com.uberverse.arkcraft.common.entity.EntityDodo;
import com.uberverse.arkcraft.common.inventory.InventoryAttachment;
import com.uberverse.arkcraft.common.item.attachments.NonSupporting;
import com.uberverse.arkcraft.common.item.ranged.ItemRangedWeapon;
import com.uberverse.arkcraft.common.network.ARKModeToggle;
import com.uberverse.arkcraft.common.network.GunFired;
import com.uberverse.arkcraft.common.network.ReloadStarted;
import com.uberverse.arkcraft.common.network.gui.OpenAttachmentInventory;
import com.uberverse.arkcraft.common.network.gui.OpenPlayerCrafting;
import com.uberverse.arkcraft.common.tileentity.IHoverInfo;
import com.uberverse.arkcraft.common.tileentity.crafter.TileEntityCropPlot;
import com.uberverse.arkcraft.util.ClientUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderGuardian;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RenderSlime;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@EventBusSubscriber
public class ClientEventHandler
{
	private static KeyBinding reload, attachment, playerPooping, arkmode, playerCrafting;

	private static Minecraft mc = Minecraft.getMinecraft();

	private static Random random = new Random();

	private static int swayTicks;
	private static float yawSway;
	private static float pitchSway;
	public static int disabledEquippItemAnimationTime = 0;

	private ItemStack selected;
	private static final ResourceLocation SCOPE_OVERLAY = new ResourceLocation(ARKCraft.MODID, "textures/gui/scope.png");
	private static final ResourceLocation SPYGLASS_OVERLAY = new ResourceLocation(ARKCraft.MODID, "textures/gui/spyglass.png");
	public boolean showScopeOverlap = false;

	public static void init()
	{
		reload = new KeyBinding("key.arkcraft.reload", Keyboard.KEY_R, ARKCraft.instance().name());
		ClientRegistry.registerKeyBinding(reload);

		playerPooping = new KeyBinding("key.arkcraft.playerPooping", Keyboard.KEY_Z, ARKCraft.instance().name());
		ClientRegistry.registerKeyBinding(playerPooping);

		playerCrafting = new KeyBinding("key.arkcraft.playerCrafting", Keyboard.KEY_I, ARKCraft.instance().name());
		ClientRegistry.registerKeyBinding(playerCrafting);

		attachment = new KeyBinding("key.attachment", Keyboard.KEY_M, ARKCraft.instance().name());
		ClientRegistry.registerKeyBinding(attachment);

		arkmode = new KeyBinding("key.harvestOverlay", Keyboard.KEY_P, ARKCraft.instance().name());
		ClientRegistry.registerKeyBinding(arkmode);

		ClientEventHandler h = new ClientEventHandler();
		MinecraftForge.EVENT_BUS.register(h);
	}

	@SubscribeEvent
	public void mouseOverTooltip(ItemTooltipEvent event)
	{
		// TODO localize
		if (WeightsConfig.isEnabled) {
			ItemStack stack = event.getItemStack();
			double weight = PlayerWeightCalculator.getWeight(stack);
			event.getToolTip().add(ChatFormatting.BOLD + "" + ChatFormatting.WHITE + "Weight: " + weight);
			if (stack.stackSize > 1) {
				event.getToolTip().add("Stack Weight: " + (weight * stack.stackSize));
			}
		}
	}

	public Vec3d getPositionEyes(EntityPlayer player, float partialTick)
	{
		if (partialTick == 1.0F) {

			return new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
		}
		else {
			double d0 = player.prevPosX + (player.posX - player.prevPosX) * partialTick;
			double d1 = player.prevPosY + (player.posY - player.prevPosY) * partialTick + player.getEyeHeight();
			double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTick;
			return new Vec3d(d0, d1, d2);
		}
	}

	public RayTraceResult rayTrace(EntityPlayer player, double distance, float partialTick)
	{
		Vec3d vec3 = getPositionEyes(player, partialTick);
		Vec3d vec31 = player.getLook(partialTick);
		Vec3d vec32 = vec3.addVector(vec31.xCoord * distance, vec31.yCoord * distance, vec31.zCoord * distance);
		return player.world.rayTraceBlocks(vec3, vec32, false, false, true);
	}
	
	boolean mouseclicked = false;
	
	
	@SubscribeEvent
	public void onMouseEvent(MouseEvent evt)
	{
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer thePlayer = mc.player;
		ItemStack rightHandStack = thePlayer.getHeldItemMainhand();
		InventoryAttachment att = InventoryAttachment.create(rightHandStack);

		if (rightHandStack != null && rightHandStack.getItem() instanceof ItemRangedWeapon) 
		{
			if(evt.getButton() == 0)
			{
				evt.setCanceled(true);
			}
			if(evt.getButton() == 1)
			{
				evt.setCanceled(true);
				if (att != null && att.isScopePresent()) {
					showScopeOverlap = evt.isButtonstate();
					System.out.println(showScopeOverlap);
					selected = rightHandStack;
				//	if (showScopeOverlap)
				}
			}
		}
		
	}

	private boolean showSpyglassOverlay;

	@SubscribeEvent
	public void onFOVUpdate(FOVUpdateEvent evt)
	{
		if (mc.gameSettings.thirdPersonView == 0 && (showScopeOverlap || showSpyglassOverlay))
			evt.setNewfov(evt.getNewfov() / 6.0F);
	}

	@SubscribeEvent
	public void onRenderHand(RenderHandEvent evt)
	{
		if (showScopeOverlap || showSpyglassOverlay) {
			evt.setCanceled(true);
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onRender(RenderGameOverlayEvent evt)
	{
		Minecraft mc = Minecraft.getMinecraft();
	//	if ((showScopeOverlap || showSpyglassOverlay) && (mc.player.getActiveItemStack() != selected || !Mouse.isButtonDown(0))) {
	//		showScopeOverlap = false;
	//		showSpyglassOverlay = false;
	//	}
		if (showScopeOverlap || showSpyglassOverlay) {
			// Render scope
			if (evt.getType() == RenderGameOverlayEvent.ElementType.HELMET) {
				if (mc.gameSettings.thirdPersonView == 0) {
					evt.setCanceled(true);
					if (showScopeOverlap)
						showScope();
					else if (showSpyglassOverlay)
						showSpyglass();
				}
			}
			// Remove crosshairs
			else if (evt.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS && (showScopeOverlap || showSpyglassOverlay))
				evt.setCanceled(true);
		}
		ItemStack stack = mc.player.getActiveItemStack();
		if (evt.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS && (stack != null && stack.getItem() instanceof ItemRangedWeapon))
			evt.setCanceled(true);
		else if (evt.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS && !Minecraft.getMinecraft().isGamePaused()) {
			RayTraceResult mop = rayTrace(mc.player, 8, evt.getPartialTicks());
			if (mop != null && mop.typeOfHit == RayTraceResult.Type.BLOCK) {
				TileEntity tile = mc.world.getTileEntity(mop.getBlockPos());
				if (tile instanceof TileEntityCropPlot) {
					tile = ((TileEntityCropPlot) tile).getCenter();
				}
				if (tile instanceof IHoverInfo) {
					ClientUtils.drawIHoverInfoTooltip((IHoverInfo) tile, mc.fontRendererObj, evt, mop.getBlockPos());
				}
			}
		}
	}

	@SubscribeEvent
	public void holding(RenderLivingEvent.Pre event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer thePlayer = mc.player;
		ItemStack stack = thePlayer.getActiveItemStack();
		if (!event.isCanceled() && event.getEntity().equals(thePlayer) && stack != null) {
			if (stack.getItem() instanceof ItemRangedWeapon) {
				ModelPlayer model = (ModelPlayer) event.getRenderer().getMainModel();
				// TODO adapt for left/right handed
				model.rightArmPose = ArmPose.BOW_AND_ARROW;
			}
		}
	}

	private static final int maxTicks = 70;

	public void showScope()
	{
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer thePlayer = mc.player;

		// add sway
		swayTicks++;
		if (swayTicks > maxTicks) {
			// change values here for control of the amount of sway!
			int divider = thePlayer.isSneaking() ? 20 : 7;
			swayTicks = 0;
			yawSway = ((random.nextFloat() * 2 - 1) / divider) / maxTicks;
			pitchSway = ((random.nextFloat() * 2 - 1) / divider) / maxTicks;
		}

		EntityPlayer p = mc.player;
		p.rotationPitch += pitchSway;
		p.rotationYaw += yawSway;

		GL11.glPushMatrix();
		mc.entityRenderer.setupOverlayRendering();
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);

		mc.renderEngine.bindTexture(SCOPE_OVERLAY);

		ScaledResolution res = new ScaledResolution(mc);
		double width = res.getScaledWidth_double();
		double height = res.getScaledHeight_double();

		Tessellator tessellator = Tessellator.getInstance();

		// TODO check correct use of vertexbuffer
		VertexBuffer buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_2D, buffer.getVertexFormat());
		buffer.pos(0.0D, height, -90.0D).tex(0.0D, 1.0D).endVertex();
		buffer.pos(width, height, -90.0D).tex(1.0D, 1.0D).endVertex();
		buffer.pos(width, 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
		buffer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
		tessellator.draw();
		// END TODO

		GL11.glPopMatrix();
	}

	public void showSpyglass()
	{
		Minecraft mc = Minecraft.getMinecraft();

		GL11.glPushMatrix();
		mc.entityRenderer.setupOverlayRendering();
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);

		mc.renderEngine.bindTexture(SPYGLASS_OVERLAY);

		ScaledResolution res = new ScaledResolution(mc);
		double width = res.getScaledWidth_double();
		double height = res.getScaledHeight_double();

		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer buffer = tessellator.getBuffer();

		buffer.begin(GL11.GL_2D, buffer.getVertexFormat());
		buffer.pos(0.0D, height, -90.0D).tex(0.0D, 1.0D).endVertex();
		buffer.pos(width, height, -90.0D).tex(1.0D, 1.0D).endVertex();
		buffer.pos(width, 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
		buffer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();

		// TODO ORIGINAL WORLDRENDERER CODE
		// worldrenderer.startDrawingQuads();
		// worldrenderer.addVertexWithUV(0.0D, height, -90.0D, 0.0D, 1.0D);
		// worldrenderer.addVertexWithUV(width, height, -90.0D, 1.0D, 1.0D);
		// worldrenderer.addVertexWithUV(width, 0.0D, -90.0D, 1.0D, 0.0D);
		// worldrenderer.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
		tessellator.draw();

		GL11.glPopMatrix();
	}

	public static void doReload()
	{
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
		if (stack != null && stack.getItem() instanceof ItemRangedWeapon) {
			ItemRangedWeapon weapon = (ItemRangedWeapon) stack.getItem();
			if (!weapon.isReloading(stack) && weapon.canReload(stack, player)) {
				ARKCraft.modChannel.sendToServer(new ReloadStarted());
				weapon.setReloading(stack, player, true);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerKeypressed(InputEvent.KeyInputEvent event)
	{
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		if (playerPooping.isPressed()) {
			System.out.println("Pressed z");
			ARKPlayer.get(player).go();
		}
		else if (playerCrafting.isPressed()) {
			ARKCraft.modChannel.sendToServer(new OpenPlayerCrafting());
		}
		else if (reload.isPressed()) {
			doReload();
		}
		else if (attachment.isKeyDown()) {
			if (player.getHeldItem(EnumHand.MAIN_HAND) != null && player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemRangedWeapon && !(player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof NonSupporting)) {
				ARKCraft.modChannel.sendToServer(new OpenAttachmentInventory());
			}
		}
		else if (arkmode.isPressed()) {
			ARKPlayer.get(Minecraft.getMinecraft().player).toggleARKMode();
			ARKCraft.modChannel.sendToServer(new ARKModeToggle());
		}
	}

	@SubscribeEvent
	public void onPlayerJoinWorld(EntityJoinWorldEvent event)
	{
		if (event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntity();
			if (event.getEntity().world.isRemote) {
				ARKPlayer.get(player).requestSynchronization(true);
			}
		}
	}
/*
	@SubscribeEvent
	public void easter(PlayerInteractEvent.RightClickBlock event)
	{
		if (event.getEntity().world.getBlockState(event.getPos()).getBlock() instanceof BlockRefiningForge) {
			Easter.handleInteract(event);
		}
	}
	
	// Cancel use animation when holding guns

	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
	{
		onRightClick(event.getItemStack());
	}

	@SubscribeEvent
	public static void onRightClickEmpty(PlayerInteractEvent.RightClickItem event)
	{
		onRightClick(event.getItemStack());
	}

	private static void onRightClick(ItemStack held)
	{
		if (held != null && held.getItem() instanceof ItemRangedWeapon) {
			ObfuscationReflectionHelper.setPrivateValue(ItemRenderer.class, Minecraft.getMinecraft().getItemRenderer(), 1F, "equippedProgressMainHand", "field_187469_f");
		}
	} */
}
