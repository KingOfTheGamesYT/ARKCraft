package com.uberverse.arkcraft.common.entity.projectile;

import java.util.List;

import com.uberverse.arkcraft.common.data.WeaponDamageSource;
import com.uberverse.arkcraft.common.entity.ITranquilizer;
import com.uberverse.lib.LogHelper;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class EntityProjectile extends Entity implements IProjectile
{
    public int xTile = -1;
    public int yTile = -1;
    public int zTile = -1;
    public Block inTile;
    public int inData;
    public boolean inGround;
    /**
     * 1 if the player can pick up the arrow
     */
    public int canBePickedUp = 0;
    /**
     * Seems to be some sort of timer for animating an arrow.
     */
    public int arrowShake;
    /**
     * The owner of this projectile
     */
    public Entity shootingEntity;
    public int ticksInGround;
    public int ticksInAir;
    public double damage;
    public int range;

    /**
     * The amount of knockback an arrow applies when it hits a mob.
     */
    public int knockbackStrength;
    public boolean beenInGround;

    public EntityProjectile(World worldIn)
    {
        super(worldIn);
        this.setSize(0.1F, 0.1F);
    }

    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance)
    {
        double d1 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
        d1 *= 64.0D;
        return distance < d1 * d1;
    }

    public EntityProjectile(World worldIn, double x, double y, double z)
    {
        super(worldIn);
        this.setSize(0.05F, 0.05F);
        this.setPosition(x, y, z);
    }

    public EntityProjectile(World worldIn, EntityLivingBase shooter, float speed, float inaccuracy, double damage, int range)
    {
        super(worldIn);
        this.shootingEntity = shooter;
        this.canBePickedUp = 0;
        this.damage = damage;
        this.range = range;
        this.setSize(0.05F, 0.05F);
        this.setLocationAndAngles(shooter.posX, shooter.posY + (double) shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
        this.posX -= (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (double) (MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
        this.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
        this.motionY = (double) (-MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI));
        setThrowableHeading(motionX, motionY, motionZ, speed, inaccuracy);
    }

    public EntityProjectile(World world, EntityLivingBase entityliving, float speed)
    {
        this(world);
        shootingEntity = entityliving;
        if (entityliving instanceof EntityPlayer) {
            this.canBePickedUp = 1;
        }
        setLocationAndAngles(entityliving.posX, entityliving.posY + entityliving.getEyeHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
        posX -= MathHelper.cos((rotationYaw / 180F) * 3.141593F) * 0.16F;
        posY -= 0.1D;
        posZ -= MathHelper.sin((rotationYaw / 180F) * 3.141593F) * 0.16F;
        setPosition(posX, posY, posZ);
        motionX = -MathHelper.sin((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F);
        motionY = -MathHelper.sin((rotationPitch / 180F) * 3.141593F);
        motionZ = MathHelper.cos((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F);
        setThrowableHeading(motionX, motionY, motionZ, speed * 1.2F, 2.0F);
    }

    public EntityProjectile(World worldIn, EntityLivingBase shooter, float speed, double damage, int range)
    {
        this(worldIn, shooter, speed, 1.0F, damage, range);
    }

    protected void entityInit()
    {
        this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
    }

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z
     * direction.
     *
     * @param inaccuracy
     *            Higher means more error.
     */
    public void setThrowableHeading(double motionX, double motionY, double motionZ, float speed, float inaccuracy)
    {
        float f2 = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
        motionX /= (double) f2;
        motionY /= (double) f2;
        motionZ /= (double) f2;
        motionX += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) inaccuracy;
        motionY += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) inaccuracy;
        motionZ += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) inaccuracy;
        motionX *= (double) speed;
        motionY *= (double) speed;
        motionZ *= (double) speed;
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        float f3 = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
        this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(motionX, motionZ) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(motionY, (double) f3) * 180.0D / Math.PI);
        this.ticksInGround = 0;
    }

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    @SideOnly(Side.CLIENT)
    public void setVelocity(double x, double y, double z)
    {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(x * x + z * z);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(x, z) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(y, (double) f) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, (double) f) * 180.0D / Math.PI);
        }

        BlockPos blockpos = new BlockPos(this.xTile, this.yTile, this.zTile);
        IBlockState iblockstate = this.world.getBlockState(blockpos);
        Block block = iblockstate.getBlock();

        if (block.getMaterial() != Material.AIR) {
            block.setBlockBoundsBasedOnState(this.world, blockpos);
            AxisAlignedBB axisalignedbb = block.getCollisionBoundingBox(this.world, blockpos, iblockstate);

            if (axisalignedbb != null && axisalignedbb.isVecInside(new Vec3(this.posX, this.posY, this.posZ))) {
                this.inGround = true;
            }
        }

        if (this.arrowShake > 0) {
            --this.arrowShake;
        }

        if (this.inGround) {
            int j = block.getMetaFromState(iblockstate);

            if (block == this.inTile && j == this.inData) {
                ticksInGround++;
                int t = getMaxLifetime();
                if (t != 0 && ticksInGround >= t) {
                    setDead();
                }
            }
            else {
                this.inGround = false;
                this.motionX *= (double) (this.rand.nextFloat() * 0.2F);
                this.motionY *= (double) (this.rand.nextFloat() * 0.2F);
                this.motionZ *= (double) (this.rand.nextFloat() * 0.2F);
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        }
        else {
            ++this.ticksInAir;
            Vec3d vec31 = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d vec3 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            RayTraceResult movingobjectposition = this.world.rayTraceBlocks(vec31, vec3, false, true, false);
            vec31 = new Vec3d(this.posX, this.posY, this.posZ);
            vec3 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (movingobjectposition != null) {
                vec3 = new Vec3d(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
            }

            Entity entity = null;
            @SuppressWarnings("rawtypes")
            List list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            int i;
            float f1;

            for (i = 0; i < list.size(); ++i) {
                Entity entity1 = (Entity) list.get(i);

                if (entity1.canBeCollidedWith() && (entity1 != this.shootingEntity || this.ticksInAir >= 5)) {
                    f1 = 0.3F;
                    AxisAlignedBB axisalignedbb1 = entity1.getEntityBoundingBox().expand((double) f1, (double) f1, (double) f1);
                    RayTraceResult movingobjectposition1 = axisalignedbb1.calculateIntercept(vec31, vec3);

                    if (movingobjectposition1 != null) {
                        double d1 = vec31.distanceTo(movingobjectposition1.hitVec);

                        if (d1 < d0 || d0 == 0.0D) {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null) {
                movingobjectposition = new RayTraceResult(entity);
            }

            if (movingobjectposition != null && movingobjectposition.entityHit != null && movingobjectposition.entityHit instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer) movingobjectposition.entityHit;

                if (entityplayer.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer && !((EntityPlayer) this.shootingEntity).canAttackPlayer(entityplayer)) {
                    movingobjectposition = null;
                }
            }

            float f2;
            float f4;

            if (movingobjectposition != null) {

                if (movingobjectposition.entityHit != null) {
                    onEntityHit(movingobjectposition.entityHit);
                }
                else {
                    onGroundHit(movingobjectposition);
                }
            }

            if (this.getIsCritical()) {
                for (i = 0; i < 4; ++i) {
                    this.world.spawnParticle(EnumParticleTypes.CRIT, this.posX + this.motionX * (double) i / 4.0D, this.posY + this.motionY * (double) i / 4.0D, this.posZ + this.motionZ * (double) i / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ, new int[0]);
                }
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

            for (this.rotationPitch = (float) (Math.atan2(this.motionY, (double) f2) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
                ;
            }

            while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
                this.prevRotationPitch += 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
                this.prevRotationYaw -= 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
            float res = getAirResistance();
            float grav = getGravity();

            if (this.isInWater()) {
                beenInGround = true;
                for (int l = 0; l < 4; ++l) {
                    f4 = 0.25F;
                    this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * (double) f4, this.posY - this.motionY * (double) f4, this.posZ - this.motionZ * (double) f4, this.motionX, this.motionY, this.motionZ, new int[0]);

                }

                res = 0.6F;
            }

            if (this.isWet()) {
                this.extinguish();
            }

            this.motionX *= (double) res;
            this.motionY *= (double) res;
            this.motionZ *= (double) res;
            this.motionY -= (double) grav;
            this.setPosition(this.posX, this.posY, this.posZ);
            this.doBlockCollisions();
        }

        // worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX,
        // posY, posZ, 0.0D, 0.0D, 0.0D);

        // System.out.println(ticksInAir);
        gunRange();
    }

    public void gunRange()
    {
        System.out.println(ticksInAir);
        world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
        if (ticksInAir >= range) {
            this.setDead();
        }
    }

    public void onGroundHit(RayTraceResult movingobjectposition)
    {
        applyGroundHitEffects(movingobjectposition);
        breakGlass(movingobjectposition);
        setDead();
    }

    public void breakGlass(RayTraceResult movingobjectposition)
    {

        BlockPos blockpos1 = movingobjectposition.getBlockPos();

        if (movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK) {
            if (this.world.getBlockState(blockpos1).getBlock().getMaterial().equals(Material.GLASS)) {
                world.destroyBlock(blockpos1, true);
                // worldObj.playSoundEffect(xTile, yTile, zTile,
                // "random.break_glass", 2F, 3F);
                LogHelper.error("Found block Glass");
            }
        }
        else {
            this.setDead();
        }
    }

    public void applyGroundHitEffects(RayTraceResult movingobjectposition)
    {
        BlockPos blockpos1 = movingobjectposition.getBlockPos();
        IBlockState iblockstate = this.world.getBlockState(blockpos1);
        this.xTile = blockpos1.getX();
        this.yTile = blockpos1.getY();
        this.zTile = blockpos1.getZ();
        iblockstate = this.world.getBlockState(blockpos1);
        this.inTile = iblockstate.getBlock();
        this.inData = this.inTile.getMetaFromState(iblockstate);
        this.motionX = (double) ((float) (movingobjectposition.hitVec.xCoord - this.posX));
        this.motionY = (double) ((float) (movingobjectposition.hitVec.yCoord - this.posY));
        this.motionZ = (double) ((float) (movingobjectposition.hitVec.zCoord - this.posZ));
        float f3 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
        this.posX -= this.motionX / (double) f3 * 0.05000000074505806D;
        this.posY -= this.motionY / (double) f3 * 0.05000000074505806D;
        this.posZ -= this.motionZ / (double) f3 * 0.05000000074505806D;
        this.playHitSound();
        this.inGround = true;
        beenInGround = true;
        this.arrowShake = getMaxArrowShake();
        this.setIsCritical(false);

        if (this.inTile.getMaterial() != Material.AIR) {
            this.inTile.onEntityCollidedWithBlock(this.world, blockpos1, iblockstate, this);
        }
    }

    public int getMaxLifetime()
    {
        return 1200;
    }

    public int getMaxArrowShake()
    {
        return 7;
    }

    public void onEntityHit(Entity entityHit)
    {
        applyEntityHitDamage(entityHit);
        applyEntityHitEffects(entityHit);
        setDead();
    }

    protected void applyEntityHitDamage(Entity entityHit)
    {
        DamageSource damagesource = null;
        if (shootingEntity == null) {
            damagesource = WeaponDamageSource.causeThrownDamage(this, this);
        }
        else {
            damagesource = WeaponDamageSource.causeThrownDamage(this, shootingEntity);
        }
        if (entityHit.attackEntityFrom(damagesource, (float) damage)) {
            playHitSound();
            setDead();
        }
        if (this instanceof ITranquilizer) {
            ((ITranquilizer) this).applyTorpor(entityHit);
        }
    }

    protected void bounceBack()
    {
        motionX *= -0.1D;
        motionY *= -0.1D;
        motionZ *= -0.1D;
        rotationYaw += 180F;
        prevRotationYaw += 180F;
        ticksInAir = 0;
    }

    protected void applyEntityHitEffects(Entity entityHit)
    {
        if (isBurning() && !(entityHit instanceof EntityEnderman)) {
            entityHit.setFire(5);
        }
        if (entityHit instanceof EntityLivingBase) {
            EntityLivingBase entityliving = (EntityLivingBase) entityHit;
            if (knockbackStrength > 0) {
                float f = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
                if (f > 0.0F) {
                    entityHit.addVelocity(motionX * knockbackStrength * 0.6D / f, 0.1D, motionZ * knockbackStrength * 0.6D / f);
                }
            }
            if (shootingEntity instanceof EntityLivingBase) {
                EnchantmentHelper.func_151384_a(entityliving, this.shootingEntity);
                EnchantmentHelper.func_151385_b((EntityLivingBase) this.shootingEntity, entityliving);
            }
            if (shootingEntity instanceof EntityPlayerMP && shootingEntity != entityHit && entityHit instanceof EntityPlayer) {
                ((EntityPlayerMP) shootingEntity).playerNetServerHandler.sendPacket(new SPacketChangeGameState(6, 0));
            }
        }
    }

    public float getGravity()
    {
        return 0.05F;
    }

    public float getAirResistance()
    {
        return 0.99F;
    }

    public final double getTotalVelocity()
    {
        return Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn)
    {
        if (!this.world.isRemote && this.inGround && this.arrowShake <= 0) {
            boolean flag = this.canBePickedUp == 1 || this.canBePickedUp == 2 && entityIn.capabilities.isCreativeMode;

            ItemStack item = getPickupItem();
            if (item == null) {
                return;
            }

            if (this.canBePickedUp == 1 && !entityIn.inventory.addItemStackToInventory(item)) {
                flag = false;
            }

            if (flag) {
                this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                onItemPickup(entityIn);
                this.setDead();
            }
        }
    }

    public void onItemPickup(EntityPlayer entityplayer)
    {
        entityplayer.onItemPickup(this, 1);
    }

    public ItemStack getPickupItem()
    {
        return null;
    }

    public void setDamage(double damage)
    {
        this.damage = damage;
    }

    public double getDamage()
    {
        return this.damage;
    }

    public void playHitSound()
    {

    }

    /**
     * Sets the amount of knockback the arrow applies when it hits a mob.
     */
    public void setKnockbackStrength(int knockBack)
    {
        this.knockbackStrength = knockBack;
    }

    public boolean aimRotation()
    {
        return true;
    }

    /**
     * Whether the arrow has a stream of critical hit particles flying behind
     * it.
     */
    public void setIsCritical(boolean isCritical)
    {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);

        if (isCritical) {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 | 1)));
        }
        else {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 & -2)));
        }
    }

    /**
     * Whether the arrow has a stream of critical hit particles flying behind
     * it.
     */
    public boolean getIsCritical()
    {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);
        return (b0 & 1) != 0;
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setShort("xTile", (short) this.xTile);
        tagCompound.setShort("yTile", (short) this.yTile);
        tagCompound.setShort("zTile", (short) this.zTile);
        tagCompound.setShort("life", (short) this.ticksInGround);
        ResourceLocation resourcelocation = (ResourceLocation) Block.REGISTRY.getNameForObject(this.inTile);
        tagCompound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
        tagCompound.setByte("inData", (byte) this.inData);
        tagCompound.setByte("shake", (byte) this.arrowShake);
        tagCompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
        tagCompound.setByte("pickup", (byte) this.canBePickedUp);
        tagCompound.setDouble("damage", this.damage);
        tagCompound.setBoolean("beenInGround", beenInGround);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        this.xTile = tagCompund.getShort("xTile");
        this.yTile = tagCompund.getShort("yTile");
        this.zTile = tagCompund.getShort("zTile");
        this.ticksInGround = tagCompund.getShort("life");
        this.beenInGround = tagCompund.getBoolean("beenInGrond");

        if (tagCompund.hasKey("inTile", 8)) {
            this.inTile = Block.getBlockFromName(tagCompund.getString("inTile"));
        }
        else {
            this.inTile = Block.getBlockById(tagCompund.getByte("inTile") & 255);
        }

        this.inData = tagCompund.getByte("inData") & 255;
        this.arrowShake = tagCompund.getByte("shake") & 255;
        this.inGround = tagCompund.getByte("inGround") == 1;

        if (tagCompund.hasKey("damage", 99)) {
            this.damage = tagCompund.getDouble("damage");
        }

        if (tagCompund.hasKey("pickup", 99)) {
            this.canBePickedUp = tagCompund.getByte("pickup");
        }
        else if (tagCompund.hasKey("player", 99)) {
            this.canBePickedUp = tagCompund.getBoolean("player") ? 1 : 0;
        }
    }
}