package com.uberverse.arkcraft.util;

import io.netty.buffer.ByteBuf;

public interface Bytable
{
	public void write(ByteBuf buf);
	
	public void read(ByteBuf buf);
}
