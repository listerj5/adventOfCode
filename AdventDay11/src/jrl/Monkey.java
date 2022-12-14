package jrl;

import java.util.ArrayList;

public class Monkey {
	static int m_Id=0;
	int id;
	ArrayList<Item> items=new ArrayList();
	int divisibleBy;
	Inspect inspect;
	Monkey target1;
	Monkey target2;
	int inspectionCount=0;
	public Monkey (int[] itemList, Inspect inspect, int divideTest) {
		id=m_Id; // individual monkey numbering - start at zero
		m_Id++; // increment ready for next monkey creation
		this.inspect=inspect;
		for(int i:itemList) {
			items.add(new Item(i));
		}
		divisibleBy=divideTest;
	}
	public void setTargets(Monkey monkey1, Monkey monkey2) {
		target1=monkey1;
		target2=monkey2;
	}
	public Monkey decideTarget(Item i) {
		if(i.isDivisibleBy(divisibleBy)) return target1; else return target2;
	}
	public void inspectItems() {
		
		while(items.size()>0) {
			Item i=items.get(0);
			inspectionCount++;
			inspect.CheckItem(i);
			if(AdventDay11.part1) i.updatePriority(i.getPriority()/3); // get bored // part 1 logic 
			throwItem(i,decideTarget(i));
		}
	}
	public void catchItem(Item i) {
		items.add(i);
	}
	public void throwItem(Item i, Monkey target) {
		items.remove(i);
		target.catchItem(i);
	}
	public int getInspectionCount() {return inspectionCount;}
	public int getMonkeyId() {return id;}
}
