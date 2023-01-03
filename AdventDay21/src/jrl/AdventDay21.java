package jrl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AdventDay21 {
	static BufferedReader in;    
	static String inputFileLocation="C:/users/701583673/adventDay21.txt";
	//static String inputFileLocation="C:/users/701583673/day21_test.txt";
	public static void main(String[] args) {
		String nextline;
		ArrayList<MonkeyShouter> mShout=new ArrayList<MonkeyShouter>();
		MonkeyShouter me=null;
		MonkeyMath root=null;
		ArrayList<MonkeyMath> mMath=new ArrayList<MonkeyMath>();
		ArrayList<Monkey> mAll=new ArrayList<Monkey>();
		try {
			// example input looks like this:
			// root: pppw + sjmn
			// dbpl: 5
			in=new BufferedReader(new FileReader(inputFileLocation));
			while((nextline=in.readLine())!=null) {
	            String[] s=nextline.split(" ");
	            if(s.length==2) {
	            	// shouter monkey
	            	MonkeyShouter m=new MonkeyShouter(s[0].substring(0,4),Integer.parseInt(s[1]));
	            	mShout.add(m);
	            	mAll.add(m);
	            	if(m.name.equals("humn")) me=m; // this needed for part 2
	            }
	            else if(s.length==4) {
	            	// math monkey
	            	MonkeyMath m=new MonkeyMath(s[0].substring(0,4),s[1],s[3],s[2].charAt(0));
	            	mMath.add(m);
	            	mAll.add(m);
	            	if(m.name.equals("root")) { // this needed for part 2
	            		root=m;
	            		m.func=new Monkey.Equals(); // this overrides the part 1 behaviour for part 2
	            	}
	            }
	        }
		} catch (IOException ex) {
			
		} // link the math monkeys to their shouters
		for(MonkeyMath mm:mMath) {
			for(Monkey m1:mAll) {
				if(m1.name.equals(mm.mName1)) mm.setMonkey1(m1);
				if(m1.name.equals(mm.mName2)) mm.setMonkey2(m1);
			}	
		}
		
		boolean equal=false;		// part 2 variables needed
		long meShout=0L;			// me shout is value that humn will broadcast
		long delta=  1000000000000L; // start with a big delta as trial run throughs indicated the answer is quite big
		
		while(!equal) { // part 2 loop to focus in on the answer
			for(MonkeyMath m1:mMath) m1.reset();
			
			me.s=meShout; // part 2 logic, comment out for part 1
			
			for(MonkeyShouter m:mShout) { // part 1 just needs to do this loop once with default settings, this loop will be called multiple times here for part 2 answer
				m.shout();
			}
			if(root.getShout()==0L) { // root shouts using the equal behaviour, which returns the difference between the two values but also prints out if they are the same
				System.out.println("Finished, answer is "+meShout);
				equal=true;
			} else {
				equal=false;
				System.out.println("wrong value "+meShout);
				System.out.println("Root shouted "+root.getShout()+" from numbers "+root.val1+","+root.val2);
				if(root.getShout()<0) { // if a negative number is reported by root then the branch of monkeys that pick up humn value is too big, so reduce the number
					meShout-=delta;
					delta/=2L; // tweak the delta - having overshot the answer, back up 1/2 a delta and see if that is enough 
				}				// this is not the most efficient as has a tendency to repeat values already tried but is good enough
				meShout+=delta;
			}
		}
		
		
		
	}
}
