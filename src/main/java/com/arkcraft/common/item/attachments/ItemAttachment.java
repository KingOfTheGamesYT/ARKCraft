package com.arkcraft.common.item.attachments;

import com.arkcraft.common.item.ARKCraftItem;

/**
 * @author Lewis_McReu
 */
public class ItemAttachment extends ARKCraftItem {
	private final AttachmentType type;

	public ItemAttachment(AttachmentType type) {
		super();
		this.type = type;
	}

	public AttachmentType getType() {
		return this.type;
	}
}
