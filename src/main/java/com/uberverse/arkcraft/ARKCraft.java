package com.uberverse.arkcraft;

import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import com.uberverse.arkcraft.common.config.CoreConfig;
import com.uberverse.arkcraft.common.config.ModuleItemConfig;
import com.uberverse.arkcraft.common.config.WeightsConfig;
import com.uberverse.arkcraft.common.event.CommonEventHandler;
import com.uberverse.arkcraft.common.gen.WorldGeneratorBushes;
import com.uberverse.arkcraft.common.handlers.GuiHandler;
import com.uberverse.arkcraft.common.handlers.recipes.ForgeCraftingHandler;
import com.uberverse.arkcraft.common.handlers.recipes.PestleCraftingManager;
import com.uberverse.arkcraft.common.handlers.recipes.PlayerCraftingManager;
import com.uberverse.arkcraft.common.handlers.recipes.RecipeHandler;
import com.uberverse.arkcraft.common.handlers.recipes.SmithyCraftingManager;
import com.uberverse.arkcraft.common.network.DescriptionHandler;
import com.uberverse.arkcraft.common.network.MessageHover;
import com.uberverse.arkcraft.common.network.MessageHover.MessageHoverReq;
import com.uberverse.arkcraft.common.network.OpenAttachmentInventory;
import com.uberverse.arkcraft.common.network.OpenPlayerCrafting;
import com.uberverse.arkcraft.common.network.PlayerPoop;
import com.uberverse.arkcraft.common.network.ReloadFinished;
import com.uberverse.arkcraft.common.network.ReloadStarted;
import com.uberverse.arkcraft.common.network.ScrollingMessage;
import com.uberverse.arkcraft.common.network.UpdateMPToCraftItem;
import com.uberverse.arkcraft.common.network.UpdatePlayerCrafting;
import com.uberverse.arkcraft.common.network.UpdateSmithyToCraftItem;
import com.uberverse.arkcraft.common.proxy.CommonProxy;
import com.uberverse.arkcraft.init.ARKCraftBlocks;
import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.arkcraft.init.ARKCraftRangedWeapons;

@Mod(modid = ARKCraft.MODID, name = ARKCraft.NAME, version = ARKCraft.VERSION)
public class ARKCraft
{
	public static final String MODID = "arkcraft", VERSION = "${version}",
			NAME = "ARKCraft";

	public static final String descriptionPacketChannel = MODID + ":descPacket";

	@Instance(ARKCraft.MODID)
	public static ARKCraft instance;

	@SidedProxy(clientSide = "com.uberverse.arkcraft.client.proxy.ClientProxy", serverSide = "com.uberverse.arkcraft.server.proxy.ServerProxy")
	public static CommonProxy proxy;

	public static CreativeTabs tabARK;
	public static SimpleNetworkWrapper modChannel;
	public static Logger modLog;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		CoreConfig.init(event.getModConfigurationDirectory());
		FMLCommonHandler.instance().bus().register(new CoreConfig());
		ModuleItemConfig.init(event.getModConfigurationDirectory());
		FMLCommonHandler.instance().bus().register(new ModuleItemConfig());

		GameRegistry.registerWorldGenerator(new WorldGeneratorBushes(), 0);

		tabARK = new CreativeTabs(CreativeTabs.getNextID(), "tabARK")
		{
			@Override
			public Item getTabIconItem()
			{
				// TODO Assign proper item as icon
				return ARKCraftItems.tabItem;
			}
		};

		ARKCraftBlocks.init();
		ARKCraftItems.init();
		ARKCraftRangedWeapons.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(ARKCraft.instance, new GuiHandler());

		RecipeHandler.registerVanillaCraftingRecipes();
		PestleCraftingManager.registerPestleCraftingRecipes();
		SmithyCraftingManager.registerSmithyCraftingRecipes();
		PlayerCraftingManager.registerPlayerCraftingRecipes();
		ForgeCraftingHandler.registerForgeRecipes();

		//This has to be here so it can create weights for our items and blocks as well
		WeightsConfig.init(event.getModConfigurationDirectory());

		setupNetwork();
		modLog = event.getModLog();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{

		CommonEventHandler.init();

		proxy.registerRenderers();
		proxy.init();
		proxy.registerEventHandlers();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{

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
		CAMPFIRE_GUI(12);
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

	private void setupNetwork()
	{
		modChannel = NetworkRegistry.INSTANCE.newSimpleChannel(ARKCraft.MODID);

		int id = 0;
		// The handler (usually in the packet class), the packet class, unique
		modChannel.registerMessage(PlayerPoop.Handler.class, PlayerPoop.class,
				id++, Side.SERVER);
		modChannel.registerMessage(UpdateMPToCraftItem.Handler.class,
				UpdateMPToCraftItem.class, id++, Side.SERVER);
		modChannel.registerMessage(UpdateSmithyToCraftItem.Handler.class,
				UpdateSmithyToCraftItem.class, id++, Side.SERVER);
		modChannel.registerMessage(OpenPlayerCrafting.Handler.class,
				OpenPlayerCrafting.class, id++, Side.SERVER);
		modChannel.registerMessage(UpdatePlayerCrafting.Handler.class,
				UpdatePlayerCrafting.class, id++, Side.SERVER);
		modChannel.registerMessage(OpenAttachmentInventory.Handler.class,
				OpenAttachmentInventory.class, id++, Side.SERVER);
		modChannel.registerMessage(ReloadStarted.Handler.class,
				ReloadStarted.class, id++, Side.SERVER);
		modChannel.registerMessage(ReloadFinished.Handler.class,
				ReloadFinished.class, id++, Side.CLIENT);
		modChannel.registerMessage(ScrollingMessage.Handler.class,
				ScrollingMessage.class, id++, Side.SERVER);
		modChannel.registerMessage(MessageHover.class,
				MessageHover.class, id++, Side.CLIENT);
		modChannel.registerMessage(MessageHoverReq.class,
				MessageHoverReq.class, id++, Side.SERVER);
		DescriptionHandler.init();
	}

	public boolean isDebugger()
	{
		return "${version}".equals("${" + "version" + "}");
	}

}
