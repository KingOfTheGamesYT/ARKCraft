package com.uberverse.arkcraft.common.item.attachments;

import com.uberverse.arkcraft.common.item.ARKCraftItem;

/**
 * @author Lewis_McReu
 *
 */
public class ItemAttachment extends ARKCraftItem
{
	private final AttachmentType type;

	public ItemAttachment(String name, AttachmentType type)
	{
		super(name);
		this.type = type;
	}

	public AttachmentType getType()
	{
		return this.type;
	}
}
