package jrl;

public class Item {
	int priority;
	int[] remainder=new int[AdventDay11.maxDivider+1];
	public Item(int priority) {
		this.priority=priority;
		int i;
		for(i=1;i<remainder.length;i++) { // this is for part 2 - each item needs to keep track of what the remainder is for a given divider / denominator
			remainder[i]=priority%i;
		}
	}
	public boolean isDivisibleBy(int num) {
		if(AdventDay11.part1) return (priority%num==0); // part 1 logic
		else return remainder[num]==0; // part 2 logic
	}
	public void updateMultiply(int num) {
		if(AdventDay11.part1) priority*=num; // simple logic for part 1 - multiply the priority
		else {
			int i;
			for(i=1;i<remainder.length;i++) { // for part 2, need to update the remainders 
				remainder[i]*=num; // need to multiply the remainders and reapply modulo to remainder
				remainder[i]=remainder[i]%i;
			}
		}
	}
	public void updateSquare() {
		if(AdventDay11.part1) priority*=priority; // part 1 logic
		else {
			int i;
			for(i=1;i<remainder.length;i++) { // for part 2 need to update the remainders
				remainder[i]*=remainder[i]; // square each of the remainders and reapply modulo to remainder
				remainder[i]=remainder[i]%i;
			}
		}
	}
	public void updateAdd(int num) {
		if(AdventDay11.part1) priority+=num; // part 1 logic
		else {						
			int i;
			for(i=1;i<remainder.length;i++) { // for part 2 need to update the remainders
				remainder[i]+=num; // add the value to each and reapply the modulo function to remainder
				remainder[i]=remainder[i]%i;
			}
		}
	}
	public void updatePriority(int priority) {
		this.priority=priority;
	}
	public int getPriority() {
		return priority;
	}
}
