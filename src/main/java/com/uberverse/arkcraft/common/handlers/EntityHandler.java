package com.uberverse.arkcraft.common.handlers;

import java.util.Random;

import com.uberverse.arkcraft.ARKCraft;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class EntityHandler
{
    static int entityID = 50;

    static int idCount = 0;

    public EntityHandler()
    {
    }

    public static void registerMonster(Class eClass, String name, Biome... biomes)
    {
        Random rand = new Random(name.hashCode());
        int mainColor = rand.nextInt() * 16777215;
        int secondColor = rand.nextInt() * 16777215;

        EntityRegistry.registerModEntity(eClass, name, idCount++, ARKCraft.instance(), 10, 1, false, mainColor, secondColor);
        EntityRegistry.addSpawn(eClass, 25, 2, 4, EnumCreatureType.CREATURE, biomes);
    }

    public static void registerMonster(Class eClass, String name)
    {
        registerMonster(eClass, name, Biomes.BEACH, Biomes.DESERT, Biomes.FOREST, Biomes.BIRCH_FOREST, Biomes.EXTREME_HILLS);
    }

    // public static void registerPassive(Class eClass, String name) {
    // int entityID = EntityRegistry.findGlobalUniqueEntityId();
    // Random rand = new Random(name.hashCode());
    // int mainColor = rand.nextInt() * 16777215;
    // int secondColor = rand.nextInt() * 16777215;
    //
    // EntityRegistry.registerGlobalEntityID(eClass, name, entityID);
    // EntityRegistry.addSpawn(eClass, 15, 2, 4, EnumCreatureType.CREATURE,
    // BiomeGenBase.plains, BiomeGenBase.savanna,
    // BiomeGenBase.beach, BiomeGenBase.desert, BiomeGenBase.extremeHills,
    // BiomeGenBase.coldBeach, BiomeGenBase.jungleEdge,
    // BiomeGenBase.jungle, BiomeGenBase.plains, BiomeGenBase.swampland);
    // EntityRegistry.registerModEntity(eClass, name, entityID,
    // ARKCraft.instance(), 64, 10, true);
    // EntityList.entityEggs.put(Integer.valueOf(entityID), new
    // EntityList.EntityEggInfo(entityID, mainColor, secondColor));
    // }

    public static void registerModEntity(Class<? extends Entity> eClass, String name, Object mod, int trackRange, int updateFreq, boolean sVU)
    {
        EntityRegistry.registerModEntity(eClass, name, ++entityID, mod, trackRange, updateFreq, sVU);
    }
}
