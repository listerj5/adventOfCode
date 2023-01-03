package jrl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AdventDay20 {
	
	// this puzzle is a bit of a mess
	
	static class WrappedInt{ // for part 1
		int value;
		public WrappedInt(int value) {
			this.value=value;
		}
	}
	static class WrappedLong{ // for part 2
		long value;
		public WrappedLong(int value) {
			this.value=value*811589153L; // all of the values will be greater or less than the array size except the zero one
			// so they will all need to be modulused by the arraySize-1
		}
	}
	
	
	static BufferedReader in;    
	static String inputFileLocation="C:/users/701583673/adventDay20.txt";
	public static void main(String[] args) {
		String nextline;
		WrappedInt zeroValue=null;
		WrappedLong zeroLong=null;
		ArrayList<WrappedInt> start=new ArrayList<WrappedInt>();
		ArrayList<WrappedInt> shuffled=new ArrayList<WrappedInt>();
		
		ArrayList<WrappedLong> startL=new ArrayList<WrappedLong>();
		ArrayList<WrappedLong> shuffledL=new ArrayList<WrappedLong>();
		
		try {
			in=new BufferedReader(new FileReader(inputFileLocation));
			while((nextline=in.readLine())!=null) {
	            WrappedInt w=new WrappedInt(Integer.parseInt(nextline));
	            WrappedLong l=new WrappedLong(Integer.parseInt(nextline));
	            if(w.value==0) zeroValue=w;
	            if(l.value==0L) zeroLong=l;
	            start.add(w);
	            shuffled.add(w);
	            startL.add(l);
	            shuffledL.add(l);
	            
	        }
			
		} catch (IOException ex) {
			
		}
	
		int[] testData={1,2,-3,3,-2,0,4}; // for testing
		
		//1, 2, -3, 4, 0, 3, -2
		
		// zero is at index 4, size is 7, so moving 1000 means 1000/7 = 142 loops with 6 remainder, the 6plus the 4 index=10, which means -7 size =3 which is "4"
		// 0,20,2,3,4,5,6		
		// moving every size()-1 takes it back to where it started
		/*
		for(int i:testData) {
			WrappedInt w=new WrappedInt(i);
			WrappedLong l=new WrappedLong(i);
			start.add(w);
			shuffled.add(w);
			startL.add(l);
			shuffledL.add(l);
			if(i==0) {
				zeroValue=w;
				zeroLong=l;
			}
		}*/
		
		int loc=0;
		for(WrappedInt n:start) {
			loc=shuffled.indexOf(n);
			
			if(n.value==0) {
				// do nothing
			}
			else if(loc+n.value>=shuffled.size()) { // could loop around multiple times
				// this has gone off the end
				// say initial value is -2000 (say location 1000, value -3000) and size is 5000
				// old logic would add 4999 to get 2999 as where the value needs to go
				// for round numbers (to help modulo, say size is 5001, then -2000 + 5000 =3000
				// if we did modulo -2000%5000 though we would get -2000, so we need to do modulo and then add another array.size to get to positive
				// same if -7000%5000
				// say -12000%5000 - still -2000, then add 5000 to get answer
				// for positive numbers can use same logic but don't need to add the final [size]
				int target=(loc+n.value)%(shuffled.size()-1); // updated code - use modulo as above to allow for multiple loops (also works for single loops)
				shuffled.remove(n);
				shuffled.add(target,n);
				
			} else if(loc+n.value==0) {
				// this needs to be removed, then added to the end of the list
				shuffled.remove(n);
				shuffled.add(n);
				
			} else if (loc+n.value<0) { // could loop around multiple times
				// this has gone off the beginning
				
				int target=(loc+n.value)%(shuffled.size()-1);
				target+=shuffled.size()-1;
								 
			
				shuffled.remove(n);
				shuffled.add(target,n);
				
			} else {			
				shuffled.remove(n);
				shuffled.add(loc+n.value,n);
			}
		}
		int[] pt1_answer=new int[3];
		loc=shuffled.indexOf(zeroValue);
		System.out.println("zero value is at "+loc);
		
		int num=(loc+1000)%shuffled.size();
		System.out.println("first value is at "+num);
		System.out.println("first value is "+(shuffled.get(num).value));
		pt1_answer[0]=shuffled.get(num).value;
		
		num=(loc+2000)%shuffled.size();
		System.out.println("second value is at "+num);
		System.out.println("second value is "+(shuffled.get(num).value));
		pt1_answer[1]=shuffled.get(num).value;
		
		num=(loc+3000)%shuffled.size();
		System.out.println("third value is at "+num);
		System.out.println("third value is "+(shuffled.get(num).value));
		pt1_answer[2]=shuffled.get(num).value;
		System.out.println("Part 1 answer is "+(pt1_answer[0]+pt1_answer[1]+pt1_answer[2]));
		

		
		int loop;
		
		for(loop=0;loop<10;loop++) {
			for(WrappedLong n:startL) {
				loc=shuffledL.indexOf(n);
				
				if(n.value==0) {
					// do nothing
				}
				else if(loc+n.value>=shuffledL.size()) { 
					long target=(loc+n.value)%(shuffledL.size()-1);
					shuffledL.remove(n);
					shuffledL.add((int)target,n);
					
				} else if(loc+n.value==0) {
					shuffledL.remove(n);
					shuffledL.add(n);
					
				} else if (loc+n.value<0) { 
					long target=(loc+n.value)%(shuffledL.size()-1);
					target+=shuffledL.size()-1;
					shuffledL.remove(n);
					shuffledL.add((int)target,n);
					
				} else {			
					shuffledL.remove(n);
					shuffledL.add((int)(loc+n.value),n);
				}
			}
			/*for debugging, uncomment the statements below
			 * System.out.println("After pass "+(loop+1)+" data is ");
			for(WrappedLong l:shuffledL) {
				System.out.println(l.value);
			}*/
		}
		long[] answer=new long[3];
		loc=shuffledL.indexOf(zeroLong);
		
		
		System.out.println("zero value is at "+loc);
		
		num=(loc+1000)%shuffledL.size();
		System.out.println("first value is at "+num);
		System.out.println("first value is "+(shuffledL.get(num).value));
		answer[0]=shuffledL.get(num).value;
		
		num=(loc+2000)%shuffledL.size();
		System.out.println("second value is at "+num);
		System.out.println("second value is "+(shuffledL.get(num).value));
		answer[1]=shuffledL.get(num).value;
		
		num=(loc+3000)%shuffledL.size();
		System.out.println("third value is at "+num);
		System.out.println("third value is "+(shuffledL.get(num).value));
		answer[2]=shuffledL.get(num).value;
		
		System.out.println("Answer is "+(answer[0]+answer[1]+answer[2]));
	
	}	
}
