package jrl;

import java.util.ArrayList;

public class MyInteger extends Thing{
	int value;
	public MyInteger(int val) {
		isList=false;
		value=val;
	}
	public void textOutput() {
		System.out.println("isList="+isList+", value="+value);
	}
	public ArrayList<Thing> getList(){ // might be a possible simplification here with the convertToList option
		return null;
	}
	public int compare (Thing thing) {
		if(value==thing.getValue()) {
			return 0;
		}
		else if(value>thing.getValue()) return -1;
		else return 1;
	}
	
	public Thing convertToList() {
		String s="["+value+"]";
		return new MyList(s);
	}
	public int compareLists(Thing thing) { return 0;}
	public int getValue() {return value;}
	public String getInputData() {return Integer.toString(value);}
	
}
