package com.arkcraft.common.entity.projectile;

import com.arkcraft.common.data.WeaponDamageSource;
import com.arkcraft.lib.LogHelper;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
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
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public abstract class EntityProjectile extends Entity implements IProjectile {
	@SuppressWarnings("unchecked")
	private static final Predicate<Entity> ARROW_TARGETS = Predicates.and(new Predicate[]{EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, new Predicate<Entity>() {
		public boolean apply(@Nullable Entity p_apply_1_) {
			return p_apply_1_.canBeCollidedWith();
		}
	}
	});
	private static final DataParameter<Byte> CRITICAL = EntityDataManager.<Byte>createKey(EntityProjectile.class, DataSerializers.BYTE);
	/**
	 * 1 if the player can pick up the arrow
	 */
	public EntityProjectile.PickupStatus pickupStatus;
	/**
	 * Seems to be some sort of timer for animating an arrow.
	 */
	public int arrowShake;
	/**
	 * The owner of this arrow.
	 */
	public Entity shootingEntity;
	public int range;
	protected boolean inGround;
	protected int timeInGround;
	private int xTile;
	private int yTile;
	private int zTile;
	private Block inTile;
	private int inData;
	private int ticksInGround;
	private int ticksInAir;
	private double damage;
	/**
	 * The amount of knockback an arrow applies when it hits a mob.
	 */
	private int knockbackStrength;

	public EntityProjectile(World worldIn) {
		super(worldIn);
		this.xTile = -1;
		this.yTile = -1;
		this.zTile = -1;
		this.pickupStatus = EntityProjectile.PickupStatus.DISALLOWED;
		this.damage = 2.0D;
		this.setSize(0.1F, 0.1F);
	}

	public EntityProjectile(World worldIn, double x, double y, double z) {
		this(worldIn);
		this.setPosition(x, y, z);
	}

	public EntityProjectile(World worldIn, EntityLivingBase shooter) {
		this(worldIn, shooter.posX, shooter.posY + (double) shooter.getEyeHeight() - 0.10000000149011612D, shooter.posZ);
		this.shootingEntity = shooter;

		if (shooter instanceof EntityPlayer) {
			this.pickupStatus = EntityProjectile.PickupStatus.ALLOWED;
		}
	}

	public EntityProjectile(World worldIn, EntityLivingBase shooter, float speed, float inaccuracy, double damage, int range) {
		super(worldIn);
		this.shootingEntity = shooter;
		this.pickupStatus = EntityProjectile.PickupStatus.DISALLOWED;
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
		shoot(motionX, motionY, motionZ, speed, inaccuracy);
	}

	public EntityProjectile(World world, EntityLivingBase entityliving, float speed) {
		this(world);
		shootingEntity = entityliving;
		if (entityliving instanceof EntityPlayer) {
			this.pickupStatus = EntityProjectile.PickupStatus.ALLOWED;
		}
		setLocationAndAngles(entityliving.posX, entityliving.posY + entityliving.getEyeHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
		posX -= MathHelper.cos((rotationYaw / 180F) * 3.141593F) * 0.16F;
		posY -= 0.1D;
		posZ -= MathHelper.sin((rotationYaw / 180F) * 3.141593F) * 0.16F;
		setPosition(posX, posY, posZ);
		motionX = -MathHelper.sin((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F);
		motionY = -MathHelper.sin((rotationPitch / 180F) * 3.141593F);
		motionZ = MathHelper.cos((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F);
		shoot(motionX, motionY, motionZ, speed * 1.2F, 2.0F);
	}

	public EntityProjectile(World worldIn, EntityLivingBase shooter, float speed, double damage, int range) {
		this(worldIn, shooter, speed, 1.0F, damage, range);
	}

	public static void registerFixesArrow(DataFixer fixer, String name) {
	}

	public static void registerFixesArrow(DataFixer fixer) {
		registerFixesArrow(fixer, "Arrow");
	}

	/**
	 * Checks if the entity is in range to render.
	 */
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double distance) {
		double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 10.0D;

		if (Double.isNaN(d0)) {
			d0 = 1.0D;
		}

		d0 = d0 * 64.0D * getRenderDistanceWeight();
		return distance < d0 * d0;
	}

	protected void entityInit() {
		this.dataManager.register(CRITICAL, Byte.valueOf((byte) 0));
	}

	public void setAim(Entity shooter, float pitch, float yaw, float p_184547_4_, float velocity, float inaccuracy) {
		float f = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
		float f1 = -MathHelper.sin(pitch * 0.017453292F);
		float f2 = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
		this.shoot((double) f, (double) f1, (double) f2, velocity, inaccuracy);
		this.motionX += shooter.motionX;
		this.motionZ += shooter.motionZ;

		if (!shooter.onGround) {
			this.motionY += shooter.motionY;
		}
	}

	@Override
	public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
		float f = MathHelper.sqrt(x * x + y * y + z * z);
		x = x / (double) f;
		y = y / (double) f;
		z = z / (double) f;
		x = x + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		y = y + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		z = z + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		x = x * (double) velocity;
		y = y * (double) velocity;
		z = z * (double) velocity;
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		float f1 = MathHelper.sqrt(x * x + z * z);
		this.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
		this.rotationPitch = (float) (MathHelper.atan2(y, (double) f1) * (180D / Math.PI));
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
		this.ticksInGround = 0;
	}

	/**
	 * Set the position and rotation values directly without any clamping.
	 */
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
		this.setPosition(x, y, z);
		this.setRotation(yaw, pitch);
	}

	/**
	 * Updates the velocity of the entity to a new value.
	 */
	@SideOnly(Side.CLIENT)
	public void setVelocity(double x, double y, double z) {
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;

		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt(x * x + z * z);
			this.rotationPitch = (float) (MathHelper.atan2(y, (double) f) * (180D / Math.PI));
			this.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
			this.prevRotationPitch = this.rotationPitch;
			this.prevRotationYaw = this.rotationYaw;
			this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			this.ticksInGround = 0;
		}
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();

		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
			this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f) * (180D / Math.PI));
			this.prevRotationYaw = this.rotationYaw;
			this.prevRotationPitch = this.rotationPitch;
		}

		BlockPos blockpos = new BlockPos(this.xTile, this.yTile, this.zTile);
		IBlockState iblockstate = this.world.getBlockState(blockpos);
		Block block = iblockstate.getBlock();

		if (iblockstate.getMaterial() != Material.AIR) {
			AxisAlignedBB axisalignedbb = iblockstate.getCollisionBoundingBox(this.world, blockpos);

			if (axisalignedbb != Block.NULL_AABB && axisalignedbb.offset(blockpos).contains(new Vec3d(this.posX, this.posY, this.posZ))) {
				this.inGround = true;
			}
		}

		if (this.arrowShake > 0) {
			--this.arrowShake;
		}

		if (this.inGround) {
			int j = block.getMetaFromState(iblockstate);

			if (block == this.inTile && j == this.inData) {
				++this.ticksInGround;

				int t = getMaxLifetime();
				if (t != 0 && this.ticksInGround >= t) {
					this.setDead();
				}
			} else {
				this.inGround = false;
				this.motionX *= (double) (this.rand.nextFloat() * 0.2F);
				this.motionY *= (double) (this.rand.nextFloat() * 0.2F);
				this.motionZ *= (double) (this.rand.nextFloat() * 0.2F);
				this.ticksInGround = 0;
				this.ticksInAir = 0;
			}

			++this.timeInGround;
		} else {
			this.timeInGround = 0;
			++this.ticksInAir;
			Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
			Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
			vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
			vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

			if (raytraceresult != null) {
				vec3d = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
			}

			Entity entity = this.findEntityOnPath(vec3d1, vec3d);

			if (entity != null) {
				raytraceresult = new RayTraceResult(entity);
			}

			if (raytraceresult != null && raytraceresult.entityHit != null && raytraceresult.entityHit instanceof EntityPlayer) {
				EntityPlayer entityplayer = (EntityPlayer) raytraceresult.entityHit;

				if (this.shootingEntity instanceof EntityPlayer && !((EntityPlayer) this.shootingEntity).canAttackPlayer(entityplayer)) {
					raytraceresult = null;
				}
			}

			if (raytraceresult != null) {
				if (raytraceresult.entityHit != null) {
					onEntityHit(raytraceresult.entityHit);
				} else {
					onGroundHit(raytraceresult);
				}
			}
		}

		if (this.getIsCritical()) {
			for (int k = 0; k < 4; ++k) {
				this.world.spawnParticle(EnumParticleTypes.CRIT, this.posX + this.motionX * (double) k / 4.0D, this.posY + this.motionY * (double) k / 4.0D, this.posZ + this.motionZ * (double) k / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ, new int[0]);
			}
		}

		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		float f4 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));

		for (this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f4) * (180D / Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
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
		float f1 = getAirResistance();
		float f2 = getGravity();

		if (this.isInWater()) {
			for (int i = 0; i < 4; ++i) {
				float f3 = 0.25F;
				this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ, new int[0]);
			}

			f1 = 0.6F;
		}

		if (this.isWet()) {
			this.extinguish();
		}

		this.motionX *= (double) f1;
		this.motionY *= (double) f1;
		this.motionZ *= (double) f1;

		if (!this.hasNoGravity()) {
			this.motionY -= 0.05000000074505806D;
		}

		this.setPosition(this.posX, this.posY, this.posZ);
		this.doBlockCollisions();
		gunRange();
	}

	public void onEntityHit(Entity entity) {
		if (entity != null) {
			applyEntityHitDamage(entity);
			applyEntityHitEffects(entity);
			this.setDead();
		}
	}

	public void applyEntityHitEffects(Entity entityHit) {
		if (isBurning() && !(entityHit instanceof EntityEnderman)) {
			entityHit.setFire(5);
		}
		if (entityHit instanceof EntityLivingBase) {
			EntityLivingBase entityliving = (EntityLivingBase) entityHit;
			if (this.knockbackStrength > 0) {
				float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

				if (f1 > 0.0F) {
					entityliving.addVelocity(
							this.motionX * (double) this.knockbackStrength * 0.6000000238418579D / (double) f1, 0.1D,
							this.motionZ * (double) this.knockbackStrength * 0.6000000238418579D / (double) f1);
				}
			}
			if (this.shootingEntity instanceof EntityLivingBase) {
				EnchantmentHelper.applyThornEnchantments(entityliving, this.shootingEntity);
				EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase) this.shootingEntity, entityliving);
			}
			this.arrowHit(entityliving);

			if (this.shootingEntity != null && entityliving != this.shootingEntity
					&& entityliving instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP) {
				((EntityPlayerMP) this.shootingEntity).connection.sendPacket(new SPacketChangeGameState(6, 0.0F));
			}
		}
	}

	public float getGravity() {
		return 0.05F;
	}

	public float getAirResistance() {
		return 0.99F;
	}

	public void applyEntityHitDamage(Entity entity) {
		float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
		int i = MathHelper.ceil((double) f * this.damage);

		if (this.getIsCritical()) {
			i += this.rand.nextInt(i / 2 + 2);
		}

		DamageSource damagesource;

		if (this.shootingEntity == null) {
			damagesource = WeaponDamageSource.causeThrownDamage(this, this);
		} else {
			damagesource = WeaponDamageSource.causeThrownDamage(this, this.shootingEntity);
		}

		if (entity.attackEntityFrom(damagesource, (float) i)) {
			playHitSound();
			setDead();
		}
	}

	public void playHitSound() {
		this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
	}

	public void gunRange() {
		world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
		if (ticksInAir >= range) {
			this.setDead();
		}
	}

	public void onGroundHit(RayTraceResult raytraceresult) {
		applyGroundHitEffects(raytraceresult);
		breakGlass(raytraceresult);
		setDead();
	}

	public void breakGlass(RayTraceResult raytraceresult) {
		BlockPos blockpos1 = raytraceresult.getBlockPos();

		if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) {
			if (this.world.getBlockState(blockpos1).getBlock() == Blocks.GLASS) {
				world.destroyBlock(blockpos1, true);
				LogHelper.error("Found block Glass");
			}
		} else {
			this.setDead();
		}
	}

	public void applyGroundHitEffects(RayTraceResult raytraceresult) {
		BlockPos blockpos = raytraceresult.getBlockPos();
		this.xTile = blockpos.getX();
		this.yTile = blockpos.getY();
		this.zTile = blockpos.getZ();
		IBlockState iblockstate = this.world.getBlockState(blockpos);
		this.inTile = iblockstate.getBlock();
		this.inData = this.inTile.getMetaFromState(iblockstate);
		this.motionX = (double) ((float) (raytraceresult.hitVec.x - this.posX));
		this.motionY = (double) ((float) (raytraceresult.hitVec.y - this.posY));
		this.motionZ = (double) ((float) (raytraceresult.hitVec.z - this.posZ));
		float f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
		this.posX -= this.motionX / (double) f2 * 0.05000000074505806D;
		this.posY -= this.motionY / (double) f2 * 0.05000000074505806D;
		this.posZ -= this.motionZ / (double) f2 * 0.05000000074505806D;
		playHitSound();
		this.inGround = true;
		this.arrowShake = getMaxArrowShake();
		this.setIsCritical(false);

		if (iblockstate.getMaterial() != Material.AIR) {
			this.inTile.onEntityCollision(this.world, blockpos, iblockstate, this);
		}
	}

	public int getMaxLifetime() {
		return 1200;
	}

	public int getMaxArrowShake() {
		return 7;
	}

	protected void bounceBack() {
		motionX *= -0.1D;
		motionY *= -0.1D;
		motionZ *= -0.1D;
		rotationYaw += 180F;
		prevRotationYaw += 180F;
		ticksInAir = 0;
	}

	protected void arrowHit(EntityLivingBase living) {
	}

	@Nullable
	protected Entity findEntityOnPath(Vec3d start, Vec3d end) {
		Entity entity = null;
		List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().offset(this.motionX, this.motionY, this.motionZ).expand(1.0, 1.0, 1.0), ARROW_TARGETS);
		double d0 = 0.0D;

		for (int i = 0; i < list.size(); ++i) {
			Entity entity1 = (Entity) list.get(i);

			if (entity1 != this.shootingEntity || this.ticksInAir >= 5) {
				AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896);
				RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(start, end);

				if (raytraceresult != null) {
					double d1 = start.squareDistanceTo(raytraceresult.hitVec);

					if (d1 < d0 || d0 == 0.0D) {
						entity = entity1;
						d0 = d1;
					}
				}
			}
		}

		return entity;
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound compound) {
		compound.setInteger("xTile", this.xTile);
		compound.setInteger("yTile", this.yTile);
		compound.setInteger("zTile", this.zTile);
		compound.setShort("life", (short) this.ticksInGround);
		ResourceLocation resourcelocation = (ResourceLocation) Block.REGISTRY.getNameForObject(this.inTile);
		compound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
		compound.setByte("inData", (byte) this.inData);
		compound.setByte("shake", (byte) this.arrowShake);
		compound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
		compound.setByte("pickup", (byte) this.pickupStatus.ordinal());
		compound.setDouble("damage", this.damage);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound compound) {
		this.xTile = compound.getInteger("xTile");
		this.yTile = compound.getInteger("yTile");
		this.zTile = compound.getInteger("zTile");
		this.ticksInGround = compound.getShort("life");

		if (compound.hasKey("inTile", 8)) {
			this.inTile = Block.getBlockFromName(compound.getString("inTile"));
		} else {
			this.inTile = Block.getBlockById(compound.getByte("inTile") & 255);
		}

		this.inData = compound.getByte("inData") & 255;
		this.arrowShake = compound.getByte("shake") & 255;
		this.inGround = compound.getByte("inGround") == 1;

		if (compound.hasKey("damage", 99)) {
			this.damage = compound.getDouble("damage");
		}

		if (compound.hasKey("pickup", 99)) {
			this.pickupStatus = EntityProjectile.PickupStatus.getByOrdinal(compound.getByte("pickup"));
		} else if (compound.hasKey("player", 99)) {
			this.pickupStatus = compound.getBoolean("player") ? EntityProjectile.PickupStatus.ALLOWED : EntityProjectile.PickupStatus.DISALLOWED;
		}
	}

	/**
	 * Called by a player entity when they collide with an entity
	 */
	public void onCollideWithPlayer(EntityPlayer entityIn) {
		if (!this.world.isRemote && this.inGround && this.arrowShake <= 0) {
			boolean flag = this.pickupStatus == EntityProjectile.PickupStatus.ALLOWED || this.pickupStatus == EntityProjectile.PickupStatus.CREATIVE_ONLY && entityIn.capabilities.isCreativeMode;

			if (this.pickupStatus == EntityProjectile.PickupStatus.ALLOWED && !entityIn.inventory.addItemStackToInventory(this.getArrowStack())) {
				flag = false;
			}

			if (flag) {
				this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				entityIn.onItemPickup(this, 1);
				this.setDead();
			}
		}
	}

	protected abstract ItemStack getArrowStack();

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
	 * prevent them from trampling crops
	 */
	protected boolean canTriggerWalking() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float partialTicks) {
		return 15728880;
	}

	public double getDamage() {
		return this.damage;
	}

	public void setDamage(double damageIn) {
		this.damage = damageIn;
	}

	/**
	 * Sets the amount of knockback the arrow applies when it hits a mob.
	 */
	public void setKnockbackStrength(int knockbackStrengthIn) {
		this.knockbackStrength = knockbackStrengthIn;
	}

	/**
	 * Returns true if it's possible to attack this entity with an item.
	 */
	public boolean canBeAttackedWithItem() {
		return false;
	}

	public float getEyeHeight() {
		return 0.0F;
	}

	/**
	 * Whether the arrow has a stream of critical hit particles flying behind it.
	 */
	public boolean getIsCritical() {
		byte b0 = ((Byte) this.dataManager.get(CRITICAL)).byteValue();
		return (b0 & 1) != 0;
	}

	/**
	 * Whether the arrow has a stream of critical hit particles flying behind it.
	 */
	public void setIsCritical(boolean critical) {
		byte b0 = ((Byte) this.dataManager.get(CRITICAL)).byteValue();

		if (critical) {
			this.dataManager.set(CRITICAL, Byte.valueOf((byte) (b0 | 1)));
		} else {
			this.dataManager.set(CRITICAL, Byte.valueOf((byte) (b0 & -2)));
		}
	}

	public static enum PickupStatus {
		DISALLOWED,
		ALLOWED,
		CREATIVE_ONLY;

		public static EntityProjectile.PickupStatus getByOrdinal(int ordinal) {
			if (ordinal < 0 || ordinal > values().length) {
				ordinal = 0;
			}

			return values()[ordinal];
		}
	}
}