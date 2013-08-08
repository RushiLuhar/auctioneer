package com.luhar.auctioneer.model;

import java.util.UUID;

import org.apache.log4j.Logger;

public abstract class AuctioneerObject {
    private final UUID id;
    static final Logger logger = Logger.getLogger(BidTracker.class);
    
    public AuctioneerObject() {
	this.id = UUID.randomUUID();
    }
    
    public UUID getID() {
	return this.id;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	AuctioneerObject other = (AuctioneerObject) obj;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	return true;
    }
    
    
}
