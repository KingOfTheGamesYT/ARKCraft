package com.uberverse.arkcraft;

import org.apache.logging.log4j.Logger;

import com.uberverse.arkcraft.common.arkplayer.network.ARKPlayerUpdate;
import com.uberverse.arkcraft.common.arkplayer.network.ARKPlayerUpdateRequest;
import com.uberverse.arkcraft.common.arkplayer.network.PlayerEngramCrafterUpdate;
import com.uberverse.arkcraft.common.config.CoreConfig;
import com.uberverse.arkcraft.common.config.ModuleItemConfig;
import com.uberverse.arkcraft.common.config.WeightsConfig;
import com.uberverse.arkcraft.common.event.CommonEventHandler;
import com.uberverse.arkcraft.common.gen.WorldGeneratorBushes;
import com.uberverse.arkcraft.common.handlers.GuiHandler;
import com.uberverse.arkcraft.common.handlers.recipes.CampfireCraftingManager;
import com.uberverse.arkcraft.common.handlers.recipes.ForgeCraftingHandler;
import com.uberverse.arkcraft.common.handlers.recipes.PestleCraftingManager;
import com.uberverse.arkcraft.common.handlers.recipes.PlayerCraftingManager;
import com.uberverse.arkcraft.common.handlers.recipes.RecipeHandler;
import com.uberverse.arkcraft.common.handlers.recipes.SmithyCraftingManager;
import com.uberverse.arkcraft.common.item.engram.ARKCraftEngrams;
import com.uberverse.arkcraft.common.network.BurnerToggle;
import com.uberverse.arkcraft.common.network.DescriptionHandler;
import com.uberverse.arkcraft.common.network.MessageHover;
import com.uberverse.arkcraft.common.network.MessageHover.MessageHoverReq;
import com.uberverse.arkcraft.common.network.ReloadFinished;
import com.uberverse.arkcraft.common.network.ReloadStarted;
import com.uberverse.arkcraft.common.network.ScrollingMessage;
import com.uberverse.arkcraft.common.network.SyncPlayerData;
import com.uberverse.arkcraft.common.network.UpdateEngrams;
import com.uberverse.arkcraft.common.network.UpdateMPToCraftItem;
import com.uberverse.arkcraft.common.network.UpdatePlayerCrafting;
import com.uberverse.arkcraft.common.network.UpdateSmithyToCraftItem;
import com.uberverse.arkcraft.common.network.gui.OpenAttachmentInventory;
import com.uberverse.arkcraft.common.network.gui.OpenPlayerCrafting;
import com.uberverse.arkcraft.common.network.player.PlayerPoop;
import com.uberverse.arkcraft.common.proxy.CommonProxy;
import com.uberverse.arkcraft.init.ARKCraftBlocks;
import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.arkcraft.init.ARKCraftRangedWeapons;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.ForgeVersion.CheckResult;
import net.minecraftforge.common.ForgeVersion.Status;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@SuppressWarnings("rawtypes")
@Mod(modid = ARKCraft.MODID, name = ARKCraft.NAME, updateJSON = ARKCraft.UPDATE_JSON, useMetadata = true)
public class ARKCraft
{
	public static final String MODID = "arkcraft", NAME = "ARKCraft";

	public static final String descriptionPacketChannel = MODID + ":descPacket";
	public static final String UPDATE_JSON = "https://raw.githubusercontent.com/BubbleTrouble14/ARKCraft/master/version-check.json";

	@Instance(ARKCraft.MODID)
	public static ARKCraft instance;

	@SidedProxy(clientSide = "com.uberverse.arkcraft.client.proxy.ClientProxy", serverSide = "com.uberverse.arkcraft.server.proxy.ServerProxy")
	public static CommonProxy proxy;

	public static CreativeTabs tabARK;
	public static SimpleNetworkWrapper modChannel;
	public static Logger logger;
	public static EventBus bus;
	public static CheckResult versionCheckResult;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		setupNetwork(event);

		bus = new EventBus();
		CoreConfig.init(event.getModConfigurationDirectory());
		FMLCommonHandler.instance().bus().register(new CoreConfig());
		ModuleItemConfig.init(event.getModConfigurationDirectory());
		FMLCommonHandler.instance().bus().register(new ModuleItemConfig());

		GameRegistry.registerWorldGenerator(new WorldGeneratorBushes(), 0);

		tabARK = ARKCreativeTab.INSTANCE;

		ARKCraftBlocks.init();
		ARKCraftItems.init();
		ARKCraftRangedWeapons.init();
		ARKCraftEngrams.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(ARKCraft.instance, new GuiHandler());

