package com.luhar.auctioneer.impl;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.luhar.auctioneer.model.Bid;
import com.luhar.auctioneer.model.BidTracker;
import com.luhar.auctioneer.model.Item;
import com.luhar.auctioneer.model.User;

public class BidTrackerImplTest {
    
    static {
	BasicConfigurator.configure();
    }
    
    static final Logger logger = Logger.getLogger(BidTracker.class);
    
    // Some users
    User alice;
    User bob;
    User charlie;
    
    // Some items
    Item foo;
    Item bar;
    Item baz;
    
    BidTracker tracker;
    
    void resetUsers() {
	alice = new User("Alice");
	bob = new User("Bob");
	charlie = new User("Charlie");
    }
    
    void resetItems() {
	foo = new Item("Foo","Finest Foo", new BigDecimal(10));
	bar = new Item("Bar","Boring Bar");
	baz = new Item("Baz");
    }
    
    void resetTracker() {
	tracker = new BidTrackerImpl();
    }
    
    void reset() {
	resetTracker();
	resetUsers();
	resetItems();
	tracker.registerItem(foo);
	tracker.registerItem(bar);
	tracker.registerItem(baz);
	tracker.registerUser(alice);
	tracker.registerUser(bob);
	tracker.registerUser(charlie);
    }
    

    @Test
    public void testRegisterUser() {
	resetUsers();
	resetTracker();
	tracker.registerUser(alice);
	assertTrue(tracker.getAllRegisteredUsers().contains(alice));
    }

    @Test
    public void testRegisterItem() {
	resetItems();
	resetTracker();
	tracker.registerItem(foo);
	assertTrue(tracker.getAllRegisteredItems().contains(foo));
    }

    @Test
    public void testRegisterBid() {
	reset();
	Bid bid = tracker.registerBid(foo, alice, new BigDecimal(11));
	assertNotNull(bid);
	assertTrue(alice.getItems().contains(foo));
	assertTrue(foo.getUsers().contains(alice));
	logger.info("#### All bids for item foo: " + tracker.getAllBidsForItem(foo));
	assertTrue(tracker.getAllBidsForItem(foo).contains(bid));
	assertTrue(tracker.getAllItemsForUser(alice).contains(foo));
    }

    @Test
    public void testGetWinningBid() {
	reset();
	// Let us have two bids against with different values. Both bids below the reserve amount
	Bid bid1 = tracker.registerBid(foo, alice, new BigDecimal(9));
	Bid bid2 = tracker.registerBid(foo, bob, new BigDecimal(8));
	assertNull(bid1);
	assertNull(bid2);
	assertTrue(foo.getBids().isEmpty());
	
	// Lets have two bids, both above reserve amount with different values
	Bid bid3 = tracker.registerBid(foo, alice, new BigDecimal(11));
	Bid bid4 = tracker.registerBid(foo, bob, new BigDecimal(12));
	assertTrue(foo.getWinningBid().equals(bid4));
	
	// Lets have two bids, identical amounts, but one bid made after the other. Thread.sleep is used to model the delay
	Bid bid5 = tracker.registerBid(bar, bob, new BigDecimal(5));
	try { Thread.sleep(1); } catch (Exception ex) {}
	Bid bid6 = tracker.registerBid(bar, charlie, new BigDecimal(5));
	assertTrue(bar.getWinningBid().equals(bid5));
	assertTrue(bar.getBids().size() == 2);
    }

    @Test
    public void testGetAllBidsForItem() {
	reset();
	// Register three bids on item. One is below the reserve price
	Bid bid1 = tracker.registerBid(foo, alice, new BigDecimal(9));
	Bid bid2 = tracker.registerBid(foo, bob, new BigDecimal(10));
	Bid bid3 = tracker.registerBid(foo, charlie, new BigDecimal(11));
	assertTrue(foo.getBids().size() == 2);
	assertTrue(foo.getWinningBid().equals(bid3));
	assertTrue(foo.getBids().contains(bid2));
	assertTrue(foo.getBids().contains(bid3));
	assertFalse(foo.getBids().contains(bid1));
    }

    @Test
    public void testGetAllItemsForUser() {
	reset();
	// Register three bids on the three items for one user.
	Bid bid1 = tracker.registerBid(foo, alice, new BigDecimal(10));
	Bid bid2 = tracker.registerBid(bar, alice, new BigDecimal(11));
	Bid bid3 = tracker.registerBid(baz, alice, new BigDecimal(12));
	
	assertTrue(alice.getItems().size() == 3);
	assertTrue(alice.getItems().contains(foo));
	assertTrue(alice.getItems().contains(bar));
	assertTrue(alice.getItems().contains(baz));
    }

}
