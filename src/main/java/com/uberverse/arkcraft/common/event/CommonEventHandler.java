package com.uberverse.arkcraft.common.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.config.ModuleItemBalance;
import com.uberverse.arkcraft.common.entity.data.ARKPlayer;
import com.uberverse.arkcraft.common.item.firearms.ItemRangedWeapon;
import com.uberverse.arkcraft.common.item.tools.ARKCraftTool;
import com.uberverse.arkcraft.common.network.ReloadFinished;
import com.uberverse.lib.LogHelper;

public class CommonEventHandler {
	public boolean destroy;
	public boolean destroyBlocks;
	public boolean startSwing;

	public static void init() {
		CommonEventHandler handler = new CommonEventHandler();
		FMLCommonHandler.instance().bus().register(handler);
		MinecraftForge.EVENT_BUS.register(handler);
	}

	@SubscribeEvent
	public void onEntityConstructing(EntityEvent.EntityConstructing event) {
		if (event.entity instanceof EntityPlayer
				&& ARKPlayer.get((EntityPlayer) event.entity) == null) {
			ARKPlayer.register((EntityPlayer) event.entity,
					event.entity.worldObj);
			if (event.entity.worldObj.isRemote) // On client
			{
				LogHelper
						.info("ARKPlayerEventHandler: Registered a new ARKPlayer on client.");
			} else {
				LogHelper
						.info("ARKPlayerEventHandler: Registered a new ARKPlayer on server.");
			}
		}
	}

	@SubscribeEvent
	public void onClonePlayer(PlayerEvent.Clone event) {
		LogHelper
				.info("ARKPlayerEventHandler: Cloning player extended properties");
		ARKPlayer.get(event.entityPlayer).copy(ARKPlayer.get(event.original));
	}

	@SubscribeEvent
	public void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entity;
			// Enable pooping once every (the value in the config) ticks
			if (player.ticksExisted
					% ModuleItemBalance.PLAYER.TICKS_BETWEEN_PLAYER_POOP == 0) {
				ARKPlayer.get(player).setCanPoop(true);
			}
		}
	}

	public static int count;


	@SuppressWarnings("static-access")
	@SubscribeEvent
	public void playerInteract(PlayerInteractEvent event) {

		Action eventAction = event.action;
		ItemStack item = event.entityPlayer.getCurrentEquippedItem();
		
		if (item != null && item.getItem() instanceof ItemRangedWeapon) {
			if (eventAction.RIGHT_CLICK_BLOCK != null
					& eventAction.RIGHT_CLICK_AIR != null) {
				ObfuscationReflectionHelper.setPrivateValue(ItemRenderer.class,
						Minecraft.getMinecraft().getItemRenderer(), 1F,
						"equippedProgress", "field_78454_c");
			}
		}
		
		
	}	

	// for (int x = -checkSize; x <= checkSize; x++) {
	// for (int z = -checkSize; z <= checkSize; z++) {
	// for (int y = 0; y <= checkSize; y++) {

	public void destroyBlocks(World world, BlockPos pos, boolean first) {
		// int checkSize = 1;
		int i = pos.getX();
		int j = pos.getY();
		int k = pos.getZ();
		for (int x = i - 1; x <= i + 1; x++) {
			for (int z = k - 1; z <= k + 1; z++) {
				for (int y = j - 1; y <= j + 1; y++) {
					if (first || (x != i && y != j && k != z)) {
						IBlockState blockState = world
								.getBlockState(new BlockPos(x, y, z));
						if (blockState.getBlock() == Blocks.log
								|| blockState.getBlock() == Blocks.log2) {
							// world.destroyBlock(new BlockPos(x, y, z), true);
							count++;
							this.destroyBlocks(world, new BlockPos(x, y, z),
									false);
						}
					}
				}
			}
		}
	}	
	//TODO fix the issues with client and server side, (ClientEventhanlder.arkMode()) is the issues
	public static boolean arkMode;
	
	public boolean arkMode()
	{
		return arkMode;
	}

	@SubscribeEvent
	public void breakSpeed(BreakSpeed event) {
		System.out.println(arkMode);
		if (arkMode
				&& event.entityPlayer.getHeldItem() != null
				&& event.entityPlayer.getHeldItem().getItem() instanceof ARKCraftTool) {
			destroyBlocks(event.entityPlayer.worldObj, event.pos, true);
			float f = count / 20F;
			System.out.println(count + " " + f);
			if (count > 0) {
				event.newSpeed = event.originalSpeed / f / 10;
			}
			count = 0;
		}
	}


	public static int reloadTicks = 0;
	public static int ticksExsisted = 0;
	public static int ticksSwing = 0;

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent evt) {

		EntityPlayer p = evt.player;
		ItemStack stack = p.getCurrentEquippedItem();

		if (stack != null && stack.getItem() instanceof ItemRangedWeapon) {
			ItemRangedWeapon w = (ItemRangedWeapon) stack.getItem();
			if (w.isReloading(stack)) {
				if (++reloadTicks >= w.getReloadDuration()) {
					if (!p.worldObj.isRemote) {
						w.setReloading(stack, p, false);
						reloadTicks = 0;
						w.hasAmmoAndConsume(stack, p);
						w.effectReloadDone(stack, p.worldObj, p);
						ARKCraft.modChannel.sendTo(new ReloadFinished(),
								(EntityPlayerMP) p);
					}
				}
			}
		}	
		
	}
}
