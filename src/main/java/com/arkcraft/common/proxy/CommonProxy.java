package com.arkcraft.common.proxy;

import com.arkcraft.common.advancement.ARKCraftAdvancementTriggers;
import com.arkcraft.common.item.IMeshedItem;
import com.arkcraft.ARKCraft;
import com.arkcraft.client.book.proxy.BookCommon;
import com.arkcraft.common.arkplayer.ARKPlayer;
import com.arkcraft.common.arkplayer.event.PlayerCommonEventHandler;
import com.arkcraft.common.arkplayer.network.ARKPlayerUpdate;
import com.arkcraft.common.arkplayer.network.ARKPlayerUpdateRequest;
import com.arkcraft.common.arkplayer.network.PlayerEngramCrafterUpdate;
import com.arkcraft.common.burner.BurnerManager;
import com.arkcraft.common.config.CoreConfig;
import com.arkcraft.common.config.ModuleItemConfig;
import com.arkcraft.common.config.WeightsConfig;
import com.arkcraft.common.engram.EngramManager;
import com.arkcraft.common.event.CommonEventHandler;
import com.arkcraft.common.event.VersionDetectionHandler;
import com.arkcraft.common.handlers.GuiHandler;
import com.arkcraft.common.network.ARKModeToggle;
import com.arkcraft.common.network.BurnerToggle;
import com.arkcraft.common.network.GunFired;
import com.arkcraft.common.network.MessageHover;
import com.arkcraft.common.network.MessageHover.MessageHoverReq;
import com.arkcraft.common.network.ReloadFinished;
import com.arkcraft.common.network.ReloadStarted;
import com.arkcraft.common.network.ScrollGui;
import com.arkcraft.common.network.UpdateEngrams;
import com.arkcraft.common.network.gui.OpenAttachmentInventory;
import com.arkcraft.common.network.gui.OpenPlayerCrafting;
import com.arkcraft.common.network.player.PlayerPoop;
import com.arkcraft.init.ARKCraftBlocks;
import com.arkcraft.init.ARKCraftEntities;
import com.arkcraft.init.ARKCraftItems;
import com.arkcraft.init.ARKCraftRangedWeapons;
import com.arkcraft.init.ARKCraftWorldGen;

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

	@SidedProxy(clientSide = "BookClient",
			serverSide = "BookCommon")
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
		ARKCraftAdvancementTriggers.init();
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
		modChannel.registerMessage(GunFired.Handler.class, GunFired.class, id++, Side.SERVER);
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
