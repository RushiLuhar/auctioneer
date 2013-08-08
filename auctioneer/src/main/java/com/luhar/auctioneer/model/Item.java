package com.luhar.auctioneer.model;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.Vector;

import com.google.common.collect.TreeMultiset;

public class Item extends AuctioneerObject {
    private final String name;
    private String description = "";
    // By default the reserve amount is set to zero
    private BigDecimal reserveAmount = new BigDecimal(0);
    
    // We use a TreeMultiSet of all bids to keep track of the first 
    private TreeMultiset<Bid> bids = TreeMultiset.create(new Comparator<Bid>() {
	public int compare(Bid o1, Bid o2) {
	    // Note that that we need to keep track of the highest bid, not the lowest bid
	    return o1.compareTo(o2);
	}
    });
    
    public Item(String name) {
	super();
	this.name = name;
    }
    
    public Item(String name, String description) {
	this(name);
	this.description = description;
    }
    
    public Item(String name, String description, BigDecimal reserveAmount) {
	this(name, description);
	this.reserveAmount = reserveAmount;
    }
    
    // Human readable name for this item
    public String getItemName() {
	return this.name;
    }
    
    // Human readable description for this item
    public String getDescription() {
	return this.description;
    }
    
    // Assuming single currency amount
    public BigDecimal getReserveAmount() {
	return this.reserveAmount;
    }
    
    // Get list of Bids that have been made on this item
    public Collection<Bid> getBids() {
	return this.bids;
    }
    
    // Get list of Users who have made Bids on this item
    public Collection<User> getUsers() {
	Vector<User> users = new Vector<User>();
	for (Bid bid : bids) {
	    if (!users.contains(bid.getUser())) {
		users.add(bid.getUser());
	    }
	}
	return users;
    }
    
    // Get Bid for a user
    public Bid getBidFromUser(User user) {
	for (Bid bid : bids) {
	    if (bid.getUser().equals(user)) {
		return bid;
	    }
	}
	return null;
    }
    
    // Add bid to this item
    public void addBid(Bid bid) {
	if (!bids.contains(bid)) {
	    logger.info("Adding bid: " + bid.toString() + " to item: " + toString());
	    bids.add(bid);
	} else {
	    logger.warn("Duplicate bid: " + bid.toString());
	}
    }
    
    public Bid getWinningBid() {
	return bids.firstEntry().getElement();
    }
    
    
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("Item ID: ").append(getID().toString()).append("  ")
		.append("Item name: ").append(this.name).append("  ")
		.append("Item description: ").append(this.description).append("  ")
		.append("Item reserve amount: ").append(this.reserveAmount.toPlainString());
	
	return builder.toString();
    }
    

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((bids == null) ? 0 : bids.hashCode());
	result = prime * result
		+ ((description == null) ? 0 : description.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result
		+ ((reserveAmount == null) ? 0 : reserveAmount.hashCode());
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
	Item other = (Item) obj;
	if (bids == null) {
	    if (other.bids != null)
		return false;
	} else if (!bids.equals(other.bids))
	    return false;
	if (description == null) {
	    if (other.description != null)
		return false;
	} else if (!description.equals(other.description))
	    return false;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	if (reserveAmount == null) {
	    if (other.reserveAmount != null)
		return false;
	} else if (!reserveAmount.equals(other.reserveAmount))
	    return false;
	return true;
    }

    
    
    
}
