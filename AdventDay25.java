package jrl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AdventDay25 {
	static BufferedReader in;    
	static String inputFileLocation="C:/users/701583673/adventDay25.txt";
	//static String inputFileLocation="C:/users/701583673/day25_test.txt";
	public static void main(String[] args) {
		String nextline;
		ArrayList<String> data=new ArrayList(); // store instructions in here
		try {
			in=new BufferedReader(new FileReader(inputFileLocation));
			while((nextline=in.readLine())!=null) {
	            data.add(nextline); // read in the instruction dataset
	        }
			
		} catch (IOException ex) {
			
		}
		long num=0L;
		long totalNum=0L;
		int l=0;
		int i=0;
		
		for(String s:data) {
			l=s.length();
			num=0L;
			for(i=0;i<l;i++) {
				if(s.charAt(i)=='2') {
					num+=(2L*Math.pow(5L, (l-i-1)));
				}
				else if (s.charAt(i)=='1') {
					num+=(1L*Math.pow(5L, (l-i-1)));
				}
				else if(s.charAt(i)=='0') {
					
				}
				else if(s.charAt(i)=='-') {
					num-=(1L*Math.pow(5L, (l-i-1)));
				}
				else if(s.charAt(i)=='=') {
					num-=(2L*Math.pow(5L, (l-i-1)));
				}
			}
			System.out.println("Value of "+s+" = "+num);
			totalNum+=num;
			System.out.println("Total value = "+totalNum);
		}
		i=0;
		while((2.5*Math.pow(5L, i))<=totalNum) { // find the starting power
			i++;
		}
		int pow=i;
		String s="";
		long pow5;
		
		// conversion back - 
		// positive numbers start with a 1 or 2, negative with a - or =
		
		//take example of 550
		// answer is 1-200 (625-125+50)
		// why does it start with 1? because 550 is closest to 625. if it was 1200, it would start with 2, the cutoff between 1 and 2 is where it is closer to 1250 than 625, i.e. 1.5*625
		// because powers of 5 are always odd, the halfway point will always be a 0.5 so integer values will always be closer to one side than the other
		// 5^4 = 625
		// 550 > 625/2 so start with a 1
		// leaves -75
		// work through logic for -75 (i.e. is -75 closer to -250, -125 or 0, its -125 and leaves 50)
		// work through logic for 50 (i.e. is 50 closer to 50,25,0,-25,-50)
		
		// -2.5*pow5<num<-1.5*pow5 = 
		// -1.5*pow5<num<-0.5*pow5 -
		// -0.5*pow5<num<0.5*pow5 0
		// 0.5pow5 to 1.5pow5 1
		// 1.5pow5 to 2.5pow5 2
		
		
		while(i>=0) {
			pow5=(long)Math.pow(5L, i);
			if(totalNum>1.5*pow5) {
				s+="2";
				totalNum-=2*pow5;
			} else if(totalNum>0.5*pow5) {
				s+="1";
				totalNum-=1*pow5;
			} else if(totalNum>-0.5*pow5) {
				s+="0";
			} else if(totalNum>-1.5*pow5) {
				s+="-";
				totalNum+=1*pow5;
			} else {
				s+="=";
				totalNum+=2*pow5;
			}
			i--;
		}
		System.out.println("Answer is "+s);
	}
}
