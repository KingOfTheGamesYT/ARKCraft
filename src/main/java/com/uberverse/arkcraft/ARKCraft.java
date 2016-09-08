package com.uberverse.arkcraft;

import org.apache.logging.log4j.Logger;

import com.uberverse.arkcraft.common.creativetabs.ARKBlueprintTab;
import com.uberverse.arkcraft.common.creativetabs.ARKCreativeTab;
import com.uberverse.arkcraft.common.handlers.GuiHandler;
import com.uberverse.arkcraft.common.proxy.CommonProxy;
import com.uberverse.arkcraft.init.ARKCraftBlocks;
import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.arkcraft.init.ARKCraftRangedWeapons;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.ForgeVersion.CheckResult;
import net.minecraftforge.common.ForgeVersion.Status;
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

	public static CreativeTabs tabARK = new ARKCreativeTab();
	public static CreativeTabs tabARKBlueprints = new ARKBlueprintTab();
	public static SimpleNetworkWrapper modChannel;
	public static Logger logger;
	public final static EventBus EVENT_BUS = new EventBus();
	public static CheckResult versionCheckResult;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit(event);
		logger = new com.uberverse.lib.Logger(event.getModLog());
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init(event);
		updateCheckResult();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit(event);
		updateCheckResult();
	}

	private static ModContainer modContainer = Loader.instance().activeModContainer();

	public static void updateCheckResult()
	{
		if (versionCheckResult == null)
		{
			CheckResult r = ForgeVersion.getResult(modContainer);
			if (r != null && r.status != Status.PENDING) versionCheckResult = r;
		}
	}

	public static ARKCraft instance()
	{
		return instance;
	}

	// TODO fix this (not working)
	public boolean isDebugger()
	{
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
