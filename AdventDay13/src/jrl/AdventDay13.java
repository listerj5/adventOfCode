package jrl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AdventDay13 {

	static BufferedReader in;    
	static String inputFileLocation="C:/users/701583673/adventDay13.txt";
	//static String inputFileLocation="C:/users/701583673/day13_test.txt";
	public static void main(String[] args) {
		String nextline;
		ArrayList<MyList> totalList=new ArrayList();
		ArrayList<String> leftData=new ArrayList(); // store instructions in here
		ArrayList<String> rightData=new ArrayList(); // store instructions in here
		try {
			in=new BufferedReader(new FileReader(inputFileLocation));
			while((nextline=in.readLine())!=null) {
	            leftData.add(nextline); // read in the instruction dataset
	            rightData.add(in.readLine());
	            in.readLine();// space between left and right
	        }
			
		} catch (IOException ex) {
			
		}
		int i;
		int answer=0;
		int total=0;
		MyList left;
		MyList right;
		for(i=0;i<leftData.size();i++) {
			left=new MyList(leftData.get(i));
			right=new MyList(rightData.get(i));
			totalList.add(left); // total list is used for part 2, it is combined list of all the inputs
			totalList.add(right);
			answer=left.compareLists(right);
			//System.out.println("Comparing "+left.inputData +" with "+right.inputData); // uncomment for further insight on result of comparison
			//System.out.println("Comparing set "+(i+1)+ " output is "+answer); // answer is -1 (wrong order) or 1 (right order)
			
			if(answer==1) total+=(i+1);	
		}
		System.out.println("puzzle part 1 answer is "+total);
		
		MyList marker2=new MyList("[[2]]");
		MyList marker6=new MyList("[[6]]");
		
		totalList.add(marker2); // add in the markers to the total list
		totalList.add(marker6);
		int m2=0, m6=0;
		Collections.sort(totalList, new SortData()); // use list sort method, comparator in separate function below
		i=1;
		for(MyList m:totalList) {
			
			// System.out.println(m.getInputData() + "at location "+ i); uncomment this if you want to see the complete list sorted
			if(m==marker2) m2=i;
			if(m==marker6) m6=i;
			i++;
		}
		System.out.println("Markers are at "+m2+" and "+m6+" with product of the two being "+(m2*m6));
	}
	static class SortData implements Comparator<MyList>{ // comparator for inputs (lists), sorts smallest to largest (i.e. [] to [10,10,10])
		public int compare(MyList left, MyList right) {
			return right.compareLists(left);
		}
	}
		
		

}
