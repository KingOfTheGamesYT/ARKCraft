package com.arkcraft.server.event;

import com.arkcraft.ARKCraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
@Mod.EventBusSubscriber(modid = ARKCraft.MODID, value = Side.SERVER)
public class ServerEventHandler {

}
