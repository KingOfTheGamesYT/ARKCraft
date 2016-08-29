package com.uberverse.lib;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class Utils
{
	public static EnumFacing getDirectionFacing(EntityLivingBase entity, boolean includeUpAndDown)
	{
		double yaw = entity.rotationYaw;
		while (yaw < 0)
			yaw += 360;
		yaw = yaw % 360;
		if (includeUpAndDown)
		{
			if (entity.rotationPitch > 45) return EnumFacing.DOWN;
			else if (entity.rotationPitch < -45) return EnumFacing.UP;
		}
		if (yaw < 45) return EnumFacing.SOUTH;
		else if (yaw < 135) return EnumFacing.WEST;
		else if (yaw < 225) return EnumFacing.NORTH;
		else if (yaw < 315) return EnumFacing.EAST;

		else return EnumFacing.SOUTH;
	}

	public static boolean isUseable(BlockPos pos, EntityPlayer player, World worldObj, TileEntity thisT)
	{
		return worldObj.getTileEntity(pos) != thisT ? false : player
				.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
	}

	static Field equippedProgress, prevEquippedProgress;

	// Function that handles the hack for the Item Swap Animation
	public static void setItemRendererEquippProgress(float From0To1, boolean isSmooth)
	{
		ItemRenderer IR = Minecraft.getMinecraft().entityRenderer.itemRenderer;
		if (IR != null) try
		{
			if (!isSmooth)
			{
				if (prevEquippedProgress == null) prevEquippedProgress = ItemRenderer.class
						.getDeclaredField("prevEquippedProgress");
				prevEquippedProgress.setAccessible(true);
				prevEquippedProgress.setFloat(IR, From0To1);
			}
			if (equippedProgress == null) equippedProgress = ItemRenderer.class
					.getDeclaredField("equippedProgress");
			equippedProgress.setAccessible(true);
			equippedProgress.setFloat(IR, From0To1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
