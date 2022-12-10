package jrl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AdventDay10 {
	static BufferedReader in;    
	static String inputFileLocation="C:/users/701583673/adventDay10.txt";
	public static void main(String[] args) {
		String nextline;
		int X=1; 										// register value
		int[] pt1_out=new int[]{20,60,100,140,180,220}; // values of interest for part 1 puzzle
		int val; 										// the value in the instruction to add / subtract
		ArrayList<Integer> Xregister=new ArrayList();	// arraylist to store register values for each cycle
		Xregister.add(1);								// starting value cycle1 in register (note this will appear at arraylist.get(0))
		ArrayList<String> instructions=new ArrayList(); // store instructions in here
		try {
			in=new BufferedReader(new FileReader(inputFileLocation));
			while((nextline=in.readLine())!=null) {
	            instructions.add(nextline); // read in the instruction dataset
	        }
			
		} catch (IOException ex) {
			
		}
		for(String s:instructions) {
			if(s.equals("noop")) {
				Xregister.add(X); // value remains same for noop command
			}
			else {
				val=Integer.parseInt(s.substring(5));
				Xregister.add(X); // value remains same for first cycle of add command
				X+=val;
				Xregister.add(X); 
			}
		}
		int i,j; // two variables to do for-next (requires nested loop for part 2 rows / columns of output)
		int total=0;
		
		for(i=0;i<pt1_out.length;i++) {
			total+=pt1_out[i]*Xregister.get(pt1_out[i]-1); // arraylist has cycle 1 start value at entry zero, so subtract 1 from index
		}
		System.out.println("part 1 total="+total);
		
		// part 2 - display output
		String[] display=new String[6]; // 6 lines on CRT
		for (i=0;i<display.length;i++) {
			display[i]=""; // initialise string
			for(j=0;j<40;j++) { // each CRT line is 40 characters (0-39)
				val=Xregister.get(j+(i*40)); // locate appropriate register value (both arraylist and CRT out start at zero)
				if(j>=val-1 && j<=val+1) {
					display[i]+="#";
				} else {
					display[i]+=".";
				}
			}
			System.out.println(display[i]);
		}
	}
	static void debugOut(int x, int cycle) {
		System.out.println("X val="+x+", cycle="+cycle);
	}
}
