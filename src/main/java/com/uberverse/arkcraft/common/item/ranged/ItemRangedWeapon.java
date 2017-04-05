package com.uberverse.arkcraft.common.item.ranged;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.client.event.ClientEventHandler;
import com.uberverse.arkcraft.common.data.WeaponModAttributes;
import com.uberverse.arkcraft.common.entity.projectile.EntityProjectile;
import com.uberverse.arkcraft.common.entity.projectile.ProjectileType;
import com.uberverse.arkcraft.common.inventory.InventoryAttachment;
import com.uberverse.arkcraft.common.item.IMeshedItem;
import com.uberverse.arkcraft.common.item.ammo.ItemProjectile;
import com.uberverse.arkcraft.common.network.GunFired;
import com.uberverse.arkcraft.init.ARKCraftBlocks;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Lewis_McReu
 * @author BubbleTrouble
 */
public abstract class ItemRangedWeapon extends ItemBow implements IMeshedItem
{
	protected static final int MAX_DELAY = 72000;
	
	static boolean notClickedYet = true;

	private Set<ItemProjectile> projectiles;
	private final int maxAmmo;
	private final int ammoConsumption;
	private final String defaultAmmoType;
	private final long shotInterval;
	private final float speed;
	private final float inaccuracy;
	public long nextShotMillis = 0;
	private double damage;
	private int range;
	private boolean fired;
	private static int ticks;
	private final float recoilSneaking;
	private final float recoil;
	private final boolean shouldRecoil;
	
	public ItemRangedWeapon(String name, int durability, int maxAmmo, String defaultAmmoType, int ammoConsumption, double shotInterval, float speed, float inaccuracy, double damage, int range, float recoil, float recoilSneaking, boolean shouldRecoil)
	{
		super();
		this.speed = speed;
		this.inaccuracy = inaccuracy;
		this.shotInterval = (long) shotInterval * 1000;
		this.ammoConsumption = ammoConsumption;
		this.defaultAmmoType = defaultAmmoType;
		this.maxAmmo = maxAmmo;
		this.setMaxDamage(durability);
		this.setMaxStackSize(1);
		this.projectiles = new HashSet<>();
		this.setCreativeTab(ARKCraft.tabARK);
		this.setUnlocalizedName(name);
		this.damage = damage;
		this.range = range;
		this.recoilSneaking = recoilSneaking;
		this.recoil = recoil;
		this.shouldRecoil = shouldRecoil;
		ARKCraft.proxy.registerModelMeshDef(this);
	}

	@Override
	public String getUnlocalizedName()
	{
		String s = super.getUnlocalizedName();
		return s.substring(s.indexOf('.') + 1);
	}

	public int getMaxAmmo()
	{
		return this.maxAmmo;
	}

	public long getShotInterval()
	{
		return this.shotInterval;
	}

	public int getAmmoConsumption()
	{
		return this.ammoConsumption;
	}

	public boolean registerProjectile(ItemProjectile projectile)
	{
		return this.projectiles.add(projectile);
	}

