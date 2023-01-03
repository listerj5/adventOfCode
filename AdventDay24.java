package jrl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AdventDay24 {
	static class Wind {
		char direction;
		int startX;
		int startY;
		int x;
		int y;
		public Wind(char direction, int x, int y) {
			this.direction=direction;
			this.startX=x;
			this.startY=y;
			this.x=x;
			this.y=y;
		}
		public void add(char[][] bliz, int t) {
			int m;
			if(direction=='>') {
				m=t%cycleWidth;
				x=startX+m;
				if(x>=eastEdge) x-=cycleWidth;
				bliz[startY][x]='#';
			} else if(direction=='<') {
				m=t%cycleWidth;
				x=startX-m;
				if(x<=westEdge) x+=cycleWidth;
				bliz[startY][x]='#';
			} else if(direction=='^') {
				m=t%cycleHeight;
				y=startY-m;
				if(y<=northEdge) y+=cycleHeight;
				bliz[y][startX]='#';
			} else {
				m=t%cycleHeight;
				y=startY+m;
				if(y>=southEdge) y-=cycleHeight;
				bliz[y][startX]='#';
			}
		}
	}
	static class Valley {
		char[][] bliz;
		public Valley(char[][] bliz) {
			this.bliz=bliz;
		}
		
	}
	
	
	static class Route {
		int x;
		int y;
		char move;
		Route lastMove;
		int time;
		static int t;
		public Route(int x, int y, char move, Route lastMove, int time) {
			this.x=x;
			this.y=y;
			this.move=move;
			this.lastMove=lastMove;
			this.time=time;
		}
		public Route(int x, int y, int time) {
			this.x=x;
			this.y=y;
			this.time=time;
		}
		public Route(int x, int y, int time, Route r) {
			this(x,y,time);
			lastMove=r;
		}
		public void move(ArrayList<Route> moves, ArrayList<Valley> valley) {
			t=(time+1)%cycles;
			if(valley.get(t).bliz[y][x]!='#' && (time<(300+start) || y!=rStartY)) {
				// stand still
				moves.add(new Route(x,y,time+1,this));
			}
			if(y>0 && valley.get(t).bliz[y-1][x]!='#') {
				// could move north
				moves.add(new Route(x,y-1,time+1,this));
			}
			if(y<southEdge && valley.get(t).bliz[y+1][x]!='#') {
				// could move south
				moves.add(new Route(x,y+1,time+1,this));
			}
			
			if(y<eastEdge && valley.get(t).bliz[y][x+1]!='#') {
				// could move east
				moves.add(new Route(x+1,y,time+1,this));
			}
			if(y>westEdge && valley.get(t).bliz[y][x-1]!='#') {
				moves.add(new Route(x-1,y,time+1,this));
				// could move west
			}
		}
	}
	
	static BufferedReader in;
	static int northEdge=0; // needs checking these do
	static int southEdge;
	static int eastEdge;
	static int westEdge=0;
	static int valleyWidth;
	static int valleyHeight;
	static int cycleWidth;
	static int cycleHeight;
	static int cycles=300;
	static int start=0;
	static int rStartY=0;
	
	static String inputFileLocation="C:/users/701583673/adventDay24.txt";
	//static String inputFileLocation="C:/users/701583673/day24_test.txt";
	public static void main(String[] args) {
		String nextline;
		ArrayList<Route> moves=new ArrayList();
		ArrayList<Valley> valley=new ArrayList();
		ArrayList<Wind> winds=new ArrayList();
		ArrayList<String> data=new ArrayList(); // store instructions in here
		try {
			in=new BufferedReader(new FileReader(inputFileLocation));
			while((nextline=in.readLine())!=null) {
	            data.add(nextline); // read in the instruction dataset
	        }
			
		} catch (IOException ex) {
			
		}
		valleyWidth=data.get(0).length();
		valleyHeight=data.size();
		cycleWidth=valleyWidth-2;
		cycleHeight=valleyHeight-2;
		southEdge=valleyHeight-1;
		eastEdge=valleyWidth-1;
		int endX=valleyWidth-2;
		int endY=valleyHeight-1;
		int startX=1;
		int startY=0;
		
		System.out.println("puzzle data:");
		System.out.println("Valley Width: "+valleyWidth);
		System.out.println("Valley Height: "+valleyHeight);
		System.out.println("Cycle Width: "+cycleWidth);
		System.out.println("Cycle Height: "+cycleHeight);
		
		
		
		
		int x;
		int y;
		for(y=1;y<data.size()-1;y++) {
			for(x=1;x<data.get(y).length()-1;x++) {
				if(data.get(y).charAt(x)!='.') {
					winds.add(new Wind(data.get(y).charAt(x),x,y));
				}
			}
		}
		// next, create a bank of valleys (note, given valley dimensions, valley should repeat after x * y cycles (minus walls, which given input data of 20 X 150 after factorising is about every 300 cycles)
		
		int i;
		
		for(i=0;i<300;i++) {
			char[][] bliz=new char[valleyHeight][valleyWidth];
			Valley v=new Valley(bliz);
			for(x=0;x<valleyWidth;x++) {
				bliz[0][x]='#';
				bliz[valleyHeight-1][x]='#';
			}
			for(y=0;y<valleyHeight;y++) {
				bliz[y][0]='#';
				bliz[y][valleyWidth-1]='#';
			}
			bliz[0][1]='.';
			bliz[valleyHeight-1][valleyWidth-2]='.';
			for(Wind w:winds) {
				w.add(bliz,i);
				// work through the winds and add barriers to valley, may as well just use # for walls and where winds are
			}
			valley.add(v);
		}
		
		moves.add(new Route(1,0,0));
		
		// then add first move in here
		
	
		
		// then loop through this
		int count=0;
		Route solution=null;
		while(moves.size()>0) {
			if(moves.get(0).x==endX && moves.get(0).y==endY) {
				System.out.println("Found solution in "+moves.get(0).time);
				solution=moves.get(0);
				break;
			} else {
				moves.get(0).move(moves,valley);
			}
			moves.remove(0);
			count++;
			if(count%1000==0) {
				//System.out.println("attempt pruning of duplicates (route size is "+moves.size()+")");
				for(i=0;i<moves.size();i++) {
					Route r1=moves.get(i);
					for(y=0;y<moves.size();y++) {
						Route r2=moves.get(y);
						if(r1!=r2 && r1.x==r2.x && r1.y==r2.y && r1.time==r2.time) {
							moves.remove(r2);
							i--;
							//System.out.println("removed duplicate");
							break;
						}
					}
				}
			}
			
		} // made it through the valley forwards 1st time
		
		moves.clear();
		moves.add(solution);
		start=solution.time;
		rStartY=valleyHeight-1;
		
		while(moves.size()>0) { // now go back
			if(moves.get(0).x==startX && moves.get(0).y==startY) {
				System.out.println("Found solution in "+moves.get(0).time);
				solution=moves.get(0);
				break;
			} else {
				moves.get(0).move(moves,valley);
			}
			moves.remove(0);
			count++;
			if(count%1000==0) {
				//System.out.println("attempt pruning of duplicates (route size is "+moves.size()+")");
				for(i=0;i<moves.size();i++) {
					Route r1=moves.get(i);
					for(y=0;y<moves.size();y++) {
						Route r2=moves.get(y);
						if(r1!=r2 && r1.x==r2.x && r1.y==r2.y && r1.time==r2.time) {
							moves.remove(r2);
							i--;
							//System.out.println("removed duplicate");
							break;
						}
					}
				}
			}
			
		}
		
		moves.clear();
		moves.add(solution);
		start=solution.time;
		rStartY=0;
		// made it back to start, now turn around and go back again
		while(moves.size()>0) {
			if(moves.get(0).x==endX && moves.get(0).y==endY) {
				System.out.println("Found final solution in "+moves.get(0).time);
				solution=moves.get(0);
				break;
			} else {
				moves.get(0).move(moves,valley);
			}
			moves.remove(0);
			count++;
			if(count%1000==0) {
				//System.out.println("attempt pruning of duplicates (route size is "+moves.size()+")");
				for(i=0;i<moves.size();i++) {
					Route r1=moves.get(i);
					for(y=0;y<moves.size();y++) {
						Route r2=moves.get(y);
						if(r1!=r2 && r1.x==r2.x && r1.y==r2.y && r1.time==r2.time) {
							moves.remove(r2);
							i--;
							//System.out.println("removed duplicate");
							break;
						}
					}
				}
			}	
		}
	}
}
