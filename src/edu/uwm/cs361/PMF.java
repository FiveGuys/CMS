package edu.uwm.cs361;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;


/**
 * This class creates an instance of a Persistence Manager Factory.
 * @author 5guys
 */
public final class PMF {
	private static final PersistenceManagerFactory pmfInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	private PMF() {}

    public static PersistenceManagerFactory get() {
        return pmfInstance;
    }
}
