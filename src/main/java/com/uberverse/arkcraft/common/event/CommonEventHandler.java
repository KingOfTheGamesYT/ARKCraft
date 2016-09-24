package com.uberverse.arkcraft.common.event;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.arkplayer.ARKPlayer;
import com.uberverse.arkcraft.common.config.ModuleItemBalance;
import com.uberverse.arkcraft.common.item.IDecayable;
import com.uberverse.arkcraft.common.item.firearms.ItemRangedWeapon;
import com.uberverse.arkcraft.common.network.ReloadFinished;
import com.uberverse.arkcraft.common.tileentity.IDecayer;
import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.arkcraft.util.Utils;
import com.uberverse.lib.LogHelper;

import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
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
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
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

	// Immutable Set (Not able to edit the set)
	private static final Set<Item> INPUTS = ImmutableSet.of(Items.bone, Items.book, Items.wheat);

	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event)
	{
		if (event.side.isServer())
		{
			World world = event.world;

			EntityItem itemToSpawn = null;
			if (!world.isRemote)
			{
				if (bookSpawnDelay > 0) bookSpawnDelay--;
				else
				{
					List<Entity> entitiesInWorld = world.loadedEntityList;
					for (Entity entityInWorld : entitiesInWorld)
					{
						// Make the set mutable each for loop.
						final Set<Item> remainingInputs = new HashSet<Item>(INPUTS); // Create
						ArrayList<EntityItem> foundEntityItems = new ArrayList<EntityItem>();
						if (entityInWorld instanceof EntityItem)
						{
							EntityItem entityItemInWorld = (EntityItem) entityInWorld;
							if (entityItemInWorld.getEntityItem().getItem() == Items.book)
							{
								LogHelper.info("Found an Entity in the world that is a book!");
								remainingInputs.remove(Items.book);
								foundEntityItems.add(entityItemInWorld);
								AxisAlignedBB areaBound = entityItemInWorld.getEntityBoundingBox().expand(3, 3, 3);
								List<Entity> entitiesWithinBound = world.getEntitiesWithinAABBExcludingEntity(
										entityItemInWorld, areaBound);
								for (Entity entityWithinBound : entitiesWithinBound)
								{
									if (entityWithinBound instanceof EntityItem)
									{
										EntityItem entityItemWithinBound = (EntityItem) entityWithinBound;
										if (entityItemWithinBound.getEntityItem().getItem() == Items.bone)
										{
											LogHelper.info("Found an Entity near the book that is a bone!");
											remainingInputs.remove(Items.bone);
											if (!remainingInputs.contains(entityItemWithinBound)) foundEntityItems.add(
													entityItemWithinBound);
										}
										else if (entityItemWithinBound.getEntityItem().getItem() == Items.wheat)
										{
											LogHelper.info("Found an Entity near the book that is wheat!");
											remainingInputs.remove(Items.wheat);
											if (!remainingInputs.contains(entityItemWithinBound)) foundEntityItems.add(
													entityItemWithinBound);
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
													LogHelper.info("Deleting the Item: " + foundEntityItem
															.getEntityItem().getItem().toString());
													foundEntityItem.setDead();
												}
												itemToSpawn = new EntityItem(world, entityItemInWorld.posX,
														entityItemInWorld.posY, entityItemInWorld.posZ, new ItemStack(
																ARKCraftItems.info_book, 1));

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
						// Spawn particle and item
						((WorldServer) world).spawnParticle(EnumParticleTypes.SMOKE_LARGE, false, itemToSpawn.posX,
								itemToSpawn.posY + 0.5D, itemToSpawn.posZ, 5, 0.0D, 0.0D, 0.0D, 0.0D, new int[0]);
						world.spawnEntityInWorld(itemToSpawn);
					}

				}

				if (event.phase == Phase.START)
				{
					TickStorage t = tick.get(event.world.provider.getDimensionId());
					if (t == null)
					{
						t = new TickStorage();
						tick.put(event.world.provider.getDimensionId(), t);
					}
					if (t.tick > 20)
					{
						t.tick = 0;
						for (int i = 0; i < event.world.loadedTileEntityList.size(); i++)
						{
							if (event.world.loadedTileEntityList.get(i) instanceof IInventory)
							{// Check
								// for
								// inventories
								// every
								// second
								Utils.checkInventoryForDecayable((IInventory) event.world.loadedTileEntityList.get(i));
							}
						}
					}
					else
					{
						t.tick++;
					}
				}
			}
		}
	}

	private Map<Integer, TickStorage> tick = new HashMap<Integer, TickStorage>();

	public static class TickStorage
	{
		private int tick;
	}

	public static int bookSpawnDelay = 0;

	public static int count;

	// for (int x = -checkSize; x <= checkSize; x++) {
	// for (int z = -checkSize; z <= checkSize; z++) {
	// for (int y = 0; y <= checkSize; y++) {

	private void destroyBlocks(World world, BlockPos pos)
	{
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
		if (!p.worldObj.isRemote && evt.phase == Phase.START)
		{
			Utils.checkInventoryForDecayable(p.inventory);
		}
	}

	@SubscribeEvent
	public void onPlayerOpenContainer(PlayerOpenContainerEvent event)
	{
		for (Object o : event.entityPlayer.openContainer.inventorySlots)
		{
			if (o != null)
			{
				Slot s = (Slot) o;
				if (s.getHasStack() && s.getStack().getItem() instanceof IDecayable) ((IDecayable) s.getStack()
						.getItem()).decayTick(s.inventory, s.getSlotIndex(), s.inventory instanceof IDecayer
								? ((IDecayer) s.inventory).getDecayModifier(s.getStack()) : 1, s.getStack());
			}
		}
	}

	@SubscribeEvent
	public void onBlockBroken(BlockEvent.HarvestDropsEvent event)
	{
		if (event.harvester != null && !event.harvester.getEntityWorld().isRemote && event.harvester
				.getHeldItem() == null && ARKPlayer.isARKMode(event.harvester) && event.state
						.getBlock() instanceof BlockLog)
		{
			ARKPlayer.get(event.harvester).addXP(0.4);
			event.drops.clear();
			if (new Random().nextDouble() < 0.5) event.drops.add(new ItemStack(ARKCraftItems.wood, 1));
			int thatch = (int) (new Random().nextDouble() * 5);
			event.drops.add(new ItemStack(ARKCraftItems.thatch, thatch));
		}
	}

	@SubscribeEvent
	public void onItemPickup(EntityItemPickupEvent event)
	{
		if (!event.entityPlayer.worldObj.isRemote)
		{
			ItemStack stack = event.item.getEntityItem();
			if (stack != null)
			{
				Item in = stack.getItem();
				if (in instanceof IDecayable)
				{
					IDecayable d = (IDecayable) in;
					EntityPlayer p = event.entityPlayer;
					for (int i = 0; i < p.inventory.getSizeInventory(); i++)
					{
						ItemStack s = p.inventory.getStackInSlot(i);
						if (s != null && s.getItem() == in)
						{
							long s2 = d.getDecayStart(s);
							int si2 = s.stackSize;
							long s1 = d.getDecayStart(stack);
							int si1 = stack.stackSize;
							si1 = in.getItemStackLimit(stack) - si2 > si1 ? si1 : in.getItemStackLimit(stack) - si2;
							stack.stackSize -= si1;

							long n = (s1 * si1 + s2 * si2) / (si1 + si2);
							ItemStack nS = new ItemStack(in, si2 + si1);
							d.setDecayStart(nS, n);
							p.inventory.setInventorySlotContents(i, nS);
							break;
						}
					}
				}
			}
		}
	}
}
