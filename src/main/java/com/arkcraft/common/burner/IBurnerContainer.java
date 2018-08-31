package com.arkcraft.common.burner;

public interface IBurnerContainer
{
	public default void toggleBurning()
	{
		getBurner().updateIsBurning(!getBurner().isBurning());
	}

	public IBurner getBurner();
}
