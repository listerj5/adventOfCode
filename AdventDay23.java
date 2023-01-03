package jrl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AdventDay23 {
	static BufferedReader in;    
	static String inputFileLocation="C:/users/701583673/adventDay23.txt";
	//static String inputFileLocation="C:/users/701583673/day23_test.txt";
	static int iMapWidth;
	static int iMapHeight;
	static int mapWidth;
	static int mapHeight;
	static int maxMove=1000; // change this to 10 to get answer to first part of puzzle, adjust this upwards if not big enough to fit all the elf moves in
	static class LocXY {
		int x;
		int y;
		Elf e;
		public LocXY(int x, int y, Elf e)
		{
			this.x=x;
			this.y=y;
			this.e=e;
		}
	}
	static class Elf{
		LocXY current;
		LocXY target;
		boolean move=false;
		public Elf(int x, int y) {
			current=new LocXY(x,y,this);
		}
		public LocXY checkDirection(ArrayList<String> d, char[][] map) {
			// check the directions in the arraylist in order,
			// return null if cant move else calculate a new LocXY which is stored as the target, return LocXY (to add to list) 
			// in main loop, have arraylist of all planned moves, check for moves that are to the same spot (note this could be processor intensive as estimate there are 3000 elves, though not all will move)
			
			if(map[current.y-1][current.x]=='.' && map[current.y-1][current.x-1]=='.' && map[current.y-1][current.x+1]=='.'
					&& map[current.y+1][current.x]=='.' && map[current.y+1][current.x-1]=='.' && map[current.y+1][current.x+1]=='.'
					&& map[current.y][current.x-1]=='.' && map[current.y][current.x+1]=='.') {
				// important bit of logic that I missed first time around - don't move if the elf has no-one around
				target=null;
			} else {
			
				int i=0;
				target=null;
				while(i<d.size()) {
					if(d.get(i).equals("N")) {
						if(map[current.y-1][current.x]=='.' && map[current.y-1][current.x-1]=='.' && map[current.y-1][current.x+1]=='.') {
							target=new LocXY(current.x, current.y-1, this);
							break;
						}
					} else if(d.get(i).equals("S")) {
						if(map[current.y+1][current.x]=='.' && map[current.y+1][current.x-1]=='.' && map[current.y+1][current.x+1]=='.') {
							target=new LocXY(current.x, current.y+1,this);
							break;
						}
						
					} else if(d.get(i).equals("E")) {
						if(map[current.y][current.x+1]=='.' && map[current.y-1][current.x+1]=='.' && map[current.y+1][current.x+1]=='.') {
							target=new LocXY(current.x+1, current.y,this);
							break;
							
						}
					} else if(d.get(i).equals("W")) {
						if(map[current.y][current.x-1]=='.' && map[current.y-1][current.x-1]=='.' && map[current.y+1][current.x-1]=='.') {
							target=new LocXY(current.x-1, current.y,this);
							break;
						}
					}
					i++;
				}
			}
			
			return target;
		}
		public void move(char[][] map) {
			if(target!=null) {
				map[current.y][current.x]='.';
				map[target.y][target.x]='#';
				current.x=target.x;
				current.y=target.y;
			}
		}
		public void noMove() {
			this.target=null;
		}
	}
	
	public static void main(String[] args) {
		String nextline;
		String oldMove;
		char[][] inputMap;
		char[][] map;
		ArrayList<String> nextMove=new ArrayList(); // this has list of whether to move north south east or west
		ArrayList<String> data=new ArrayList(); // store instructions in here
		ArrayList<String> display=new ArrayList(); // this used to print out the map if needed
		ArrayList<Elf> elves=new ArrayList(); // the elves
		ArrayList<LocXY> moves=new ArrayList(); // where the elves are planning to move
		try {
			in=new BufferedReader(new FileReader(inputFileLocation));
			while((nextline=in.readLine())!=null) {
	            data.add(nextline); // read in the instruction dataset
	        }
			
		} catch (IOException ex) {
			
		}
		iMapWidth=data.get(0).length();
		iMapHeight=data.size();
		mapWidth=iMapWidth+(2*maxMove); // this is overkill but assume max map size is the input map width plus twice the number of moves (assuming elves move out from centre every move)
		mapHeight=iMapHeight+(2*maxMove);
		
		inputMap=new char[iMapHeight][iMapWidth];
		map=new char[mapHeight][mapWidth];
		
		nextMove.add("N");
		nextMove.add("S");
		nextMove.add("W");
		nextMove.add("E");
		
		
		
		int x;
		int y;
		for(y=0;y<iMapHeight;y++) {
			for(x=0;x<iMapWidth;x++) {
				inputMap[y][x]=data.get(y).charAt(x);
			}
		}
		
		
		
		for(y=0;y<mapHeight;y++) {
			String s="";
			for(x=0;x<mapWidth;x++) {
				if(y<maxMove || (y-maxMove)>=iMapHeight) {
					map[y][x]='.';
					s+=".";
				}
				else if(x<maxMove || (x-maxMove)>=iMapWidth) {
					map[y][x]='.';
					s+=".";
				} else {
					map[y][x]=inputMap[y-maxMove][x-maxMove];
					s+=inputMap[y-maxMove][x-maxMove];
					if(map[y][x]=='#') elves.add(new Elf(x,y));
				}
			}
			display.add(s);
		}
		
		// uncomment line below to print out the map
		// for(String s:display) System.out.println(s);
		
		int loop;
		
		LocXY act;
		for(loop=1;loop<=maxMove;loop++) {
			moves.clear();
			for(Elf e:elves) {
				act=e.checkDirection(nextMove, map);
				if(act!=null) moves.add(act);
			}
			System.out.println("Round "+loop+": first pass moves="+moves.size()); 
			if(moves.size()==0) break; // use this for answer to part 2
			
			// then compare moves
			// if any moves are to the same location, use the pointer to elf to stop the move (i.e. call LocXY.e.noMove)
			
			while(moves.size()>0) {
				LocXY m=moves.get(0);
				if(m!=null) {
					for(LocXY m2:moves) {
						if(m2!=null && m.x==m2.x && m.y==m2.y && m.e!=m2.e) {
							m.e.noMove();
							m2.e.noMove();
						}
					}
					m.e.move(map);
				}
				moves.remove(0);
			}
			oldMove=nextMove.get(0); // move previous move to the end by removing it from index zero and adding it (at the end of the array)
			nextMove.remove(0);
			nextMove.add(oldMove);
			
			/* uncomment to see the output printed out
			 * display.clear();
			for(y=0;y<mapHeight;y++) {
				String s="";
				for(x=0;x<mapWidth;x++) {
					s+=map[y][x];
				}
				display.add(s);
			}
			for(String s:display) System.out.println(s);*/
			
			
		}
		int minX=100;
		int minY=100;
		int maxX=0;
		int maxY=0;
		for(Elf e:elves) {
			if(e.current.x<minX) minX=e.current.x;
			if(e.current.x>maxX) maxX=e.current.x;
			if(e.current.y<minY) minY=e.current.y;
			if(e.current.y>maxY) maxY=e.current.y;
			
		}
		int xSpan=(1+(maxX-minX));
		int ySpan=(1+(maxY-minY));
		System.out.println("X span = "+minX+","+maxX+"   size="+xSpan);
		System.out.println("Y span = "+minY+","+maxY+"   size="+ySpan);
		System.out.println("Number elves="+elves.size());
		System.out.println("Answer to part 1 when using "+maxMove+" loops is: "+((xSpan*ySpan)-elves.size()));
		
		
		
		
	}
}
