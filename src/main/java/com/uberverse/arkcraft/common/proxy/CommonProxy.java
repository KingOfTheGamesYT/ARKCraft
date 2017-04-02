package com.uberverse.arkcraft.common.proxy;

import net.minecraft.entity.player.EntityPlayer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.client.achievement.ARKCraftAchievements;
import com.uberverse.arkcraft.client.book.proxy.BookCommon;
import com.uberverse.arkcraft.common.arkplayer.ARKPlayer;
import com.uberverse.arkcraft.common.arkplayer.event.PlayerCommonEventHandler;
import com.uberverse.arkcraft.common.arkplayer.network.ARKPlayerUpdate;
import com.uberverse.arkcraft.common.arkplayer.network.ARKPlayerUpdateRequest;
import com.uberverse.arkcraft.common.arkplayer.network.PlayerEngramCrafterUpdate;
import com.uberverse.arkcraft.common.burner.BurnerManager;
import com.uberverse.arkcraft.common.config.CoreConfig;
import com.uberverse.arkcraft.common.config.ModuleItemConfig;
import com.uberverse.arkcraft.common.config.WeightsConfig;
import com.uberverse.arkcraft.common.engram.EngramManager;
import com.uberverse.arkcraft.common.event.CommonEventHandler;
import com.uberverse.arkcraft.common.event.VersionDetectionHandler;
import com.uberverse.arkcraft.common.handlers.GuiHandler;
import com.uberverse.arkcraft.common.item.IMeshedItem;
import com.uberverse.arkcraft.common.network.ARKModeToggle;
import com.uberverse.arkcraft.common.network.BurnerToggle;
import com.uberverse.arkcraft.common.network.MessageHover;
import com.uberverse.arkcraft.common.network.MessageHover.MessageHoverReq;
import com.uberverse.arkcraft.common.network.ReloadFinished;
import com.uberverse.arkcraft.common.network.ReloadStarted;
import com.uberverse.arkcraft.common.network.ScrollGui;
import com.uberverse.arkcraft.common.network.UpdateEngrams;
import com.uberverse.arkcraft.common.network.gui.OpenAttachmentInventory;
import com.uberverse.arkcraft.common.network.gui.OpenPlayerCrafting;
import com.uberverse.arkcraft.common.network.player.PlayerPoop;
import com.uberverse.arkcraft.init.ARKCraftBlocks;
import com.uberverse.arkcraft.init.ARKCraftEntities;
import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.arkcraft.init.ARKCraftRangedWeapons;
import com.uberverse.arkcraft.init.ARKCraftWorldGen;

public abstract class CommonProxy
{
	public enum GUI
	{
		SMITHY,
		MORTAR_AND_PESTLE,
		BOOK,
		CROP_PLOT,
		TAMING,
		COMPOST_BIN,
		SCOPE,
		PLAYER,
		TAMED_DINO,
		REFINING_FORGE,
		ATTACHMENTS,
		ENGRAMS,
		CAMPFIRE,
		INV_DODO,
		FABRICATOR;

		public final int id;

		GUI()
		{
			this.id = getNextId();
		}

		static int idCounter = 0;

		private static int getNextId()
		{
			return idCounter++;
		}
	}

	@SidedProxy(clientSide = "com.uberverse.arkcraft.client.book.proxy.BookClient",
			serverSide = "com.uberverse.arkcraft.client.book.proxy.BookCommon")
	public static BookCommon dossierProxy;

	public CommonProxy()
	{}

	public void preInit(FMLPreInitializationEvent event)
	{
		setupNetwork(event);
		registerEventHandlers();
		initializeConfiguration(event);
		ARKCraftWorldGen.init();

		WeightsConfig.init(event.getModConfigurationDirectory());

		ARKCraftBlocks.init();

		ARKCraftItems.init();
		ARKCraftRangedWeapons.init();
		ARKCraftAchievements.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(ARKCraft.instance(), new GuiHandler());

		EngramManager.init();
		ARKCraftItems.initBlueprints();
		BurnerManager.init();
	}

	public void init(FMLInitializationEvent event)
	{
		ARKCraftEntities.init();
		ARKPlayer.init();
	}

	public void postInit(FMLPostInitializationEvent event)
	{}

	protected void registerEventHandlers()
	{
		CommonEventHandler.init();
		PlayerCommonEventHandler.init();
		VersionDetectionHandler.init();
	}

	private final void initializeConfiguration(FMLPreInitializationEvent event)
	{
		CoreConfig.init(event.getModConfigurationDirectory());
		MinecraftForge.EVENT_BUS.register(new CoreConfig());
		ModuleItemConfig.init(event.getModConfigurationDirectory());
		MinecraftForge.EVENT_BUS.register(new ModuleItemConfig());
	}

	private final void setupNetwork(FMLPreInitializationEvent event)
	{
		SimpleNetworkWrapper modChannel = NetworkRegistry.INSTANCE.newSimpleChannel(ARKCraft.MODID);
		ARKCraft.modChannel = modChannel;

		int id = 0;
		// The handler (usually in the packet class), the packet class, unique
		// identifier, side to be handled
		modChannel.registerMessage(OpenPlayerCrafting.Handler.class, OpenPlayerCrafting.class, id++, Side.SERVER);
		modChannel.registerMessage(PlayerPoop.Handler.class, PlayerPoop.class, id++, Side.SERVER);
		modChannel.registerMessage(OpenAttachmentInventory.Handler.class, OpenAttachmentInventory.class, id++,
				Side.SERVER);
		modChannel.registerMessage(ReloadStarted.Handler.class, ReloadStarted.class, id++, Side.SERVER);
		modChannel.registerMessage(ReloadFinished.Handler.class, ReloadFinished.class, id++, Side.CLIENT);
		modChannel.registerMessage(MessageHover.class, MessageHover.class, id++, Side.CLIENT);
		modChannel.registerMessage(MessageHoverReq.class, MessageHoverReq.class, id++, Side.SERVER);
		modChannel.registerMessage(BurnerToggle.Handler.class, BurnerToggle.class, id++, Side.SERVER);
		modChannel.registerMessage(UpdateEngrams.Handler.class, UpdateEngrams.class, id++, Side.CLIENT);
		modChannel.registerMessage(ARKPlayerUpdateRequest.Handler.class, ARKPlayerUpdateRequest.class, id++,
				Side.SERVER);
		modChannel.registerMessage(ARKPlayerUpdate.Handler.class, ARKPlayerUpdate.class, id++, Side.CLIENT);
		modChannel.registerMessage(PlayerEngramCrafterUpdate.Handler.class, PlayerEngramCrafterUpdate.class, id++,
				Side.CLIENT);
		modChannel.registerMessage(ScrollGui.Handler.class, ScrollGui.class, id++, Side.SERVER);
		modChannel.registerMessage(ARKModeToggle.Handler.class, ARKModeToggle.class, id++, Side.SERVER);
	}

	public EntityPlayer getPlayer()
	{
		return null;
	}

	public abstract EntityPlayer getPlayerFromContext(MessageContext ctx);

	public abstract long getTime();

	public abstract long getWorldTime();

	public void registerModelMeshDef(IMeshedItem i){}
}
