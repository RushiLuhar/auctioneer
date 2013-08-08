package com.luhar.auctioneer.model;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;


public class User extends AuctioneerObject {
    private final String name;
    private Collection<Bid> bids = new Vector<Bid>();
    private Collection<Item> items = new Vector<Item>();
    
    public User(String name) {
	super();
	this.name = name;
    }
    
    // Get human readable short name for the user
    public String getName() {
	return this.name;
    }
    
    // Get list of Bids made by this user
    public Collection<Bid> getBids() {
	return this.bids;
    }
    
    // Get list of Items bid on by this user
    public Collection<Item> getItems() {
	return this.items;
    }
    
    public Bid makeBid(Item item, BigDecimal amount) {
	Bid newBid = new Bid(item, this, amount);
	this.bids.add(newBid);
	// Check and log if there are any bids already for this item
	if (items.contains(item)) {
	    AuctioneerObject.logger.warn(String.format("User %s has already bid on Item %s", this.toString(), item.toString()));
	} else {
	    items.add(item);
	}
	return newBid;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((bids == null) ? 0 : bids.hashCode());
	result = prime * result + ((items == null) ? 0 : items.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (!super.equals(obj))
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	User other = (User) obj;
	if (bids == null) {
	    if (other.bids != null)
		return false;
	} else if (!bids.equals(other.bids))
	    return false;
	if (items == null) {
	    if (other.items != null)
		return false;
	} else if (!items.equals(other.items))
	    return false;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	return true;
    }
    
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("User ID: ").append(getID().toString()).append("  ")
		.append("User name: ").append(getName());
	return builder.toString();
    }
    
    
}
