package com.uberverse.arkcraft.common.item.tool;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.item.IBreakable;
import com.uberverse.arkcraft.common.item.ItemQualitable;
import com.uberverse.arkcraft.common.item.Qualitable;
import com.uberverse.arkcraft.common.item.ranged.ItemRangedWeapon;
import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.arkcraft.util.AbstractItemStack;
import com.uberverse.arkcraft.util.AbstractItemStack.ChancingAbstractItemStack;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public abstract class ItemToolBase extends ItemQualitable implements IBreakable
{
    private final double baseBreakSpeed;
    private final double baseDamage;
    private final ToolType toolType;
    private static final Collection<Block> effectiveBlocks = Lists.newArrayList();
    private static final Map<Block, Collection<AbstractItemStack>> dropMap = Maps.newHashMap();
    public final ToolMaterial material;

    public static void registerEffectiveBlocks(Block... blocks)
    {
        Collections.addAll(effectiveBlocks, blocks);
    }

    public static void registerBlockDrops(Block block, Collection<AbstractItemStack> drops)
    {
        dropMap.put(block, drops);
    }

    public ItemToolBase(int baseDurability, double baseBreakSpeed, double baseDamage, ItemType type, ToolType toolType, ToolMaterial mat)
    {
        super(baseDurability, type);
        this.baseBreakSpeed = baseBreakSpeed;
        this.baseDamage = baseDamage;
        this.toolType = toolType;
        this.material = mat;
        setMaxStackSize(1);
        setCreativeTab(ARKCraft.tabARK);
    }

    @Override
    public final float getStrVsBlock(ItemStack itemstack, IBlockState state)
    {
        if (!effectiveBlocks.contains(state.getBlock()) || isBroken(itemstack))
            return 0;
        float base = 1.5f * (float) getBreakSpeed(itemstack);
        return MathHelper.clamp(base * getSpeedDivider(itemstack), 0.1f, base);
    }

    private final float getSpeedDivider(ItemStack stack)
    {
        if (!stack.hasTagCompound()) {
            setSpeedDivider(stack, -1);
        }
        return stack.getTagCompound().getFloat("divider");
    }

    private final void setSpeedDivider(ItemStack stack, float value)
    {
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setFloat("divider", value);
    }

    @Override
    public final boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack)
    {
        if (entityLiving instanceof EntityPlayer && !entityLiving.isSwingInProgress) {
            RayTraceResult mop = ItemRangedWeapon.rayTrace(entityLiving, 5, 0);
            if (mop.typeOfHit == RayTraceResult.Type.BLOCK) {
                IBlockState bs = entityLiving.world.getBlockState(mop.getBlockPos());
                Block target = effectiveBlocks.contains(bs.getBlock()) ? bs.getBlock() : null;
                if (target != null) {
                    World w = entityLiving.world;
                    BlockPos pos = mop.getBlockPos();
                    int size = bs.getBlock() == Blocks.LOG || bs.getBlock() == Blocks.LOG2 ? countTree(w, pos, target) : countOre(w, pos, target, (EntityPlayer) entityLiving, stack, false);
                    setSpeedDivider(stack, (float) 1 / (float) size);
                }
                return false;
            }
        }
        return super.onEntitySwing(entityLiving, stack);
    }

    private int countTree(World world, final BlockPos start, Block target)
    {
        BlockPos pos = start;

        int height = 0;
        while (world.getBlockState(pos).getBlock() == target) {
            height++;
            pos = pos.up();
        }

        pos = start.down();

        while (world.getBlockState(pos).getBlock() == target) {
            pos = pos.down();
            height++;
        }

        pos = start;
        int width = 1;
        if (world.getBlockState(pos.north()).getBlock() == target || world.getBlockState(pos.south()).getBlock() == target || world.getBlockState(pos.east()).getBlock() == target || world.getBlockState(pos.west()).getBlock() == target)
            width++;

        return width * width * height;
    }

    private final int countOre(World world, final BlockPos start, Block target, EntityLivingBase user, ItemStack stack, boolean shouldBreak)
    {
        BlockPos pos = start;
        Collection<BlockPos> done = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();

        queue.add(pos);

        while (!queue.isEmpty()) {
            pos = queue.remove();
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();

            for (int x = i - 1; x <= i + 1; x++)
                for (int z = k - 1; z <= k + 1; z++)
                    for (int y = j - 1; y <= j + 1; y++)
                        if (!(x == i && y == j && k == z)) {
                            BlockPos n = new BlockPos(x, y, z);
                            IBlockState blockState = world.getBlockState(n);
                            if (blockState.getBlock() == target) {
                                if (!done.contains(n))
                                    queue.add(n);
                            }
                        }
            done.add(pos);
        }

        if (shouldBreak)
            for (BlockPos p : done) {
                if (damage(stack, user)) {
                    if (user instanceof EntityPlayer)
                        target.onBlockHarvested(world, p, world.getBlockState(p), (EntityPlayer) user);
                    world.destroyBlock(p, false);
                }
                else
                    break;
            }

        return done.size();
    }

    int count = 0;

    private void destroyBlocks(World world, BlockPos pos, ItemStack stack, EntityLivingBase user, Predicate<IBlockState> blockChecker)
    {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        for (int j = y - 1; j <= y + 1; j++)
            for (int i = x - 1; i <= x + 1; i++)
                for (int k = z - 1; k <= z + 1; k++) {
                    BlockPos p = new BlockPos(i, j, k);
                    IBlockState blockState = world.getBlockState(p);
                    if (blockChecker.apply(blockState)) {
                        if (!isBroken(stack)) {
                            count++;
                            world.destroyBlock(p, false);
                            if (damage(stack, user)) {
                                this.destroyBlocks(world, p, stack, user, blockChecker);
                            }
                            else {
                                return;
                            }
                        }
                        else
                            return;
                    }
                }
    }

    private final double getToolModifier(ItemStack tool)
    {
        return Qualitable.get(tool).harvestMultiplier;
    }

    public final Collection<AbstractItemStack> applyOutputModifiers(final Collection<AbstractItemStack> originalDrops, ItemStack tool)
    {
        Collection<AbstractItemStack> out = Lists.newArrayList();
        for (AbstractItemStack ais : originalDrops) {
            if (ais instanceof ChancingAbstractItemStack && itemRand.nextInt((int) (1 / ((ChancingAbstractItemStack) ais).chance * getToolModifier(tool) * toolType.getModifier(ais.item) * material.getModifier(ais.item))) != 0)
                continue;
            out.add(new AbstractItemStack(ais.item, (int) Math.round(ais.getAmount() * getToolModifier(tool) * toolType.getModifier(ais.item) * material.getModifier(ais.item) * ((itemRand.nextInt(51) + itemRand.nextInt(51) + itemRand.nextInt(51)) / 3 + 75) / 100), ais.meta));
        }
        return out;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
    {
        Block block = state.getBlock();
        if (!worldIn.isRemote && effectiveBlocks.contains(block)) {
            if (dropMap.containsKey(block)) {
                destroyBlocks(worldIn, pos, stack, entityLiving, (IBlockState ibs) -> ibs.getBlock() == block);
                Collection<AbstractItemStack> drops = dropMap.get(block);
                drops = applyOutputModifiers(drops, stack);
                for (AbstractItemStack ais : drops)
                    ais.setAmount(ais.getAmount() * count);

                for (AbstractItemStack ais : drops)
                    for (ItemStack s : ais.toItemStacks())
                        Block.spawnAsEntity(worldIn, pos, s);
                count = 0;
            }
            else
                countOre(worldIn, pos, block, entityLiving, stack, true);

            return false;
        }
        return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
    }

    public final double getAttackDamage(ItemStack stack)
    {
        return Qualitable.get(stack).harvestMultiplier * baseDamage;
    }

    public final double getBreakSpeed(ItemStack stack)
    {
        return Qualitable.get(stack).harvestMultiplier * baseBreakSpeed;
    }

    private static UUID attUUID = UUID.fromString("attack_damage");

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public final Multimap getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
    {
        Multimap map = super.getAttributeModifiers(slot, stack);
        map.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(attUUID, "attack_damage", getAttackDamage(stack), 0));
        return map;
    }

    private static final double interval = 0.2, c = 1, cm = c - interval, cmm = cm - interval, cmmm = cmm - interval, cmmmm = cmmm - interval,
            cp = c + interval, cpp = cp + interval, cppp = cpp + interval;

    public enum ToolType
    {
        PICK(c, cmm, cmm, cmmm, cm, cmm, cm, c, cmm, c), HATCHET(cmm, c, cmmm, cm, cmm, cmmm, cmm, cmm, c, cmmmm);
        private final double thatchModifier, woodModifier, metalModifier, stoneModifier, crystalModifier, obsidianModifier, flintModifier,
                meatModifier, hideModifier, oilModifier;

        private ToolType(double thatchModifier, double woodModifier, double metalModifier, double stoneModifier, double crystalModifier, double obsidianModifier, double flintModifier, double meatModifier, double hideModifier, double oilModifier)
        {
            this.thatchModifier = thatchModifier;
            this.woodModifier = woodModifier;
            this.metalModifier = metalModifier;
            this.stoneModifier = stoneModifier;
            this.crystalModifier = crystalModifier;
            this.obsidianModifier = obsidianModifier;
            this.flintModifier = flintModifier;
            this.meatModifier = meatModifier;
            this.hideModifier = hideModifier;
            this.oilModifier = oilModifier;
        }

        private double getModifier(Item output)
        {
            if (output == ARKCraftItems.thatch)
                return thatchModifier;
            else if (output == ARKCraftItems.wood)
                return woodModifier;
            else if (output == ARKCraftItems.metal)
                return metalModifier;
            else if (output == ARKCraftItems.stone)
                return stoneModifier;
            else if (output == ARKCraftItems.crystal)
                return crystalModifier;
            else if (output == ARKCraftItems.obsidian)
                return obsidianModifier;
            else if (output == ARKCraftItems.flint)
                return flintModifier;
            else if (output == ARKCraftItems.meat_raw || output == ARKCraftItems.primemeat_raw)
                return meatModifier;
            else if (output == ARKCraftItems.hide)
                return hideModifier;
            else if (output == ARKCraftItems.oil)
                return oilModifier;
            else
                return 1;
        }
    }

    private static final int defaultMetalMaterialModifier = 3, defaultStoneMaterialModifier = 1;

    public enum ToolMaterial
    {
        STONE(defaultStoneMaterialModifier, defaultStoneMaterialModifier, 0.5, defaultStoneMaterialModifier, 0.05, 0.05, defaultStoneMaterialModifier,
                defaultStoneMaterialModifier, defaultStoneMaterialModifier, 0.05),
        METAL(defaultMetalMaterialModifier);

        private final double thatchModifier, woodModifier, metalModifier, stoneModifier, crystalModifier, obsidianModifier, flintModifier,
                meatModifier, hideModifier, oilModifier;

        private ToolMaterial(double general)
        {
            this(general, general, general, general, general, general, general, general, general, general);
        }

        private ToolMaterial(double thatchModifier, double woodModifier, double metalModifier, double stoneModifier, double crystalModifier, double obsidianModifier, double flintModifier, double meatModifier, double hideModifier, double oilModifier)
        {
            this.thatchModifier = thatchModifier;
            this.woodModifier = woodModifier;
            this.metalModifier = metalModifier;
            this.stoneModifier = stoneModifier;
            this.crystalModifier = crystalModifier;
            this.obsidianModifier = obsidianModifier;
            this.flintModifier = flintModifier;
            this.meatModifier = meatModifier;
            this.hideModifier = hideModifier;
            this.oilModifier = oilModifier;
        }

        private double getModifier(Item output)
        {
            if (output == ARKCraftItems.thatch)
                return thatchModifier;
            else if (output == ARKCraftItems.wood)
                return woodModifier;
            else if (output == ARKCraftItems.metal)
                return metalModifier;
            else if (output == ARKCraftItems.stone)
                return stoneModifier;
            else if (output == ARKCraftItems.crystal)
                return crystalModifier;
            else if (output == ARKCraftItems.obsidian)
                return obsidianModifier;
            else if (output == ARKCraftItems.flint)
                return flintModifier;
            else if (output == ARKCraftItems.meat_raw || output == ARKCraftItems.primemeat_raw)
                return meatModifier;
            else if (output == ARKCraftItems.hide)
                return hideModifier;
            else if (output == ARKCraftItems.oil)
                return oilModifier;
            else
                return 1;
        }
    }
}
