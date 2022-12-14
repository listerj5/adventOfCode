package jrl;

import java.util.ArrayList;

public class AdventDay11 {
	//monkey real data - comment this out and uncomment the test data to test with test data
	static int[][] itemData=new int[][] {{65,58,93,57,66},{76, 97, 58, 72, 57, 92, 82},{90, 89, 96},{72, 63, 72, 99},{65},{97,71},{83, 68, 88, 55, 87, 67},{64, 81, 50, 96, 82, 53, 62, 92}};
	static Inspect[] inspect=new Inspect[] {new InspectMultiply(7), new InspectAdd(4), new InspectMultiply(5), new InspectSquare(), new InspectAdd(1), new InspectAdd(8), new InspectAdd(2), new InspectAdd(5)};  
	static int[] divideNums=new int[] {19,3,13,17,2,11,5,7};
	static int[][] targets=new int[][] {{6,4},{7,5},{5,1},{0,4},{6,2},{7,3},{2,1},{3,0}};
	
	//monkey test data
	//static int[][] itemData=new int[][] {{79,98},{54,65,75,74},{79,60,97},{74}};
	//static Inspect[] inspect=new Inspect[] {new InspectMultiply(19), new InspectAdd(6), new InspectSquare(), new InspectAdd(3) }; 
	//static int[] divideNums=new int[] {23,19,13,17};
	//static int[][] targets=new int[][] {{2,3},{2,0},{1,3},{0,1}};
	
	static int maxDivider=3; // this is used for part 2 to know how many entries to maintain remainders for (for each item)
	static boolean part1=false;
	
	public static void main(String[] args) {
		ArrayList <Monkey> monkeyList=new ArrayList();
		for(int i:divideNums) {
			if(i>maxDivider) maxDivider=i;
		}
		int x;
		for(x=0;x<inspect.length;x++) {
			Monkey m=new Monkey(itemData[x],inspect[x],divideNums[x]);
			monkeyList.add(m);
		}
		for(x=0;x<inspect.length;x++) {
			monkeyList.get(x).setTargets(monkeyList.get(targets[x][0]), monkeyList.get(targets[x][1]));
		}
		
		int numCycles;
		if(part1) numCycles=20;
		else numCycles=10000;
		
		for(x=0;x<numCycles;x++) {
			int n;
			for(n=0;n<monkeyList.size();n++) {
				Monkey m=monkeyList.get(n);
				m.inspectItems();
			}
		}
		for(Monkey m:monkeyList) {
			System.out.println("monkey "+m.getMonkeyId()+" inspected item "+m.getInspectionCount()+" times");
		}

	}

}
