package edu.uwm.cs361;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DatastoreTest {

    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void setUp() {
    	
        helper.setUp();
    }

    @After
    public void tearDown() {
    	
        helper.tearDown();
    }

    @Test
    public void testAddUser() {
    	
    	DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    	
        assertEquals(0, ds.prepare(new Query("add")).countEntities(withLimit(10)));
        
        ds.put(new Entity("add"));
        
        ds.put(new Entity("add"));
        
        assertEquals(2, ds.prepare(new Query("add")).countEntities(withLimit(10)));
    }

    @Test
    public void testUpdateUser() throws EntityNotFoundException {
        
    	DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    	
    	Entity update = new Entity("update");
    	
    	update.setProperty("Param1", 100);
    	
    	ds.put(update);
    	
    	assertEquals(Long.parseLong("100"), ds.get(KeyFactory.createKey("update", update.getKey().getId())).getProperty("Param1"));
    	
    	update.setProperty("Param1", 1000);
    	
    	ds.put(update);
    	
    	assertEquals(Long.parseLong("1000"), ds.get(KeyFactory.createKey("update", update.getKey().getId())).getProperty("Param1"));
    }
    
    @Test
    public void addAdmin() {
    	
    	Datastore.addAdmin();
    	
    	assertEquals(1, Datastore.getUsers(null));
    }
}