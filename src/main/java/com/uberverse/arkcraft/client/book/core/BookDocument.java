package com.uberverse.arkcraft.client.book.core;

import com.uberverse.arkcraft.client.book.lib.Page;

/**
 * 
 * @author Archiving
 * This class is used as a base wrapper for all pages. Imagine this being the 'book' and the Page[] being all the pages inside the book.
 * 
 */
public class BookDocument {

	public Page[] entries;
	
	public Page[] getEntries() { return entries; }
	public void setEntries(Page[] entries) { this.entries = entries; }
	
}
