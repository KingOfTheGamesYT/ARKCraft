package com.uberverse.arkcraft;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import org.apache.logging.log4j.Logger;

import com.uberverse.arkcraft.common.event.CommonEventHandler;
import com.uberverse.arkcraft.common.network.OpenAttachmentInventory;
import com.uberverse.arkcraft.common.network.ReloadFinished;
import com.uberverse.arkcraft.common.network.ReloadStarted;
import com.uberverse.arkcraft.common.proxy.CommonProxy;
import com.uberverse.arkcraft.init.ARKCraftBlocks;
import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.arkcraft.init.ARKCraftWeapons;

@Mod(modid = ARKCraft.MODID, name = ARKCraft.NAME, version = ARKCraft.VERSION)
public class ARKCraft
{
	public static final String MODID = "arkcraft", VERSION = "${version}",
			NAME = "ARKCraft";
	
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
	//	CoreConfig.init(event.getModConfigurationDirectory());
	//	FMLCommonHandler.instance().bus().register(new CoreConfig());
	//	ModuleItemConfig.init(event.getModConfigurationDirectory());
	//	FMLCommonHandler.instance().bus().register(new ModuleItemConfig());
		
		tabARK = new CreativeTabs(CreativeTabs.getNextID(), "tabARK")
		{
			@Override
			public Item getTabIconItem()
			{
				// TODO Assign proper item as icon
				return ARKCraftItems.azul;
			}
		};
	//	proxy.preInit();
		ARKCraftBlocks.init();
		ARKCraftItems.init();
		ARKCraftWeapons.init();
		
		setupNetwork();	
		modLog = event.getModLog();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		/*
		CoreCommonEventHandler coreEventHandler = new CoreCommonEventHandler();
		MinecraftForge.EVENT_BUS.register(coreEventHandler);
		FMLCommonHandler.instance().bus().register(coreEventHandler); */

		CommonEventHandler.init();

		proxy.registerRenderers();
		proxy.registerWeapons();
		proxy.registerEventHandlers();
		proxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
	
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
		ATTACHMENT_GUI(11);
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
		// id, side the packet is received on
		modChannel.registerMessage(OpenAttachmentInventory.Handler.class,
				OpenAttachmentInventory.class, id++, Side.SERVER);
		modChannel.registerMessage(ReloadStarted.Handler.class,
				ReloadStarted.class, id++, Side.SERVER);
		modChannel.registerMessage(ReloadFinished.Handler.class,
				ReloadFinished.class, id++, Side.CLIENT);
	}
	
	public boolean isDebugger()
	{
		return "${version}".equals("${" + "version" + "}");
	}

}
