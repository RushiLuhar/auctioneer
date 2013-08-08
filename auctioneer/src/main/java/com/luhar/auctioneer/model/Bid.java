package com.luhar.auctioneer.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Bid extends AuctioneerObject implements Comparable<Bid> {
    
    private final Item item;
    private final User user;
    private BigDecimal bidAmount;
    // Time that this bid was last updated
    private Date lastUpdated;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss.SSS z");
    
    public Bid(Item item, User user, BigDecimal bidAmount) {
	super();
	this.item = item;
	this.user = user;
	this.bidAmount = bidAmount;  
	this.lastUpdated = new Date();
    }
    
    // Get the Item for this bid
    public Item getItem() {
	return this.item;
    }
    
    // Get the user who made this bid
    public User getUser() {
	return this.user;
    }
    
    // Assuming all bids are made for a single currency
    public BigDecimal getBidAmount() {
	return this.bidAmount;
    }
    
    // Assume that the bidAmount can be changed in an auction
    public void setBidAmount(BigDecimal newAmount) {
	this.bidAmount = newAmount;
	this.lastUpdated = new Date();
    }
    
    public Date getLastUpdate() {
	return this.lastUpdated;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result
		+ ((bidAmount == null) ? 0 : bidAmount.hashCode());
	result = prime * result + ((item == null) ? 0 : item.hashCode());
	result = prime * result
		+ ((lastUpdated == null) ? 0 : lastUpdated.hashCode());
	result = prime
		* result
		+ ((simpleDateFormat == null) ? 0 : simpleDateFormat.hashCode());
	result = prime * result + ((user == null) ? 0 : user.hashCode());
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
	Bid other = (Bid) obj;
	if (bidAmount == null) {
	    if (other.bidAmount != null)
		return false;
	} else if (!bidAmount.equals(other.bidAmount))
	    return false;
	if (item == null) {
	    if (other.item != null)
		return false;
	} else if (!item.equals(other.item))
	    return false;
	if (lastUpdated == null) {
	    if (other.lastUpdated != null)
		return false;
	} else if (!lastUpdated.equals(other.lastUpdated))
	    return false;
	if (simpleDateFormat == null) {
	    if (other.simpleDateFormat != null)
		return false;
	} else if (!simpleDateFormat.equals(other.simpleDateFormat))
	    return false;
	if (user == null) {
	    if (other.user != null)
		return false;
	} else if (!user.equals(other.user))
	    return false;
	return true;
    }

    public String toString() {
	StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append("Bid id: ").append(getID().toString()).append("  Amount: ").append(bidAmount.toPlainString())
		.append("User: ").append(user.toString()).append("\n")
		.append("Item: ").append(item.toString()).append("\n")
		.append("Bid Updated at: ").append(simpleDateFormat.format(lastUpdated));
	return stringBuilder.toString();
    }

    public int compareTo(Bid otherBid) {
	// Note that we need to compare to get the highest bid first. If the bidAmounts are equal, return the
	// one with the higher timestamp
	if (otherBid.getBidAmount().compareTo(getBidAmount()) == 0) {
	    if (otherBid.getLastUpdate().getTime() > getLastUpdate().getTime()) {
		// Other bid is newer
		return -1;
	    } else if (otherBid.getLastUpdate().getTime() == getLastUpdate().getTime()) {
		// Identical bid
		return 0;
	    }
	    else {
		return 1;
	    }
	}
	
	return otherBid.getBidAmount().compareTo(bidAmount);
    }
}