	public boolean isValidProjectile(Item item)
	{
		return this.projectiles.contains(item);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelResourceLocation getModel(ItemStack stack)
	{
		String jsonPath = ARKCraft.MODID + ":weapons/" + this.getUnlocalizedName();
		InventoryAttachment att = InventoryAttachment.create(stack);
		if (att != null) {
			if (att.isScopePresent()) {
				jsonPath = jsonPath + "_scope";
			}
			else if (att.isFlashPresent()) {
				jsonPath = jsonPath + "_flashlight";
			}
			else if (att.isLaserPresent()) {
				jsonPath = jsonPath + "_laser";
			}
			else if (att.isSilencerPresent()) {
				jsonPath = jsonPath + "_silencer";
			}
			else if (att.isHoloScopePresent()) {
				jsonPath = jsonPath + "_holo_scope";
			}
		}
		if (isReloading(stack)) {
			jsonPath = jsonPath + "_reload";
		}
		return new ModelResourceLocation(jsonPath, "inventory");
	}

	public Random getItemRand()
	{
		return new Random();
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return MAX_DELAY;
	}

	public void setReloading(ItemStack stack, EntityPlayer player, boolean reloading)
	{
		stack.getTagCompound().setBoolean("reloading", reloading);
	}

	public boolean isReloading(ItemStack stack)
	{
		checkNBT(stack);
		return stack.getTagCompound().getBoolean("reloading");
	}

	public int getReloadTicks(ItemStack stack)
	{
		return stack.getTagCompound().getInteger("reloadTicks");
	}

	private void setReloadTicks(ItemStack stack, int reloadTicks)
	{
		stack.getTagCompound().setInteger("reloadTicks", reloadTicks);
	}
	
	public boolean fired(ItemStack stack)
	{
		checkNBT(stack);
		return stack.getTagCompound().getBoolean("fired");
	}

	public void setFired(ItemStack stack, EntityPlayer player, boolean fired)
	{
		stack.getTagCompound().setBoolean("fired", fired);
	}

	private void checkNBT(ItemStack stack)
	{
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
	{
		if(isSelected)
		{
			if(world.isRemote)
			{
				updateClient(stack, world, (EntityPlayer) entity, itemSlot, isSelected);
			} else updateServer(stack, world, (EntityPlayer) entity, itemSlot, isSelected);
		}
	}

	public void updateServer(ItemStack stack, World world, EntityPlayer entity, int itemSlot, boolean isSelected) 
	{

	}

	public void updateClient(ItemStack stack, World world, EntityPlayer entity, int itemSlot, boolean isSelected) 
	{
	    InventoryAttachment inv = InventoryAttachment.create(stack);

	    if (Mouse.getEventButtonState()) {
	        if (Mouse.getEventButton() == 0) {
	            notClickedYet = true;
	        }
	    }
	    else 
		    {
			if (notClickedYet) 
			{
				if (Mouse.getEventButton() == 0)
				{
					notClickedYet = false;
					if(Minecraft.getMinecraft().currentScreen == null)
					{
						if(canFire(stack, entity))
						{
							if(this.nextShotMillis < System.currentTimeMillis())
							{
								ARKCraft.modChannel.sendToServer(new GunFired());
								setFired(stack, entity, true);
							}
						}
						else
						{
							if (!this.isReloading(stack))
							{
								soundEmpty(stack, world, entity);
							}
						
						}
					}
				}
			}
			else
			{
				
			}
		}
		if (inv != null && inv.isFlashPresent())
		{
			updateFlashlight(entity);
		}
		else if (inv != null && inv.isLaserPresent())
		{
			updateLaser(entity);
		}
		if(fired(stack))
		{
			++ticks;
			if(ticks >= recoilDelay() + 1)
			{
				recoilDown(entity, getRecoil(), getRecoilSneaking(), isSelected);
				ticks = 0;
				setFired(stack, entity, false);
			}
		}
	}

		/*
		if (entityIn instanceof EntityPlayer) {
			if (isSelected) {
				InventoryAttachment inv = InventoryAttachment.create(stack);
				if (inv != null && inv.isFlashPresent()) {
					updateFlashlight(entityIn);
				}
				else if (inv != null && inv.isLaserPresent()) {
					updateLaser(entityIn);
				}
			}
			if(fired(stack) && entityIn instanceof EntityPlayer)
			{
				System.out.println(ticks);
				afterFire(stack, worldIn, (EntityPlayer) entityIn);
				ticks++;
				if(ticks >= recoilDelay())
				{
					float f = entityIn.isSneaking() ? -0.01F : -0.02F;
					double d = -MathHelper.sin((entityIn.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((0 / 180F)
							* 3.141593F) * f;
					double d1 = MathHelper.cos((entityIn.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((0 / 180F)
							* 3.141593F) * f;
					recoilDown((EntityPlayer)entityIn, recoil, recoilSneaking, shouldRecoil);
					entityIn.addVelocity(d, 0, d1);
					ticks = 0;
					setFired(stack, (EntityPlayer) entityIn, false);
				}
				}
			}	
			else if (isReloading(stack)) {
				resetReload(stack, (EntityPlayer) entityIn);
			}
			if(Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && !Minecraft.getMinecraft().isGamePaused())
			{
				if(!worldIn.isRemote){
				if(stack.getItem() instanceof ItemRangedWeapon)
				{
					if (canFire(stack, (EntityPlayer) entityIn))
					{
						if (this.nextShotMillis < System.currentTimeMillis())
							System.out.println("Fire");
						ARKCraft.modChannel.sendToServer(new GunFired());
						//	fire(stack, worldIn, (EntityPlayer) entityIn, 0);
							new ActionResult<>(EnumActionResult.SUCCESS, stack);
				//		((EntityPlayer) entityIn).setItemInUse(stack, getMaxItemUseDuration(stack));
					}
					else {
						// Can't reload; no ammo
						if (!this.isReloading(stack)) {
							soundEmpty(stack, worldIn, (EntityPlayer) entityIn);
						}
					}
				}			
			}			
		}
	} */
	
	public void recoilDown(EntityPlayer entityIn, float recoil, float recoilSneaking, boolean shouldRecoil)
	{
		float i = recoil == 0F ? 0F : recoil - 0.5F;
		float j = recoilSneaking == 0F ? 0F : recoilSneaking - 0.5F;
		if(shouldRecoil)entityIn.rotationPitch += entityIn.isSneaking() ? i : j;
	}
	
	public float getRecoil()
	{
		return recoil;
	}
	
	public float getRecoilSneaking()
	{
		return recoilSneaking;
	}
	
	public boolean getShouldRecoil()
	{
		return shouldRecoil;
	}
	
	public void recoilUp(EntityPlayer entityIn, float recoil, float recoilSneaking,  boolean shouldRecoil)
	{
		if(shouldRecoil)entityIn.rotationPitch -= entityIn.isSneaking() ? recoil : recoilSneaking;
	}

	public int recoilDelay() 
	{
		return 4;
	}

	private void updateLaser(Entity entityIn)
	{
		World w = entityIn.world;
		RayTraceResult mop = rayTrace(entityIn, 35, 1.0F);
		if (mop.typeOfHit == RayTraceResult.Type.BLOCK) {
			double x = mop.hitVec.xCoord;
			double y = mop.hitVec.yCoord;
			double z = mop.hitVec.zCoord;

			w.spawnParticle(EnumParticleTypes.REDSTONE, x, y, z, 0, 0, 0, 0);
		}
	}

	private void resetReload(ItemStack stack, EntityPlayer player)
	{
		setReloading(stack, player, false);
		setReloadTicks(stack, 0);
	}

	private void updateFlashlight(Entity entityIn)
	{
		RayTraceResult mop = rayTrace(entityIn, 20, 1.0F);
		if (mop != null && mop.typeOfHit != RayTraceResult.Type.MISS) {
			World world = entityIn.world;
			BlockPos pos;

			if (mop.typeOfHit == RayTraceResult.Type.ENTITY) {
				pos = mop.entityHit.getPosition();
			}
			else {
				pos = mop.getBlockPos();
				pos = pos.offset(mop.sideHit);
			}

			if (world.isAirBlock(pos)) {
				world.setBlockState(pos, ARKCraftBlocks.blockLight.getDefaultState());
				world.scheduleUpdate(pos, ARKCraftBlocks.blockLight, 2);
			}
		}
	}

	public static Vec3d getPositionEyes(Entity player, float partialTick)
	{
		if (partialTick == 1.0F) {
			return new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
		}
		else {
			double d0 = player.prevPosX + (player.posX - player.prevPosX) * partialTick;
			double d1 = player.prevPosY + (player.posY - player.prevPosY) * partialTick + player.getEyeHeight();
			double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTick;
			return new Vec3d(d0, d1, d2);
		}
	}

	public static RayTraceResult rayTrace(Entity player, double distance, float partialTick)
	{
		Vec3d vec3 = getPositionEyes(player, partialTick);
		Vec3d vec31 = player.getLook(partialTick);
		Vec3d vec32 = vec3.addVector(vec31.xCoord * distance, vec31.yCoord * distance, vec31.zCoord * distance);
		return player.world.rayTraceBlocks(vec3, vec32, false, false, true);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		
		return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn); 		
		/*
		if (itemStackIn.stackSize <= 0 || playerIn.isHandActive()) { //== hand
			return new ActionResult<>(EnumActionResult.PASS, itemStackIn);
		}

		if (canFire(itemStackIn, playerIn)) {
			if (this.nextShotMillis < System.currentTimeMillis())
				System.out.println("Fire");
				// Start aiming weapon to fire
				playerIn.setActiveHand(hand);
			return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
			//playerIn.setItemInUse(itemStackIn, getMaxItemUseDuration(itemStackIn));
		}
		else {
			// Can't reload; no ammo
			if (!this.isReloading(itemStackIn)) {
				soundEmpty(itemStackIn, worldIn, playerIn);
			}
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn); */
		
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.NONE;
	}

	public void hasAmmoAndConsume(ItemStack stack, EntityPlayer player)
	{
		int ammoFinal = getAmmoQuantity(stack);
		String type = "";
		ItemStack[] inventory = player.inventory.mainInventory;
		for (int i = 0; i < inventory.length; i++) {
			ItemStack invStack = inventory[i];
			if (invStack != null)
				if (isValidProjectile(invStack.getItem())) {
					int stackSize = invStack.stackSize;
					type = invStack.getItem().getUnlocalizedName().substring(5);
					int ammo = stackSize < this.getMaxAmmo() - ammoFinal ? stackSize : this.getMaxAmmo() - ammoFinal;
					ammoFinal += ammo;

					invStack.stackSize = stackSize - ammo;
					if (invStack.stackSize < 1)
						inventory[i] = null;
					if (ammoFinal == this.getMaxAmmo())
						break;
				}
		}
		if (ammoFinal > 0) {
			setAmmoType(stack, type);
			setAmmoQuantity(stack, ammoFinal);
		}
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) {
		if(!(entityLiving instanceof EntityPlayer))return;
		EntityPlayer player = (EntityPlayer) entityLiving;
		if (canFire(stack, player)) {
			fire(stack, world, player, timeLeft);
		}
	}

	public boolean canReload(ItemStack stack, EntityPlayer player)
	{
		return getAmmoQuantity(stack) < getMaxAmmo() && !player.capabilities.isCreativeMode;
	}

	public boolean canFire(ItemStack stack, EntityPlayer player)
	{
		return (player.capabilities.isCreativeMode || isLoaded(stack, player));
	}

	public boolean hasAmmoInInventory(EntityPlayer player)
	{
		return findAvailableAmmo(player) != null;
	}

	/*
	public ItemProjectile findAvailableAmmo(EntityPlayer player)
	{
		for (ItemProjectile projectile : projectiles) {
			if (player.inventory.hasItem(projectile))
				return projectile;
		}
		return null;
	}*/

	//On top is the old Method
	public ItemProjectile findAvailableAmmo(EntityPlayer player)
	{
		for (ItemProjectile projectile : projectiles)
		{
			for(int i = 0; i < player.inventory.getSizeInventory(); i++)
			{
				ItemStack stack = player.inventory.getStackInSlot(i);
				if(this.isProjectile(stack))
				{
					return projectile;
				}
			}
		}
		return null;
	}

	protected boolean isProjectile(ItemStack stack)
	{
		return stack != null && stack.getItem() instanceof ItemProjectile;
	}

	public int getAmmoQuantityInInventory(ItemStack stack, EntityPlayer player)
	{
		InventoryPlayer inventory = player.inventory;
		String type = getAmmoType(stack);
		Item item = GameRegistry.findItem(ARKCraft.MODID, type);
		int out = 0;
		if (type != null) { // && inventory.hasItemStack(item)
			for (ItemStack s : inventory.mainInventory) {
				if (s != null && s.getItem().equals(item)) {
					out += s.stackSize;
				}
			}
		}
		return out;
	}

	public int getAmmoQuantity(ItemStack stack)
	{
		if (stack.hasTagCompound())
			return stack.getTagCompound().getInteger("ammo");
		else
			return 0;
	}

	public void setAmmoQuantity(ItemStack stack, int ammo)
	{
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setInteger("ammo", ammo);
	}

	public String getAmmoType(ItemStack stack)
	{
		String type = null;
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("ammotype"))
			type = stack.getTagCompound().getString("ammotype");
		if (type == null || type.equals(""))
			type = this.getDefaultAmmoType();
		return type.toLowerCase();
	}

