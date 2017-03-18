package com.uberverse.arkcraft.common.item.ranged;

import java.util.List;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.entity.projectile.EntityArkArrow;
import com.uberverse.arkcraft.common.item.ammo.ItemArrow;
import com.uberverse.arkcraft.init.ARKCraftRangedWeapons;
import com.uberverse.arkcraft.util.I18n;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemARKBow extends ItemBow
{
    private float power;

    public ItemARKBow(float power)
    {
        this.power = power;
        setCreativeTab(ARKCraft.tabARK);
    }

    @Override
    public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining)
    {
        ModelResourceLocation modelresourcelocation = new ModelResourceLocation(ARKCraft.MODID + ":bow", "inventory");

        if (stack.getItem() == this && player.getItemInUse() != null) {
            if (useRemaining >= 18) {
                modelresourcelocation = new ModelResourceLocation(ARKCraft.MODID + ":bow_pulling_2", "inventory");
            }
            else if (useRemaining > 13) {
                modelresourcelocation = new ModelResourceLocation(ARKCraft.MODID + ":bow_pulling_1", "inventory");
            }
            else if (useRemaining > 0) {
                modelresourcelocation = new ModelResourceLocation(ARKCraft.MODID + ":bow_pulling_0", "inventory");
            }
        }
        return modelresourcelocation;
    }

    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
        if (playerIn.isSneaking()) {
            String currentSelection = getSelectedArrow(itemStackIn);
            for (int i = 0; i < playerIn.inventory.getSizeInventory(); i++) {
                ItemStack itemstack = playerIn.inventory.getStackInSlot(i);
                if (itemstack != null && itemstack.getItem() instanceof ItemArrow) {
                    String newSelection = itemstack.getUnlocalizedName();
                    if (!newSelection.equals(currentSelection))
                        setSelectedArrow(itemStackIn, itemstack);
                }
            }
            return itemStackIn;
        }

        if (playerIn.capabilities.isCreativeMode) {
            playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
            if (getSelectedArrow(itemStackIn).isEmpty())
                setSelectedArrow(itemStackIn, new ItemStack(ARKCraftRangedWeapons.stone_arrow));
        }
        else {
            // if (getArrowType(itemStackIn) != null)
            if (!getSelectedArrow(itemStackIn).isEmpty()) {
                String selected = getSelectedArrow(itemStackIn);
                boolean flag = false;
                if (selected.equals(ARKCraftRangedWeapons.stone_arrow.getUnlocalizedName())) {
                    if (playerIn.inventory.hasItemStack((ARKCraftRangedWeapons.stone_arrow)))
                        flag = true;
                }
                // else if (getArrowType(stack).equals("metal_arrow"))
                else if (selected.equals(ARKCraftRangedWeapons.metal_arrow.getUnlocalizedName())) {
                    if (playerIn.inventory.hasItemStack((ARKCraftRangedWeapons.metal_arrow)))
                        flag = true;
                }
                // else if (getArrowType(stack).equals("tranq_arrow"))
                else if (selected.equals(ARKCraftRangedWeapons.tranq_arrow.getUnlocalizedName())) {
                    if (playerIn.inventory.hasItemStack((ARKCraftRangedWeapons.tranq_arrow)))
                        flag = true;
                }
                if (flag)
                    playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
            }
        }
        return itemStackIn;
    }
    //
    // public static String getArrowType(ItemStack stack)
    // {
    // if (!stack.hasTagCompound()) setArrowType(stack, null);
    // return stack.getTagCompound().getString("arrowtype");
    // }
    //
    // public static void setArrowType(ItemStack bow, ItemStack arrow)
    // {
    // if (!bow.hasTagCompound()) bow.setTagCompound(new NBTTagCompound());
    // bow.getTagCompound().setString("arrowtype", arrow != null ?
    // arrow.getUnlocalizedName() : "");
    // }

    public static String getSelectedArrow(ItemStack bow)
    {
        if (!bow.hasTagCompound())
            setSelectedArrow(bow, null);
        return bow.getTagCompound().getString("selectedarrow");
    }

    public static void setSelectedArrow(ItemStack bow, ItemStack arrow)
    {
        if (!bow.hasTagCompound())
            bow.setTagCompound(new NBTTagCompound());
        bow.getTagCompound().setString("selectedarrow", arrow != null ? arrow.getUnlocalizedName() : "");
    }

    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft)
    {
        if (!worldIn.isRemote) {
            int j = this.getMaxItemUseDuration(stack) - timeLeft;

            float f = (float) j / 20.0F;
            f = (f * f + f * 2.0F) / 3.0F;

            if ((double) f < 0.1D) {
                return;
            }

            if (f > 1.0F) {
                f = 1.0F;
            }

            float speed = f * 2 * 1.5f * power;

            // EntityArrow entityarrow = new EntityArrow(worldIn, playerIn,
            // speed);

            EntityArkArrow entityarrow = null;

            // if (getArrowType(stack).equals("stone_arrow"))
            if (getSelectedArrow(stack).equals(ARKCraftRangedWeapons.stone_arrow.getUnlocalizedName())) {
                if (playerIn.capabilities.isCreativeMode || playerIn.inventory.consumeInventoryItem(ARKCraftRangedWeapons.stone_arrow))
                    entityarrow = new EntityArkArrow(worldIn, playerIn, speed);
            }
            // else if (getArrowType(stack).equals("metal_arrow"))
            else if (getSelectedArrow(stack).equals(ARKCraftRangedWeapons.metal_arrow.getUnlocalizedName())) {
                // if
                // (playerIn.inventory.consumeInventoryItem(ARKCraftRangedWeapons.metal_arrow))
                // entityarrow =
                // new EntityMetalArrow(worldIn, playerIn, speed);
            }
            // else if (getArrowType(stack).equals("tranq_arrow"))
            else if (getSelectedArrow(stack).equals(ARKCraftRangedWeapons.tranq_arrow.getUnlocalizedName())) {
                // if
                // (playerIn.inventory.consumeInventoryItem(ARKCraftRangedWeapons.tranq_arrow))
                // entityarrow =
                // new EntityTranqArrow(worldIn, playerIn, speed);
            }

            if (f == 1.0F) {
                entityarrow.setIsCritical(true);
            }

            stack.damageItem(1, playerIn);

            // TODO use new SoundSystem
            // worldIn.playSoundAtEntity(playerIn, "random.bow", 1.0F, 1.0F /
            // (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

            entityarrow.canBePickedUp = 2;

            playerIn.addStat(StatList.getObjectUseStats(this));

            worldIn.spawnEntity(entityarrow);
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced)
    {
        super.addInformation(stack, playerIn, tooltip, advanced);
        String out = getSelectedArrow(stack);
        if (!out.isEmpty())
            tooltip.add(I18n.format(out + ".name"));
    }

    @Override
    public int getItemEnchantability()
    {
        return 0;
    }
}
