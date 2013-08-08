package com.luhar.auctioneer.impl;

import java.math.BigDecimal;
import java.util.Collection;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.luhar.auctioneer.model.Bid;
import com.luhar.auctioneer.model.BidTracker;
import com.luhar.auctioneer.model.Item;
import com.luhar.auctioneer.model.User;
import com.luhar.auctioneer.utils.ObjectCache;

public class BidTrackerImpl implements BidTracker {
    private static final Logger logger = Logger.getLogger(BidTracker.class);
    private ObjectCache<Item> items = new ObjectCache<Item>();
    private ObjectCache<User> users = new ObjectCache<User>();
    private ObjectCache<Bid> bids = new ObjectCache<Bid>();
    
    
    public boolean registerUser(User user) {
	if (users.containsItem(user)) { 
	    logger.warn("Attempting to register duplicate user: " + user.toString());
	    return false;
	}
	
	logger.info("Registering user: " + user.toString());
	users.addItem(user);
	return true;
    }
    
    public boolean registerItem(Item item) {
	if (items.containsItem(item)) {
	    logger.warn("Attempting to register duplicate item: " + item.toString());
	    return false;
	}
	
	logger.info("Registering item: " + item.toString());
	items.addItem(item);
	return true;
    }
    
    public Bid registerBid(Item item, User user, BigDecimal amount) {
	boolean itemCheck = items.containsItem(item);
	boolean userCheck = users.containsItem(user);
	// Bid amount has to be non-zero and greater than the reserve amount
	boolean amountCheck = amount.compareTo(BigDecimal.ZERO) > 0 && amount.compareTo(item.getReserveAmount()) >= 0;
	if (itemCheck && userCheck && amountCheck) {
	    // First check if the item already has a bid by this user. If it has, simply modify the bid amount
	    Bid existingBid = item.getBidFromUser(user);
	    if (existingBid != null) {
		logger.info("Item: " + item.toString() + " already contains a bid from user: " + user.toString());
		if (existingBid.getBidAmount().compareTo(amount) != 0) {
		    // Modify the bid amount
		    logger.info("Modifying amount for existing Bid: " + existingBid.toString() + " to: " + amount.toPlainString());
		    existingBid.setBidAmount(amount);
		    item.bidModified(existingBid);
		    return existingBid;
		}
	    }
	    // Get the user to make a new bid
	    Bid newBid = user.makeBid(item, amount);
	    item.addBid(newBid);
	    bids.addItem(newBid);
	    logger.info(String.format("Registered new bid: %s for item: %s by user: %s", newBid.toString(), item.toString(), user.toString()));
	    return newBid;
	} 
	
	if (!itemCheck) { logger.warn("Reject bid because of invalid item: " + item.toString()); }
	if (!userCheck) { logger.warn("Reject bid because of invalid user: " + user.toString()); }
	if (!amountCheck) { logger.warn("Reject bid because of invalid amount: " + amount.toPlainString()); }
	return null;
    }


    public Bid getWinningBid(Item item) {
	if (item.getBids().size() > 0) {
	   return item.getWinningBid();
	} else {
	    logger.info("No bids recorded for item: " + item.toString());
	    return null;
	}
    }

    public Collection<Bid> getAllBidsForItem(Item item) {
	return item.getBids();
    }

    public Collection<Item> getAllItemsForUser(User user) {
	return user.getItems();
    }
    
    public Collection<User> getAllRegisteredUsers() {
	return users.getAllItems();
    }
    
    public Collection<Item> getAllRegisteredItems() {
	return items.getAllItems();
    }
    
    public static void main(String[] args) {
	BasicConfigurator.configure();
	// Some users
	User alice = new User("Alice");
	User bob = new User("Bob");
	User charlie = new User("Charlie");
	
	// Some items
	Item foo = new Item("Foo","Finest Foo", new BigDecimal(10));
	Item bar = new Item("Bar","Boring Bar");
	Item baz = new Item("Baz");
	
	// Create BidTracker
	BidTracker bidTracker = new BidTrackerImpl();
	bidTracker.registerUser(alice);
	bidTracker.registerUser(bob);
	bidTracker.registerUser(charlie);
	bidTracker.registerItem(foo);
	bidTracker.registerItem(bar);
	bidTracker.registerItem(baz);
	
	// Alice and Bob bid on foo
	bidTracker.registerBid(foo,alice,new BigDecimal(9));
	bidTracker.registerBid(foo, bob, new BigDecimal(11));
	// Alice's bid is lower than the reserve and will be rejected. Bob's bid should be the winning bid
	logger.info("All bids on item foo: " + foo.getBids().toString());
	logger.info("Winning bid on item foo: " + foo.getWinningBid().toString());
	
	// Alice, bob and charlie all bid on bar. Charlie bids last. Thread.sleep added to simulate last bid
	bidTracker.registerBid(bar, alice, new BigDecimal(5));
	bidTracker.registerBid(bar, bob, new BigDecimal(6));
	try { Thread.sleep(1); } catch (Exception ex) {}
	bidTracker.registerBid(bar, charlie, new BigDecimal(6));
	// Although Charlie and Bob's bids are identical, Bob bid first and should win the auction
	logger.info("Winning bid on bar is: " + bar.getWinningBid().toString());
	
	// Bob has successfully bid for both items foo and bar. They should be displayed.
	logger.info("Items bid for by Bob: " + bob.getItems().toString());
	
	// Alice, Bob and Charlie have all bid for item bar. They should be displayed
	logger.info("Users bidding on item bar: " + bar.getUsers().toString());
	
	// There are no bids recorded on item baz. Should return null
	logger.info("Bids recorded on item baz: " + baz.getBids().toString());
    }

}
