/**
 * 
 */
package com.uberverse.arkcraft.client.easter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

/**
 * @author ERBF
 *
 */

public class Easter 
{
	
	public static class MICHAEL_BAY
	{
		
		public static void createExplosion(EntityPlayer player, World world, float power, double x, double y, double z, double xOffset, double yOffset, double zOffset)
		{
			generateExplosion(player, world, power, x, y, z, xOffset, yOffset, zOffset, true);
		}
		
		public static void createExplosion(EntityPlayer player, World world, float power, BlockPos pos, double xOffset, double yOffset, double zOffset)
		{
			createExplosion(player, world, power, pos.getX(), pos.getY(), pos.getZ(), xOffset, yOffset, zOffset);
		}
		
		public static void createExplosionNoDamage(EntityPlayer player, World world, float power, double x, double y, double z, double xOffset, double yOffset, double zOffset)
		{
			generateExplosion(player, world, power, x, y, z, xOffset, yOffset, zOffset, false);
		}
		
		public static void createExplosionNoDamage(EntityPlayer player, World world, float power, BlockPos pos, double xOffset, double yOffset, double zOffset)
		{
			generateExplosion(player, world, power, pos.getX(), pos.getY(), pos.getZ(), xOffset, yOffset, zOffset, false);
		}
		
		private static void generateExplosion(EntityPlayer player, World world, float power, double x, double y, double z, double xOffset, double yOffset, double zOffset, boolean damaging)
		{
			Explosion explosion = new Explosion(world, player, x + xOffset, y + yOffset, z + zOffset, power, damaging, damaging);
			if(damaging)
			{
				explosion.doExplosionA();
				explosion.doExplosionB(true);
			} else {
				explosion.doExplosionB(true);
			}
		}
		
	}

}
