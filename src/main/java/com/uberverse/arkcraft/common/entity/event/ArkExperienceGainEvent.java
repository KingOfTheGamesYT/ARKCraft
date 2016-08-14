package com.uberverse.arkcraft.common.entity.event;

import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class ArkExperienceGainEvent extends EntityEvent
{
	private long xp;

	public ArkExperienceGainEvent(Entity entity, long xp)
	{
		super(entity);
		this.xp = xp;
	}
	
	public long getXp()
	{
		return xp;
	}
	
	public void setXp(long xp)
	{
		this.xp = xp;
	}
}
