package com.uberverse.arkcraft.common.event;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.client.event.ClientEventHandler;
import com.uberverse.arkcraft.common.config.ModuleItemBalance;
import com.uberverse.arkcraft.common.entity.data.ARKPlayer;
import com.uberverse.arkcraft.common.item.firearms.ItemRangedWeapon;
import com.uberverse.arkcraft.common.item.tools.ARKCraftTool;
import com.uberverse.arkcraft.common.network.ReloadFinished;
import com.uberverse.lib.LogHelper;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
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
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

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

		boolean arkMode = false;
		Action eventAction = event.action;
		World world = Minecraft.getMinecraft().theWorld;
		ItemStack currentTool = event.entityPlayer.getCurrentEquippedItem();

		/*
		if (currentTool != null && currentTool.getItem() instanceof ARKCraftTool && arkMode != ClientEventHandler.openOverlay()) {
			if (eventAction.LEFT_CLICK_BLOCK != null) {
				IBlockState blockState = world.getBlockState(event.pos);
				if (blockState.getBlock() == Blocks.log) {
					count = count + 1;
					System.out.println(count);
					if(count == 4) 
					{
						this.destroyBlocks(world, event.pos);
						count = 0;
					}
				}
			}
		}*/

	}	
	
//	for (int x = -checkSize; x <= checkSize; x++) {
//		for (int z = -checkSize; z <= checkSize; z++) {
//			for (int y = 0; y <= checkSize; y++) {

	public void destroyBlocks(World world, BlockPos pos, boolean first) {
		//int checkSize = 1;
		int i = pos.getX();
		int j = pos.getY();
		int k = pos.getZ();
		for (int x = i - 1; x <= i + 1; x++) {
			for (int z = k - 1; z <= k + 1; z++) {
				for (int y = j - 1; y <= j + 1; y++) {
					if (first || (x != i && y != j && k != z)) {
						IBlockState blockState = world
								.getBlockState(new BlockPos(x, y, z));
						if (blockState.getBlock() == Blocks.log || blockState.getBlock() == Blocks.log2) {
							//world.destroyBlock(new BlockPos(x, y, z), true);
							count++;
							this.destroyBlocks(world, new BlockPos(x, y, z), false);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void breakSpeed(BreakSpeed event){
		if(ClientEventHandler.openOverlay() && event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem().getItem() instanceof ARKCraftTool){
			destroyBlocks(event.entityPlayer.worldObj, event.pos, true);
			float f = count / 20F;
			System.out.println(count +" " + f);
			if(count > 0){
				event.newSpeed = event.originalSpeed / f / 10;
			}
			count = 0;
		}
	}

	@SubscribeEvent
	public void onBreakEvent(BlockEvent.BreakEvent event) {

	}

	public static int reloadTicks = 0;
	public static int ticksExsisted = 0;
	public static int ticksSwing = 0;

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent evt) {
		
		EntityPlayer p = evt.player;
		ItemStack stack = p.getCurrentEquippedItem();
		boolean arkMode = false;
		World world = Minecraft.getMinecraft().theWorld;
		
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
		/*
		else if(stack != null ){
			float x = p.swingProgress;
			System.out.println(x);
		}	*/
//		else if(stack != null && stack.getItem() instanceof ARKCraftTool) 
	//	{
			
			/*
			if(p.isSwingInProgress = true)
			{
				ticksSwing = 200;
				++ticksExsisted;
				p.swingProgressInt = 1;
				
				if(ticksExsisted >= 100)
				{
					p.isSwingInProgress = false;
					p.swingProgressInt = 0;
					ticksExsisted = 0;
				}
				else
	            {
	                p.swingProgressInt = 0;
	            }                		
			} */	
			/*
			System.out.println(p.swingProgressInt + " Swing Progress Int");	
			System.out.println(p.swingProgress + " Swing Progress");
			System.out.println(p.isSwingInProgress + " is Swing");
			
			if(p.swingProgressInt == 0)
			{
				++ticksExsisted;
				
				if(ticksExsisted >= 100)
				{
					p.isSwingInProgress = true;
					p.swingProgressInt = 1;
					ticksExsisted = 0;
				}  
			}
		} */
	}
}
