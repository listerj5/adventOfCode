package jrl;

import java.util.ArrayList;

public abstract class Thing {
	boolean isList;
	
	// Thing can be either an integer or a list
	public abstract void textOutput();
	
	public abstract int compare(Thing thing);
	
	public abstract ArrayList<Thing> getList();
	
	public abstract int compareLists(Thing thing);
	
	public abstract Thing convertToList();
	public abstract int getValue();
	public abstract String getInputData();
	
	
}