	public void setAmmoType(ItemStack stack, String type)
	{
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setString("ammotype", type);
	}

	public String getDefaultAmmoType()
	{
		return this.defaultAmmoType;
	}

	public boolean isLoaded(ItemStack stack, EntityPlayer player)
	{
		return getAmmoQuantity(stack) > 0 || player.capabilities.isCreativeMode;
	}

	public void soundEmpty(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		//     world.playSoundAtEntity(entityplayer, "random.click", 1.0F, 1.0F / 0.8F);
		world.playSound(entityplayer, entityplayer.getPosition(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1.0F, 1.0F / 0.8F);
	}

	public void soundCharge(ItemStack stack, World world, EntityPlayer player)
	{
		//TODO Add Custom Sounds
		//   String name = ARKCraft.MODID + ":" + this.getUnlocalizedName() + "_reload";
		world.playSound(player, player.getPosition(), SoundEvent.REGISTRY.getObject(new ResourceLocation(ARKCraft.MODID + ":" + this.getUnlocalizedName() + "_reload")),  SoundCategory.PLAYERS, 0.7F, 0.9F / (getItemRand().nextFloat() * 0.2F + 0.0F));
	}

	public abstract int getReloadDuration();

	public void applyProjectileEnchantments(EntityProjectile entity, ItemStack itemstack)
	{
		int damage = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(49), itemstack);
		if (damage > 0) {
			entity.setDamage(damage);
		}

		int knockback = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(48), itemstack);
		if (knockback > 0) {
			entity.setKnockbackStrength(knockback);
		}

