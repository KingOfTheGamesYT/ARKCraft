package com.arkcraft.common.handlers;

import java.util.ArrayList;

import com.arkcraft.common.config.CoreBalance;
import com.arkcraft.common.gen.WrappedOreGenerator;
import net.minecraft.block.Block;

import com.arkcraft.lib.LogHelper;

public class GenerationHandler
{

	public static ArrayList<WrappedOreGenerator.Instruction> oresToGenerate = new ArrayList<WrappedOreGenerator.Instruction>();
	public static WrappedOreGenerator generator;

	public GenerationHandler()
	{}

	/**
	 * The only
	 */
	public static void addOreToGen(Block block, int height)
	{
		/* This is for standard ore generation. */
		addOreToGen(block, height, CoreBalance.GEN.MAX_DEFAULT_ORE_BLOCKS_SPAWN_PER_VEIN,
				CoreBalance.GEN.MAX_DEFAULT_ORE_VEIN_SPAWN_PER_CHUNK);

	}

	public static void addOreToGen(Block block, int height, int maxBlocksInVain)
	{
		addOreToGen(block, height, maxBlocksInVain, CoreBalance.GEN.MAX_DEFAULT_ORE_VEIN_SPAWN_PER_CHUNK);
	}

	/**
	 * Full Override method - Doesn't follow @BALANCE.GEN.class
	 */
	public static void addOreToGen(Block block, int height, int maxBlocksInVain, int maxVeinsInChunk)
	{
		/*
		 * Gen ID is where to generate it. -1 is in nether, 0 is overworld, 1 is
		 * end.
		 */
		WrappedOreGenerator.Instruction instruction = new WrappedOreGenerator.Instruction(block, height, maxBlocksInVain, maxVeinsInChunk);
		generator = new WrappedOreGenerator(0, instruction);
	}

	public WrappedOreGenerator getOreGenerator()
	{
		if (generator != null)
		{
			return generator;
		}
		else
		{
			LogHelper.error("Generator is null!");
		}
		return null;
	}

}
