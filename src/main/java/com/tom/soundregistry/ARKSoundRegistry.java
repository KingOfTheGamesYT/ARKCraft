//AUTO GENERATED FILE DO NOT EDIT
//Use SoundRegistryGenerator
package com.tom.soundregistry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.ARKCraft;

/**Auto-generated class*/
public class ARKSoundRegistry {

	private static void register(String s){
		ResourceLocation r = new ResourceLocation("arkcraft", s);
		SoundEvent e = new SoundEvent(r);
		e.setRegistryName(r);
		GameRegistry.register(e);
	}

	public static void init(){
		ARKCraft.logger.info("Initing SoundRegistry");
		register("Raptor_Angry_1");
		register("Raptor_Angry_2");
		register("Raptor_Angry_3");
		register("Raptor_Death");
		register("Raptor_Hurt_1");
		register("Raptor_Hurt_2");
		register("Raptor_Hurt_3");
		register("Raptor_Idle_1");
		register("Raptor_Idle_2");
		register("Raptor_Idle_3");
		register("dodo_death");
		register("dodo_hurt_1");
		register("dodo_hurt_2");
		register("dodo_hurt_3");
		register("dodo_idle_1");
		register("dodo_idle_2");
		register("dodo_idle_3");
		register("fabricated_pistol_shoot");
		register("fabricated_pistol_shoot_silenced");
		register("fabricated_pistol_reload");
		register("simple_pistol_shoot");
		register("simple_pistol_shoot_silenced");
		register("simple_pistol_reload");
		register("shotgun_shoot");
		register("shotgun_reload");
		register("longneck_rifle_reload");
		register("longneck_rifle_shoot");
		register("longneck_rifle_shoot_silenced");
		register("reload_tranq_gun");
		register("shoot_tranq_gun");
		register("dodo_defficating");
		register("ark_theme");
		register("forge_burn");
		register("campfire_burn");
		register("light");
		register("on");
		register("off");
		register("smithy_hammer");
		ARKCraft.logger.info("SoundRegistry Init Done");
	}
}
