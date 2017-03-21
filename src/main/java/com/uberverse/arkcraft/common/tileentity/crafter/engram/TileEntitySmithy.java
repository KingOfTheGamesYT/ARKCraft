package com.uberverse.arkcraft.common.tileentity.crafter.engram;

import java.util.Random;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class TileEntitySmithy extends TileEntityEngramCrafter
{
	@Override
	public void update()
	{
		super.update();
		if (isCrafting() && new Random().nextInt(100) == 0) world.playSound(pos.getX(), pos.getY(), pos.getZ(),
				SoundEvent.REGISTRY.getObject(new ResourceLocation("arkcraft:smithy_hammer")), SoundCategory.BLOCKS, 1, 0, true);
	}

	public TileEntitySmithy()
	{
		super(24, "smithy");
	}
}
