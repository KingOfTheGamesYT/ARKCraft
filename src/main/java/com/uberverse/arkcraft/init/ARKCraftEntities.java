package com.uberverse.arkcraft.init;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.entity.EntityDodo;
import com.uberverse.arkcraft.common.handlers.EntityHandler;

public class ARKCraftEntities
{
    public static void init()
    {
        EntityHandler.registerMonster(EntityDodo.class, ARKCraft.MODID + ".dodo");
    }
}
