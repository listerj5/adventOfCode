package jrl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class AdventDay17 {
	
	


	
	static BufferedReader in;    
	static String inputFileLocation="C:/users/701583673/adventDay17.txt";
	
	static int caveWidth=7;
	
	//static String inputFileLocation="C:/users/701583673/day17_test.txt";
	public static void main(String[] args) {
		String nextline;
		Cave cave;
		String jets="";
		int spawnNumber=0;
		Shape sh;
		Shape[] sequence= {new Bar(),new Cross(), new Angle(), new Rod(), new Box()};
		int spawnLevel=4;// bottom edge is 3 units above floor, i.e. floor at zero, 1-3 are air, then block is at 4
		try {
			in=new BufferedReader(new FileReader(inputFileLocation));
			while((nextline=in.readLine())!=null) {
	            jets+=nextline;
	        }
		} catch (IOException ex) {
			
		}
		int rocks=0;
		int i,j;
		cave=new Cave();
		int jetIndex=0;
		
		System.out.println(jets.length());
		
		// running the program for part 2 showed that the first cycle of jet loops i.e. 10091 jet pushes took 1756 rocks stopped
		// at the point the first jet cycle completed, the 1st pattern had just stopped and the second rock pattern was just about to spawn
		// at the point the second jet cycle completed, the 1st pattern had again, just stopped and the second rock pattern was just about to spawn
		// this implies the same pattern (as per the second cycle) continues to repeat indefinitely
		
		/*
		 * Jet loop at 1756 rocks stopped
			Rock stopped=true
			Spawn index=2
			height=2719
			
			Jet loop at 3511 rocks stopped
			Rock stopped=true
			Spawn index=2
			height=5466
			
			Jet loop at 5266 rocks stopped
			Rock stopped=true
			Spawn index=2
			height=8213
			
		 */
		
		// so the first loop lasted exactly 1756 rocks, subsequent loops lasted exactly 1755 rocks
		// the height of the first loop was 2719, subsequent loops added 2747 height
		// so need to determine how many cycles in elephant ask and height of the remainder
		
		
		long answer=1000000000000L;
		long minus1st=answer-1756L;
		long remainder=minus1st%1755;
		long cycles=minus1st/1755;
		long a1=(cycles-1L)*2747L;
		
		System.out.println("cycles:"+cycles+ " remainder:"+remainder);
		
		//remainder is 1404 rocks, added to 3511 rocks gives 4915 - this is the height of the first cycle and last cycle and a half. add to this number of cycles(-1) * cycle height 
		
				
		// jets is 10091 long
		// this means it will go zero to 10090 then start again
		// 
		
		boolean stopped;
		boolean showHeight=false;
		while(rocks<4915) {
			sh=sequence[spawnNumber].getNew();
			sh.setLocation(2, spawnLevel+cave.maxRockHeight);
			spawnNumber++;
			if(spawnNumber==sequence.length) {
				spawnNumber=0;
				System.out.println("shape looping, jet index is "+jetIndex);
			}
			cave.addShape(sh);
			stopped=false;
			//move sequence
			while(!stopped) {
				
				if(jets.charAt(jetIndex)=='<') {
					//try move move left
					if(!sh.checkLeftSideCollision(cave)) {
						cave.removeShape(sh);
						sh.x--;
						cave.addShape(sh);
					}
				} else if(jets.charAt(jetIndex)=='>'){
					if(!sh.checkRightSideCollision(cave)) {
						cave.removeShape(sh);
						sh.x++;
						cave.addShape(sh);
					}
					//try move right
				}
				else {
					System.out.println("ERROR after "+jetIndex);
					return;
				}
				jetIndex++;
				
				
				if(sh.checkBottomCollision(cave)) {
					stopped=true;
				} else {
					cave.removeShape(sh);
					sh.y--;
					cave.addShape(sh);
				}
				if(jetIndex==jets.length()) {
					if(stopped) {
						System.out.println("Jet loop at "+rocks+" rocks stopped");
						System.out.println("Rock stopped="+stopped);
						System.out.println("Spawn index="+spawnNumber);
						showHeight=true;
					}
					jetIndex=0;
				}
			}
			rocks++;
			
			for(i=cave.maxRockHeight;i<cave.contents.length;i++) {
				for(j=0;j<caveWidth;j++) {
					if(cave.contents[i][j]=='#') {
						cave.maxRockHeight=i;
						if(showHeight) {
							System.out.println("height="+cave.maxRockHeight);
							showHeight=false;
						}
					}
				}
			}
		}
		System.out.println(cave.maxRockHeight);
		System.out.println("jets at "+jetIndex);
		System.out.println("rocks="+rocks);
		
		System.out.println("Answer is "+(a1+(long)cave.maxRockHeight));
		
		
	}
	static void drawCave(Cave cave, char direction) {
		if(cave.maxRockHeight<10) {
			
		}
	}
}
