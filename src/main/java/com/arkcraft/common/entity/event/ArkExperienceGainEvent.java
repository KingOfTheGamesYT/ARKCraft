package com.arkcraft.common.entity.event;

import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class ArkExperienceGainEvent extends EntityEvent
{
	private double xp;

	public ArkExperienceGainEvent(Entity entity, double xp)
	{
		super(entity);
		this.xp = xp;
	}

	public double getXp()
	{
		return xp;
	}

	public void setXp(double xp)
	{
		this.xp = xp;
	}
}
