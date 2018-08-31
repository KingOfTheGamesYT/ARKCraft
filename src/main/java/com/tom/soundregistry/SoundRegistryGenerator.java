package com.tom.soundregistry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;

/**
 * @author tom5454
 * */
public class SoundRegistryGenerator {

	@SuppressWarnings("unchecked")//Run this when changing sounds.json
	public static void main(String[] args) {
		System.out.println("Loading");
		Gson gson = new Gson();
		try{
			System.out.println("Reading sounds.json");
			Map<String, Object> sounds = (Map<String, Object>) gson.fromJson(new BufferedReader(new FileReader(new File(".", "src" + File.separator + "main" + File.separator + "resources" + File.separator + "assets" + File.separator + "arkcraft" + File.separator + "sounds.json"))), Object.class);
			PrintWriter w = new PrintWriter(new File(".", "src" + File.separator + "main" + File.separator + "java" + File.separator + "com" + File.separator + "tom" + File.separator + "soundregistry" + File.separator + "ARKSoundRegistry.java"));
			System.out.println("Printing output");
			w.println("//AUTO GENERATED FILE DO NOT EDIT");
			w.println("//Use SoundRegistryGenerator");
			w.println("package com.tom.soundregistry;");
			w.println();
			w.println("import net.minecraft.util.ResourceLocation;");
			w.println("import net.minecraft.util.SoundEvent;");
			w.println();
			w.println("import net.minecraftforge.fml.common.registry.GameRegistry;");
			w.println();
			w.println("import ARKCraft;");
			w.println();
			w.println("/**Auto-generated class*/");
			w.println("public class ARKSoundRegistry {");
			w.println();
			w.println("	private static void register(String s){");
			w.println("		ResourceLocation r = new ResourceLocation(\"arkcraft\", s);");
			w.println("		SoundEvent e = new SoundEvent(r);");
			w.println("		e.setRegistryName(r);");
			w.println("		GameRegistry.register(e);");
			w.println("	}");
			w.println();
			w.println("	public static void init(){");
			w.println("		ARKCraft.logger.info(\"Initing SoundRegistry\");");
			for(Entry<String, Object> e : sounds.entrySet()){
				w.print("		register(\"");
				w.print(e.getKey());
				w.println("\");");
			}
			w.println("		ARKCraft.logger.info(\"SoundRegistry Init Done\");");
			w.println("	}");
			w.println("}");
			w.close();
		}catch(Throwable e){
			System.err.println("An exception occurred while generating SoundRegistry");
			e.printStackTrace();
		}
	}

}
