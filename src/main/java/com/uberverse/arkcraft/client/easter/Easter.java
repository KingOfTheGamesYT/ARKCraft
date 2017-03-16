/**
 * 
 */
package com.uberverse.arkcraft.client.easter;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.client.achievement.ARKCraftAchievements;
import com.uberverse.arkcraft.client.achievement.AchievementHelper;
import com.uberverse.arkcraft.init.ARKCraftRangedWeapons;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author ERBF
 */

public class Easter
{
    @SuppressWarnings("static-access")
    @SubscribeEvent
    public static void handleInteract(PlayerInteractEvent.RightClickBlock event) throws NullPointerException
    {
        ItemStack item = event.getEntityPlayer().getActiveItemStack();
        AchievementPage page = ARKCraftAchievements.page;
        Achievement achievement = ARKCraftAchievements.achievementMichaelBay;

        EntityPlayer player = event.getEntityPlayer();
        if (!AchievementHelper.containsAchievement(page, achievement)) {
            if (item != null && item.getItem() == ARKCraftRangedWeapons.rocket_propelled_grenade) {
                if (item.getDisplayName().equals("Michael_Bay") && player.isSneaking()) {

                    double x = player.posX;
                    double y = player.posY;
                    double z = player.posZ;

                    Easter.MICHAEL_BAY.createExplosionNoDamage(player, event.getWorld(), 5.0F, x, y, z, 0, 1, 0);
                    AchievementHelper.registerAndDisplay(player, achievement);
                }
            }
        }
    }

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
            if (damaging) {
                explosion.doExplosionA();
                explosion.doExplosionB(true);
                logExplosion(explosion, player, true);
            }
            else {
                explosion.doExplosionB(true);
                logExplosion(explosion, player, false);
            }
        }

        private static void logExplosion(Explosion explosion, EntityPlayer player, boolean explosionA)
        {
            Vec3d coords = explosion.getPosition();
            String playerName = "Explosion Created By:" + player.getDisplayNameString();
            String damaging = ", Damaging:" + (explosionA ? "true" : "false");
            String x = ", x:" + coords.xCoord, y = ", y:" + coords.yCoord, z = ", z:" + coords.zCoord;
            ARKCraft.logger.error("THIS IS NOT AN ERROR! " + playerName + x + y + z + damaging);
        }

    }

}