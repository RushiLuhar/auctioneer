package com.luhar.auctioneer.utils;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.luhar.auctioneer.model.AuctioneerObject;

// Simple object cache for storing Auctioneer items
public class ObjectCache<T extends AuctioneerObject> {
    private final Map<UUID, T> cache = new ConcurrentHashMap<UUID, T>();
    
    public void addItem(T item) {
	cache.put(item.getID(), item);
    }
    
    public T getItem(UUID itemID) {
	return cache.get(itemID);
    }
    
    public boolean containsItem(T item) {
	return !(getItem(item.getID()) == null);
    }
    
    public Collection<T> getAllItems() {
	return cache.values();
    }
    
}