		if (EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(50), itemstack) > 0) {
			entity.setFire(100);
		}
	}

	public final void postShootingEffects(ItemStack itemstack, EntityPlayer entityplayer, World world)
	{
		effectPlayer(itemstack, entityplayer, world);
		effectShoot(entityplayer, itemstack, world, entityplayer.posX, entityplayer.posY, entityplayer.posZ, entityplayer.rotationYaw, entityplayer.rotationPitch);
	}

	public void effectPlayer(ItemStack itemstack, EntityPlayer entityplayer, World world)
	{
		float f = entityplayer.isSneaking() ? -0.01F : -0.02F;
		double d = -MathHelper.sin((entityplayer.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((0 / 180F)
				* 3.141593F) * f;
		double d1 = MathHelper.cos((entityplayer.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((0 / 180F)
				* 3.141593F) * f;
		recoilUp(entityplayer, recoil, recoilSneaking, shouldRecoil);
		entityplayer.addVelocity(d, 0, d1);
	}

	public void effectShoot(EntityPlayer p, ItemStack stack, World world, double x, double y, double z, float yaw, float pitch)
	{
		String soundPath = ARKCraft.MODID + ":" + this.getUnlocalizedName() + "_shoot";
		InventoryAttachment att = InventoryAttachment.create(stack);
		if (att != null && att.isSilencerPresent())
			soundPath = soundPath + "_silenced";
		//TODO New Sound Effect
		//	world.playSoundEffect(x, y, z, SoundEvent.REGISTRY.getObject(new ResourceLocation(soundPath), 1.5F, 1F / (this.getItemRand().nextFloat() * 0.4F + 0.7F));
		//world.playSound(entityplayer, entityplayer.getPosition(), SoundEvent.REGISTRY.getObject(new ResourceLocation(soundPath), SoundCategory.PLAYERS, 1.5F, 1F / (this.getItemRand().nextFloat() * 0.4F + 0.7F));
		//SoundUtil.playSound(world, x, y, z, new ResourceLocation(soundPath), SoundCategory.PLAYERS, 1.5F, 1F / (this.getItemRand().nextFloat() * 0.4F + 0.7F), false);

		float particleX = -MathHelper.sin(((yaw + 23) / 180F) * 3.141593F) * MathHelper.cos((pitch / 180F) * 3.141593F);
		float particleY = -MathHelper.sin((pitch / 180F) * 3.141593F) - 0.1F;
		float particleZ = MathHelper.cos(((yaw + 23) / 180F) * 3.141593F) * MathHelper.cos((pitch / 180F) * 3.141593F);

		for (int i = 0; i < 3; i++) {
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + particleX, y + particleY, z + particleZ, 0.0D, 0.0D, 0.0D);
		}
		world.spawnParticle(EnumParticleTypes.FLAME, x + particleX , y + particleY , z + particleZ , 0.0D, 0.0D, 0.0D);
	}

	public void fire(ItemStack stack, World world, EntityPlayer player, int timeLeft)
	{
		if (!world.isRemote) {
			for (int i = 0; i < getAmmoConsumption(); i++) {
				EntityProjectile p = createProjectile(stack, world, player);
				applyProjectileEnchantments(p, stack);
				if (p != null)
					world.spawnEntity(p);
			}
		}
		afterFire(stack, world, player);
	//	afterFire(stack, world, player);
	}
	
	public void fireNew(ItemStack stack, World world, EntityPlayer player)
	{
		if (canFire(stack, player)) {
		if (!world.isRemote) {
			for (int i = 0; i < getAmmoConsumption(); i++) {
				EntityProjectile p = createProjectile(stack, world, player);
				applyProjectileEnchantments(p, stack);
				if (p != null)
					world.spawnEntity(p);
			//	setFired(stack, player, true);
			}
			}
		}
	}

	public void afterFire(ItemStack stack, World world, EntityPlayer player)
	{
		if (!player.capabilities.isCreativeMode)
			this.setAmmoQuantity(stack, this.getAmmoQuantity(stack) - ammoConsumption);
		int damage = 1;
		int ammo = this.getAmmoQuantity(stack);
		if (stack.getItemDamage() + damage > stack.getMaxDamage()) {
			String type = this.getAmmoType(stack);
			Item i = GameRegistry.findItem(ARKCraft.MODID, type);
			ItemStack s = new ItemStack(i, ammo);
			player.inventory.addItemStackToInventory(s);
		}
		else if (ammo < 1) {
			if (hasAmmoInInventory(player) && FMLCommonHandler.instance().getSide().isClient()) {
				ClientEventHandler.doReload();
			}
			else {
				this.setAmmoType(stack, "");
			}
		}
		this.nextShotMillis = System.currentTimeMillis() + this.shotInterval;
		stack.damageItem(damage, player);
		postShootingEffects(stack, player, world);
	//	setFired(stack, player, true);
	}

	protected EntityProjectile createProjectile(ItemStack stack, World world, EntityPlayer player)
	{
		try {
			String type = this.getAmmoType(stack);

			Class<?> c = Class.forName("com.uberverse.arkcraft.common.entity.projectile." + ProjectileType.valueOf(type.toUpperCase()).getEntity());
			Constructor<?> con = c.getConstructor(World.class, EntityLivingBase.class, float.class, float.class, double.class, int.class);
			return (EntityProjectile) con.newInstance(world, player, this.speed, this.inaccuracy, this.damage, this.range);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		catch (SecurityException e) {
			e.printStackTrace();
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void effectReloadDone(ItemStack stack, World world, EntityPlayer player)
	{
		// player.swingItem();
	}
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
		Multimap<String, AttributeModifier> multimap = HashMultimap.create();
		this.addItemAttributeModifiers(multimap);
		return multimap;
	}

	public void addItemAttributeModifiers(Multimap<String, AttributeModifier> multimap)
	{
		multimap.put(WeaponModAttributes.RELOAD_TIME.getName(), new AttributeModifier("Weapon reloadtime modifier", this.getReloadDuration(), 0));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}

	@Override
	public int getItemEnchantability()
	{
		return 0;
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		return oldStack != null && newStack != null && oldStack.getItem() != newStack.getItem() && slotChanged;
	}
}
