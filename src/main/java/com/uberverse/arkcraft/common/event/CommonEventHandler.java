package com.uberverse.arkcraft.common.event;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.config.ModuleItemBalance;
import com.uberverse.arkcraft.common.entity.data.ARKPlayer;
import com.uberverse.arkcraft.common.item.firearms.ItemRangedWeapon;
import com.uberverse.arkcraft.common.item.tools.ARKCraftTool;
import com.uberverse.arkcraft.common.network.ReloadFinished;
import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.lib.LogHelper;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
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
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class CommonEventHandler
{
	public boolean destroy;
	public boolean destroyBlocks;
	public boolean startSwing;

	public static File f;
	public static PrintWriter out;
	
	public static void init()
	{
		CommonEventHandler handler = new CommonEventHandler();
		FMLCommonHandler.instance().bus().register(handler);
		MinecraftForge.EVENT_BUS.register(handler);
		try {
			f = new File(".", "user_commands.txt");
			System.out.println("[MessagePrinter] Output: " + f.getAbsolutePath());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	@SubscribeEvent
	public void onEntityConstructing(EntityEvent.EntityConstructing event)
	{
		if (event.entity instanceof EntityPlayer && ARKPlayer
				.get((EntityPlayer) event.entity) == null)
		{
			ARKPlayer.register((EntityPlayer) event.entity, event.entity.worldObj);
			if (event.entity.worldObj.isRemote) // On client
			{
				LogHelper.info("ARKPlayerEventHandler: Registered a new ARKPlayer on client.");
			}
			else
			{
				LogHelper.info("ARKPlayerEventHandler: Registered a new ARKPlayer on server.");
			}
		}
	}

	@SubscribeEvent
	public void onClonePlayer(PlayerEvent.Clone event)
	{
		LogHelper.info("ARKPlayerEventHandler: Cloning player extended properties");
		ARKPlayer.get(event.entityPlayer).copy(ARKPlayer.get(event.original));
	}

	@SubscribeEvent
	public void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event)
	{
		// LogHelper.info("LIVING UPDATE EVENT");
		if (event.entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.entity;
			// Enable pooping once every (the value in the config) ticks
			if (player.ticksExisted % ModuleItemBalance.PLAYER.TICKS_BETWEEN_PLAYER_POOP == 0)
			{
				ARKPlayer.get(player).setCanPoop(true);
			}
		}
	}

	private static final Set<Item> INPUTS = ImmutableSet.of(Items.bone, Items.book, Items.feather);
	
	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event) {
		if (event.side.isServer()) {
			//LogHelper.info("[OnWorldTick] The Side is on the server!");
			World world = event.world;
				
			EntityItem itemToSpawn = null;
			if (!world.isRemote) {
				if (bookSpawnDelay > 0) bookSpawnDelay--;
				else {
				//LogHelper.info("The world is not remote.");
				List<Entity> entitiesInWorld = world.loadedEntityList;
				for(Entity entityInWorld : entitiesInWorld) {
					final Set<Item> remainingInputs = new HashSet<>(INPUTS); // Create a mutable copy of the input set to track which items have been found
					ArrayList<EntityItem> foundEntityItems = new ArrayList<EntityItem>();
					//LogHelper.info("Found an Entity in the world!");
					if(entityInWorld instanceof EntityItem) {
						EntityItem entityItemInWorld = (EntityItem)entityInWorld;
						if(entityItemInWorld.getEntityItem().getItem() == Items.book) {
							LogHelper.info("Found an Entity in the world that is a book!");
							remainingInputs.remove(Items.book);
							foundEntityItems.add(entityItemInWorld);
							AxisAlignedBB areaBound = entityItemInWorld.getEntityBoundingBox().expand(3, 3, 3);
							List<Entity> entitiesWithinBound = world.getEntitiesWithinAABBExcludingEntity(entityItemInWorld, areaBound);
								for (Entity entityWithinBound : entitiesWithinBound) {
									if (entityWithinBound instanceof EntityItem) {
										EntityItem entityItemWithinBound = (EntityItem) entityWithinBound;
										if (entityItemWithinBound.getEntityItem().getItem() == Items.bone) {
											LogHelper.info("Found an Entity near the book that is a bone!");
											remainingInputs.remove(Items.bone);
											if(!remainingInputs.contains(entityItemWithinBound)) foundEntityItems.add(entityItemWithinBound);
										} else if (entityItemWithinBound.getEntityItem().getItem() == Items.feather) {
											LogHelper.info("Found an Entity near the book that is a feather!");
											remainingInputs.remove(Items.feather);
											if(!remainingInputs.contains(entityItemWithinBound)) foundEntityItems.add(entityItemWithinBound);
										}
										if (remainingInputs.isEmpty()) {
											LogHelper.info("All items have been found. The Items hashmap is empty.");
											for (EntityItem foundEntityItem : foundEntityItems) {												
												bookSpawnDelay += 20;
												foundEntityItem.getEntityItem().stackSize--;
												if (foundEntityItem.getEntityItem().stackSize <= 0) {
													LogHelper.info("Deleting the Item: " + foundEntityItem.getEntityItem().getItem().toString());
													foundEntityItem.setDead();
												}
												((WorldServer)world).spawnParticle(EnumParticleTypes.SMOKE_LARGE, false,
														entityItemInWorld.posX + 0.5D,
														entityItemInWorld.posY + 1.0D,
														entityItemInWorld.posZ + 0.5D, 
														1, 0.0D, 0.0D, 0.0D, 0.0D, new int[0]
												);
												itemToSpawn = new EntityItem(world, entityItemInWorld.posX, entityItemInWorld.posY, entityItemInWorld.posZ, new ItemStack(ARKCraftItems.info_book, 1));
											}
										}

									}
								}

							}
						}
					foundEntityItems.clear();
					}
				if(itemToSpawn != null) world.spawnEntityInWorld(itemToSpawn);
				}
			}
		}
	}
	
						/*
						for (int j = 1; j < entities.size(); j++)
						{
							if (entities.get(i) instanceof EntityItem && entities
									.get(j) instanceof EntityItem)
							{
								EntityItem target = (EntityItem) entities.get(i);
								EntityItem targetJ = (EntityItem) entities.get(j);

								
								int x = target.getPosition().getX();
								int y = target.getPosition().getY();
								int z = target.getPosition().getZ();

								int xJ = targetJ.getPosition().getX();
								int yJ = targetJ.getPosition().getY();
								int zJ = targetJ.getPosition().getZ();

								Item item = target.getEntityItem().getItem();
								Item itemJ = targetJ.getEntityItem().getItem();
									
								if ((item == Items.book && itemJ == Items.bone) || (item == Items.bone && itemJ == Items.book) && !target.isDead && !targetJ.isDead)
								{
									if (target.ticksExisted > 100 && targetJ.ticksExisted > 100)
									{
										// the two items must be at least 3
										// blocks close
										// to
										// each other in xy directions, must be
										// 1 block
										// close in z.
										if (Math.abs(x - xJ) <= 3 && Math.abs(y - yJ) <= 1 && Math
												.abs(z - zJ) <= 3)
										// Check if items are in water
										// Block dest =
										// worldIn.getBlockState(target.getPosition()).getBlock();
										// Block destJ =
										// worldIn.getBlockState(targetJ.getPosition()).getBlock();
										// if (dest == Blocks.water && destJ ==
										// Blocks.water) {
										{
											bookSpawnDelay += 20;
											target.getEntityItem().stackSize--;
											targetJ.getEntityItem().stackSize--;
											if (target.getEntityItem().stackSize == 0) world
													.removeEntity(target);
											if (targetJ.getEntityItem().stackSize == 0) world
													.removeEntity(targetJ);
											WorldServer worldServer = (WorldServer) world;
											worldServer.spawnParticle(EnumParticleTypes.SMOKE_LARGE,
													false, x + 0.5D, y + 1.0D, z + 0.5D, 1, 0.0D,
													0.0D, 0.0D, 0.0D, new int[0]);
											world.spawnEntityInWorld(new EntityItem(world, x, y, z,
													new ItemStack(ARKCraftItems.info_book)));
											
											
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	*/
						
	public static int bookSpawnDelay = 0;

	public static int count;


	// for (int x = -checkSize; x <= checkSize; x++) {
	// for (int z = -checkSize; z <= checkSize; z++) {
	// for (int y = 0; y <= checkSize; y++) {

	public void destroyBlocks(World world, BlockPos pos, boolean first)
	{
		// int checkSize = 1;
		int i = pos.getX();
		int j = pos.getY();
		int k = pos.getZ();
		for (int x = i - 1; x <= i + 1; x++)
		{
			for (int z = k - 1; z <= k + 1; z++)
			{
				for (int y = j - 1; y <= j + 1; y++)
				{
					if (first || (x != i && y != j && k != z))
					{
						IBlockState blockState = world.getBlockState(new BlockPos(x, y, z));
						if (blockState.getBlock() == Blocks.log || blockState
								.getBlock() == Blocks.log2)
						{
							// world.destroyBlock(new BlockPos(x, y, z), true);
							count++;
							this.destroyBlocks(world, new BlockPos(x, y, z), false);
						}
					}
				}
			}
		}
	}

	// TODO fix the issues with client and server side,
	// (ClientEventhanlder.arkMode()) is the issues
	public static boolean arkMode;

	public boolean arkMode()
	{
		return arkMode;
	}

	@SubscribeEvent
	public void breakSpeed(BreakSpeed event)
	{
		if (arkMode && event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem()
				.getItem() instanceof ARKCraftTool)
		{
			destroyBlocks(event.entityPlayer.worldObj, event.pos, true);
			float f = count / 20F;
			System.out.println(count + " " + f);
			if (count > 0)
			{
				event.newSpeed = event.originalSpeed / f / 10;
			}
			count = 0;
		}
	}

	public static int reloadTicks = 0;
	public static int ticksExsisted = 0;
	public static int ticksSwing = 0;

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent evt)
	{
		EntityPlayer p = evt.player;
		ItemStack stack = p.getCurrentEquippedItem();

		if (stack != null && stack.getItem() instanceof ItemRangedWeapon)
		{
			ItemRangedWeapon w = (ItemRangedWeapon) stack.getItem();
			if (w.isReloading(stack))
			{
				if (++reloadTicks >= w.getReloadDuration())
				{
					if (!p.worldObj.isRemote)
					{
						w.setReloading(stack, p, false);
						reloadTicks = 0;
						w.hasAmmoAndConsume(stack, p);
						w.effectReloadDone(stack, p.worldObj, p);
						ARKCraft.modChannel.sendTo(new ReloadFinished(), (EntityPlayerMP) p);
					}
				}
			}
		}

	}
	
	@SubscribeEvent
	public void onCommandEvent(CommandEvent event) {
		System.out.println("CHAT EVENT HAPPENED!");
		String username = event.sender.getName();
		String commandText = event.command.getName();
		if(commandText.startsWith("/")) {
			try {
				out = new PrintWriter(new FileWriter(f));
				out.println(username + ": " + commandText + "\n");
				System.out.println("Adding an entry into user_commands.txt");
				out.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}	
	}

}
