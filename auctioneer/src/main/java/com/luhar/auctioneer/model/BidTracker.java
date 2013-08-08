package com.luhar.auctioneer.model;

import java.math.BigDecimal;
import java.util.Collection;

public interface BidTracker {
    
    // Register a new user
    public boolean registerUser(User user);
    
    // Register a new item
    public boolean registerItem(Item item);
    
    // Record a user's bid on an item. Returns true on a successful bid
    public Bid registerBid(Item item, User user, BigDecimal amount);
    
    // Get the current winning bid for an item
    public Bid getWinningBid(Item item);
    
    // Get all the bids for an item
    public Collection<Bid> getAllBidsForItem(Item item);
    
    // Get all the items on which a user has bid
    public Collection<Item> getAllItemsForUser(User user);
    
    // Get all known users
    public Collection<User> getAllRegisteredUsers();
    
    // Get all registered items
    public Collection<Item> getAllRegisteredItems();
}
