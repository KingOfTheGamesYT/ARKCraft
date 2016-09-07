package com.uberverse.arkcraft.common.event;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
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
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.arkplayer.ARKPlayer;
import com.uberverse.arkcraft.common.config.ModuleItemBalance;
import com.uberverse.arkcraft.common.item.firearms.ItemRangedWeapon;
import com.uberverse.arkcraft.common.item.tools.ARKCraftTool;
import com.uberverse.arkcraft.common.network.ReloadFinished;
import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.lib.LogHelper;
import com.uberverse.lib.Utils;

import com.google.common.collect.ImmutableSet;

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
	}

	@SubscribeEvent
	public void onEntityConstructing(EntityEvent.EntityConstructing event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			ARKPlayer.register((EntityPlayer) event.entity);
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
				ARKPlayer.get(player).feelTheUrge();
			}
		}
	}

	private static final Set<Item> INPUTS = ImmutableSet.of(Items.bone, Items.book, Items.wheat);

	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event)
	{
		if (event.side.isServer())
		{
			// LogHelper.info("[OnWorldTick] The Side is on the server!");
			World world = event.world;

			EntityItem itemToSpawn = null;
			if (!world.isRemote)
			{
				if (bookSpawnDelay > 0) bookSpawnDelay--;
				else
				{
					// LogHelper.info("The world is not remote.");
					List<Entity> entitiesInWorld = world.loadedEntityList;
					for (Entity entityInWorld : entitiesInWorld)
					{
						final Set<Item> remainingInputs = new HashSet<Item>(INPUTS); // Create
						// a
						// mutable
						// copy
						// of
						// the
						// input
						// set
						// to
						// track
						// which
						// items
						// have
						// been
						// found
						ArrayList<EntityItem> foundEntityItems = new ArrayList<EntityItem>();
						// LogHelper.info("Found an Entity in the world!");
						if (entityInWorld instanceof EntityItem)
						{
							EntityItem entityItemInWorld = (EntityItem) entityInWorld;
							if (entityItemInWorld.getEntityItem().getItem() == Items.book)
							{
								LogHelper.info("Found an Entity in the world that is a book!");
								remainingInputs.remove(Items.book);
								foundEntityItems.add(entityItemInWorld);
								AxisAlignedBB areaBound = entityItemInWorld.getEntityBoundingBox().expand(3, 3, 3);
								List<Entity> entitiesWithinBound = world.getEntitiesWithinAABBExcludingEntity(entityItemInWorld, areaBound);
								for (Entity entityWithinBound : entitiesWithinBound)
								{
									if (entityWithinBound instanceof EntityItem)
									{
										EntityItem entityItemWithinBound = (EntityItem) entityWithinBound;
										if (entityItemWithinBound.getEntityItem().getItem() == Items.bone)
										{
											LogHelper.info("Found an Entity near the book that is a bone!");
											remainingInputs.remove(Items.bone);
											if (!remainingInputs.contains(entityItemWithinBound)) foundEntityItems.add(entityItemWithinBound);
										}
										else if (entityItemWithinBound.getEntityItem().getItem() == Items.wheat)
										{
											LogHelper.info("Found an Entity near the book that is wheat!");
											remainingInputs.remove(Items.wheat);
											if (!remainingInputs.contains(entityItemWithinBound)) foundEntityItems.add(entityItemWithinBound);
										}
										if (remainingInputs.isEmpty())
										{
											LogHelper.info("All items have been found. The Items hashmap is empty.");
											for (EntityItem foundEntityItem : foundEntityItems)
											{
												Random random = new Random();
												bookSpawnDelay += 100 + random.nextInt(10);
												foundEntityItem.getEntityItem().stackSize--;
												if (foundEntityItem.getEntityItem().stackSize <= 0)
												{
													LogHelper.info("Deleting the Item: " + foundEntityItem.getEntityItem().getItem().toString());
													foundEntityItem.setDead();
												}
												itemToSpawn = new EntityItem(world, entityItemInWorld.posX, entityItemInWorld.posY,
														entityItemInWorld.posZ, new ItemStack(ARKCraftItems.info_book, 1));

											}
										}

									}
								}

							}
						}
						foundEntityItems.clear();
					}
					if (itemToSpawn != null)
					{
						((WorldServer) world).spawnParticle(EnumParticleTypes.SMOKE_LARGE, false, itemToSpawn.posX, itemToSpawn.posY + 0.5D,
								itemToSpawn.posZ, 5, 0.0D, 0.0D, 0.0D, 0.0D, new int[0]);
						world.spawnEntityInWorld(itemToSpawn);
					}

				}
			}
		}
	}

	public static int bookSpawnDelay = 0;

	public static int count;

	// for (int x = -checkSize; x <= checkSize; x++) {
	// for (int z = -checkSize; z <= checkSize; z++) {
	// for (int y = 0; y <= checkSize; y++) {

	private void destroyBlocks(World world, BlockPos pos)
	{
		// TODO Fix crash due to stackoverflow
		// int checkSize = 1;

		Collection<BlockPos> done = new HashSet<>();
		Queue<BlockPos> queue = new LinkedList<>();

		queue.add(pos);

		while (!queue.isEmpty())
		{
			pos = queue.remove();
			int i = pos.getX();
			int j = pos.getY();
			int k = pos.getZ();

			// world.destroyBlock(pos, true);

			for (int x = i - 1; x <= i + 1; x++)
			{
				for (int z = k - 1; z <= k + 1; z++)
				{
					for (int y = j - 1; y <= j + 1; y++)
					{
						if (x != i && y != j && k != z)
						{

							BlockPos n = new BlockPos(x, y, z);
							IBlockState blockState = world.getBlockState(new BlockPos(x, y, z));
							if (blockState.getBlock() == Blocks.log || blockState.getBlock() == Blocks.log2)
							{
								if (!done.contains(n)) queue.add(n);
							}
						}
					}
				}
			}
			done.add(pos);
		}
	}

	// TODO fix the issues with client and server side,
	// (ClientEventhandler.arkMode()) is the issues
	// --> basically keep arkmode as a player variable
	public static boolean arkMode;

	public boolean arkMode()
	{
		return arkMode;
	}

	@SubscribeEvent
	public void breakSpeed(BreakSpeed event)
	{
		if (arkMode && event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem().getItem() instanceof ARKCraftTool)
		{
			destroyBlocks(event.entityPlayer.worldObj, event.pos);
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
	private int ticks = 0;

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
		if(!p.worldObj.isRemote && evt.phase == Phase.START){
			if(ticks > 19){
				ticks = 0;
				Utils.checkInventoryForDecayable(p.inventory);
			}else{
				ticks++;
			}
		}
	}

}