		RecipeHandler.registerVanillaCraftingRecipes();
		PestleCraftingManager.registerPestleCraftingRecipes();
		SmithyCraftingManager.registerSmithyCraftingRecipes();
		PlayerCraftingManager.registerPlayerCraftingRecipes();
		ForgeCraftingHandler.registerForgeRecipes();
		CampfireCraftingManager.registerCampfireRecipes();

		// This has to be here so it can create weights for our items and blocks
		// as well
		WeightsConfig.init(event.getModConfigurationDirectory());

		proxy.preInit();

		logger = new com.uberverse.lib.Logger(event.getModLog());
		mc = Loader.instance().activeModContainer();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{

		CommonEventHandler.init();

		proxy.registerRenderers();
		proxy.init();
		proxy.registerEventHandlers();
		updateCheckResult();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		// if (event.getSide().isClient()) System.out
		// .println(Minecraft.getMinecraft().fontRendererObj.getStringWidth("
		// "));
		updateCheckResult();
	}

	private static ModContainer mc;

	public static void updateCheckResult()
	{
		if (versionCheckResult == null)
		{
			CheckResult r = ForgeVersion.getResult(mc);
			if (r != null && r.status != Status.PENDING) versionCheckResult = r;
		}
	}

	public static ARKCraft instance()
	{
		return instance;
	}

	public enum GUI
	{
		SMITHY(0),
		PESTLE_AND_MORTAR(1),
		INV_DODO(2),
		BOOK_GUI(3),
		CROP_PLOT(4),
		TAMING_GUI(5),
		COMPOST_BIN(6),
		SCOPE(7),
		PLAYER(8),
		TAMED_DINO(9),
		FORGE_GUI(10),
		ATTACHMENT_GUI(11),
		ENGRAM_GUI(12),
		CAMPFIRE_GUI(13);
		int id;

		GUI(int id)
		{
			this.id = id;
		}

		public int getID()
		{
			return id;
		}
	}

	private void setupNetwork(FMLPreInitializationEvent event)
	{
		modChannel = NetworkRegistry.INSTANCE.newSimpleChannel(ARKCraft.MODID);

		int id = 0;
		// The handler (usually in the packet class), the packet class, unique
		modChannel.registerMessage(PlayerPoop.Handler.class, PlayerPoop.class, id++, Side.SERVER);
		modChannel.registerMessage(UpdateMPToCraftItem.Handler.class, UpdateMPToCraftItem.class, id++, Side.SERVER);
		modChannel.registerMessage(UpdateSmithyToCraftItem.Handler.class, UpdateSmithyToCraftItem.class, id++, Side.SERVER);
		modChannel.registerMessage(OpenPlayerCrafting.Handler.class, OpenPlayerCrafting.class, id++, Side.SERVER);
		modChannel.registerMessage(UpdatePlayerCrafting.Handler.class, UpdatePlayerCrafting.class, id++, Side.SERVER);
		modChannel.registerMessage(OpenAttachmentInventory.Handler.class, OpenAttachmentInventory.class, id++, Side.SERVER);
		modChannel.registerMessage(ReloadStarted.Handler.class, ReloadStarted.class, id++, Side.SERVER);
		modChannel.registerMessage(ReloadFinished.Handler.class, ReloadFinished.class, id++, Side.CLIENT);
		modChannel.registerMessage(MessageHover.class, MessageHover.class, id++, Side.CLIENT);
		modChannel.registerMessage(MessageHoverReq.class, MessageHoverReq.class, id++, Side.SERVER);
		modChannel.registerMessage(BurnerToggle.Handler.class, BurnerToggle.class, id++, Side.SERVER);
		modChannel.registerMessage(SyncPlayerData.Handler.class, SyncPlayerData.class, id++, Side.CLIENT);
		modChannel.registerMessage(UpdateEngrams.Handler.class, UpdateEngrams.class, id++, Side.CLIENT);
		modChannel.registerMessage(ARKPlayerUpdateRequest.Handler.class, ARKPlayerUpdateRequest.class, id++, Side.SERVER);
		modChannel.registerMessage(ARKPlayerUpdate.Handler.class, ARKPlayerUpdate.class, id++, Side.CLIENT);
		modChannel.registerMessage(PlayerEngramCrafterUpdate.Handler.class, PlayerEngramCrafterUpdate.class, id++, Side.CLIENT);
		modChannel.registerMessage(ScrollingMessage.Handler.class, ScrollingMessage.class, id++, Side.SERVER);
		DescriptionHandler.init();
	}

	public boolean isDebugger()
	{
		System.out.println(version());
		return "".equals(version());
	}

	public String version()
	{
		return this.getClass().getAnnotation(Mod.class).version();
	}

	public String modid()
	{
		return this.getClass().getAnnotation(Mod.class).modid();
	}

	public String name()
	{
		return this.getClass().getAnnotation(Mod.class).name();
	}
}
