package edu.uwm.cs361;

import java.io.IOException;

/**
 * Callback interface
 * @author 5guys
 */
public interface CallBack {
	
	public static final int ACCESS_ALL = 1;
	
	public static final int ACCESS_LIMITED = 2;
	
	public static final int ACCESS_ADMIN = 3;
	
	public void printContent() throws IOException;
	
	public void validate();
}
