package jrl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AdventDay22 {
	
	static class Move {
		char dir;
		int spaces;
		public Move(char c, int sp) {
			this.dir=c;
			this.spaces=sp;
		}
		public String debugOut() {
			return new String (dir+" "+Integer.toString(spaces));
		}
	}
	static class Face {
		int xMin; // set the boundaries of the cube face, this to enable transfer to another cube face when navigating
		int xMax;
		int yMin;
		int yMax;
		Transpose left; // this includes the instructions for what to do when exiting the left / right / top / bottom edge of the current face, including the new face
		Transpose right;
		Transpose up;
		Transpose down;
		public Face(int xMin, int xMax, int yMin, int yMax) {
			this.xMin=xMin;
			this.yMin=yMin;
			this.xMax=xMax;
			this.yMax=yMax;
		}
	}
	
	static class Transpose {
		Face oldFace;
		Face newFace;
		char newDirection;
		char oldDirection;
		public Transpose(Face f1, Face f2, char oldDirection, char newDirection) {
			this.oldFace=f1;
			this.newFace=f2;
			this.newDirection=newDirection;
			this.oldDirection=oldDirection;
		}
		public int[] getLocation(int x, int y) { 
			int x1=0,y1=0;
			if(oldDirection=='W') {
				if(newDirection=='S') {
					y1=newFace.yMin;
					x1=newFace.xMin+(y-oldFace.yMin); //old y becomes the new x, large y=large x
				}
				else if(newDirection=='N') { // doesn't seem to ever happen, but if it did small old y = big new x
					y1=newFace.yMax;
					x1=newFace.xMin+(oldFace.yMax-y);
					
				} else if(newDirection=='E') {
					x1=newFace.xMin;
					y1=newFace.yMin+(oldFace.yMax-y); // new y is opposite of old y, so small old y = big new y
					
				} else if(newDirection=='W') { // this might not be called upon, but just in case
					x1=newFace.xMax;
					y1=newFace.yMin+(y-oldFace.yMin); // y stays the same (ish)
				}
			}
			else if(oldDirection=='N') {
				if(newDirection=='E') { // the bigger the old x the bigger the new y
					x1=newFace.xMin;
					y1=newFace.yMin+(x-oldFace.xMin);
				}
				else if(newDirection=='S') { // not used, but if it was...
					y1=newFace.yMin;
					x1=newFace.xMin+(oldFace.xMax-x);// max x becomes minX and minX becomes maxX
				}
				else if(newDirection=='W') { // bigger x is smaller y
					x1=newFace.xMax;
					y1=newFace.yMin+(oldFace.xMax-x);
				}
				else if(newDirection=='N') { // will be needed
					y1=newFace.yMax;
					x1=newFace.xMin+(x-oldFace.xMin);
				}
			}
			else if(oldDirection=='E') {
				if(newDirection=='E') {
					x1=newFace.xMin;
					y1=newFace.yMin+(y-oldFace.yMin);
				}
				else if(newDirection=='S') { // large y leads to small x
					y1=newFace.yMin;
					x1=newFace.xMin+(oldFace.yMax-y);
				}
				else if(newDirection=='W') {
					x1=newFace.xMax;
					y1=newFace.yMin+(oldFace.yMax-y);
				}
				else if(newDirection=='N') { // bigger y means bigger x
					y1=newFace.yMax;
					x1=newFace.xMin+(y-oldFace.yMin);
				}
			}
			else if(oldDirection=='S') {
				if(newDirection=='E') { // bigger x smaller y
					x1=newFace.xMin;
					y1=newFace.yMin+(oldFace.xMax-x);
				}
				else if(newDirection=='S') { 
					y1=newFace.yMin;
					x1=newFace.xMin+(x-oldFace.xMin);
				}
				else if(newDirection=='W') { // bigger x bigger y
					x1=newFace.xMax;
					y1=newFace.yMin+(x-oldFace.xMin);
					
				}
				else if(newDirection=='N') { 
					y1=newFace.yMax;
					x1=newFace.xMin+(oldFace.xMax-x);
				}
			}
			int[] rvalue= {x1,y1};
			return rvalue;
		}
		
	}
	
	// faces - if starting face is s, cube looks like this:
	
	//			S  A
	//			B
	//		 D  C
	//		 E
	
	// S > A - (East) just carry on, same direction across char array, no position translation S (Max,Y) > A(0,Y)  - NO SPACE
	// S > B - (South) just carry on, same direction across char array, no position translation - NO SPACE
	// S > D - (West) horizontal and vertical inversion (i.e. x axis S<0 -> D=0, Y=0 on S becomes D(Y) max, West becomes East, so S(0,Y) > D(0,150-SY) [x=x-50,y=150-y]
	// S > E - (North) S(X,0) > E(0,SX), North becomes East, implies you move from (X,0) to (0,150+(X-50)) (if 50 cube) 
	
	
	// A > S - (West) normal, no direction translation, no position translation - NO SPACE
	// A > E (north) no change in direction etc, so straight translation - A(X,0) > E(X,0) [X=X-100, y=mapHeight-1]   SPACE
	// A > B (South) - direction turns South to West, [100 ,x-50
	// A > C (East) - direction east to West, x maxWidth to 100 (CXmax), y to 150-Y
	
	// B > S (North) normal
	// B > C (South) normal
	// B > D (west) west to south, y becomes X at top of D
	// B > A (east) east to North, y becomes X at bottom of A
	
	// C > D (West) normal
	// C > B (north) normal
	// C > A (east), east becomes west, yMin on C becomes ymax on A (and VV)
	// C > E (south), south becomes west, CX becomes Y, starts on EX max
	
	// D > C normal
	// D > E normal
	// D > B (north) to east
	// D > S (west) to east
	
	// E > S (West) to south
	
	static int mapWidth;
	static int mapHeight;
	static char[][] map;
	static BufferedReader in;    
	static String inputFileLocation="C:/users/701583673/adventDay22.txt";
	//static String inputFileLocation="C:/users/701583673/day22_test.txt";
	public static void main(String[] args) {
		String nextline;
		
		ArrayList<String> mapData=new ArrayList(); // store instructions in here
		ArrayList<Move> moves=new ArrayList();
		String directions="";
		mapWidth=0;
		try {
			in=new BufferedReader(new FileReader(inputFileLocation));
			boolean mapDone=false;
			while((nextline=in.readLine())!=null) {
	            if(!mapDone) {
	            	if(nextline.length()<2) mapDone=true;
	            	else {
	            		mapData.add(nextline);
	            		if(nextline.length()>mapWidth) mapWidth=nextline.length();
	            	}
	            }
	            else directions=nextline;
	        }
		} catch (IOException ex) {
			
		}
		mapHeight=mapData.size();
		map=new char[mapHeight][mapWidth];
		// faces - if starting face is s, cube looks like this:
		
		//			S  A
		//			B
		//		 D  C
		//		 E
		
		Face s=new Face(50,99,0,49); // starting face
		Face a=new Face(100,149,0,49);
		Face b=new Face(50,99,50,99);
		Face c=new Face(50,99,100,149);
		Face d=new Face(0,49,100,149);
		Face e=new Face(0,49,150,199);
		
		// link the faces
		s.right=new Transpose(s,a,'E','E');
		s.down=new Transpose(s,b,'S','S');
		s.left=new Transpose(s,d,'W','E');
		s.up=new Transpose(s,e,'N','E');
		
		a.right=new Transpose(a,c,'E','W');
		a.down=new Transpose(a,b,'S','W');
		a.left=new Transpose(a,s,'W','W');
		a.up=new Transpose(a,e,'N','N');
		
		b.right=new Transpose(b,a,'E','N');
		b.down=new Transpose(b,c,'S','S');
		b.left=new Transpose(b,d,'W','S');
		b.up=new Transpose(b,s,'N','N');
		
		c.right=new Transpose(c,a,'E','W');
		c.down=new Transpose(c,e,'S','W');
		c.left=new Transpose(c,d,'W','W');
		c.up=new Transpose(c,b,'N','N');
		
		d.right=new Transpose(d,c,'E','E');
		d.down=new Transpose(d,e,'S','S');
		d.left=new Transpose(d,s,'W','E');
		d.up=new Transpose(d,b,'N','E');
		
		e.right=new Transpose(e,c,'E','N');
		e.down=new Transpose(e,a,'S','S');
		e.left=new Transpose(e,s,'W','S');
		e.up=new Transpose(e,d,'N','N');
		
		
		
		
		
		
		int x;
		int y;
		boolean startSet=false;
		int startX=0;
		int startY=0;
		char facing='E';
		int n;
		
		for(y=0;y<mapHeight;y++) { // note - answer requires +1 offset, so array entry zero is actually row 1, col 1.
			for(x=0;x<mapWidth;x++) { 
				if(x>=mapData.get(y).length()) map[y][x]=' '; 	
				else {
					map[y][x]=mapData.get(y).charAt(x);
					if(!startSet && map[y][x]=='.') {
						startX=x;
						startY=y;
						startSet=true;
						System.out.println("Start position found and set to "+startX+","+startY);
					}
				}
			}
		}
		
		for(y=0;y<directions.length();y++) {
			if(directions.charAt(y)=='L') moves.add(new Move('L',0));
			else if(directions.charAt(y)=='R') moves.add(new Move('R',0));
			else {
				x=1;
				while(x+y<directions.length() && directions.charAt(y+x)!='R' && directions.charAt(y+x)!='L') {
					x++;
				}
				moves.add(new Move('F',Integer.parseInt(directions.substring(y,y+x))));
				y+=(x-1);
			}
		}
		x=startX;
		y=startY;
		
		for(Move m:moves) { // part 1 code
			facing=turn(m.dir,facing);
			n=m.spaces;
			while(n>0) {
				if(facing=='N') {
					if(y==0) { // reached top of map
						if(map[mapHeight-1][x]=='#') n=0; // quick check on bottom of map, stop
						else if(map[mapHeight-1][x]==' ') { // more complicated check as bottom of map has space
							
							int temp=navigateWrap(map,x,mapHeight-1,facing);
							
							if(temp==mapHeight-1) n=0; // can't move, so stop
							
							else { // can move, so move and use a step
								n--;
								y=temp;
							}
						}
						else { // quick check on bottom of map, its fine to move (i.e. its a '.')
							y=mapHeight-1;
							n--;
						}
						
					} else { // not at the top, so just check the y-1 position
						if(map[y-1][x]=='#') n=0; // barrier, so stop
						else if(map[y-1][x]==' ') { // space, need to check what comes after the space
							
							int temp=navigateWrap(map,x,y-1,facing);
							
							if(temp==y-1) n=0; // can't move, so stop
							else {
								n--;
								y=temp;
							}
						}
						else { // no space, no barrier
							y--;
							n--;
						}
					}
				}
				else if (facing=='E') {
					if(x==mapWidth-1) { // reached the eastern edge of map
						if(map[y][0]=='#') n=0; // quick check, is western edge blocked? if so stop
						else if(map[y][0]==' ') { // more complicated check
							int temp=navigateWrap(map,0,y,facing);
							
							if(temp==0) n=0;
							
							else {
								n--;
								x=temp;
							}
						}
						else { // quick check on western edge of map, its fine 
							x=0;
							n--;
						}
					} else {
						if(map[y][x+1]=='#') n=0;
						else if(map[y][x+1]==' ') {
							
							int temp=navigateWrap(map,x+1,y,facing);
							
							if(temp==x+1) n=0; // can't move, so stop
							else {
								n--;
								x=temp;
							}
						}
						else {
							x++;
							n--;
						}
					}
				}
				else if(facing=='S') {
					if(y==mapHeight-1) { // reached bottom of map
						if(map[0][x]=='#') n=0; // quick check on top of map, stop
						else if(map[0][x]==' ') { // more complicated check as top of map has space
							
							int temp=navigateWrap(map,x,0,facing);
							
							if(temp==0) n=0; // can't move, so stop
							
							else { // can move, so move and use a step
								n--;
								y=temp;
							}
						}
						else { // quick check on top of map, its fine to move (i.e. its a '.')
							y=0;
							n--;
						}
						
					} else { // not at the bottom, so just check the y+1 position
						if(map[y+1][x]=='#') n=0; // barrier, so stop
						else if(map[y+1][x]==' ') { // space, need to check what comes after the space
							
							int temp=navigateWrap(map,x,y+1,facing);
							
							if(temp==y+1) n=0; // can't move, so stop
							else {
								n--;
								y=temp;
							}
						}
						else { // no space, no barrier
							y++;
							n--;
						}
					}
				}
				else if (facing=='W') {
					if(x==0) { // reached the western edge of map
						if(map[y][mapWidth-1]=='#') n=0; // quick check, is western edge blocked? if so stop
						else if(map[y][mapWidth-1]==' ') { // more complicated check
							int temp=navigateWrap(map,mapWidth-1,y,facing);
							
							if(temp==mapWidth-1) n=0;
							
							else {
								n--;
								x=temp;
							}
						}
						else { // quick check on western edge of map, its fine 
							x=mapWidth-1;
							n--;
						}
					} else {
						if(map[y][x-1]=='#') n=0;
						else if(map[y][x-1]==' ') {
							
							int temp=navigateWrap(map,x-1,y,facing);
							
							if(temp==x-1) n=0; // can't move, so stop
							else {
								n--;
								x=temp;
							}
						}
						else {
							x--;
							n--;
						}
					}
				}
			}
		}
		
		System.out.println("X="+x+", Y="+y+", Facing "+facing);
		
		
		// part 2 code:
		
		x=startX;
		y=startY;
		int nextX=0;
		int nextY=0;
		int[] x1y1=new int[2];
		facing='E';
		Face currentFace=s; // starting Face
		
		for(Move m:moves) {
			facing=turn(m.dir,facing);
			n=m.spaces;
			while(n>0) {
				if(facing=='N') {
					if(y==currentFace.yMin) { // reached edge of current face
						x1y1=currentFace.up.getLocation(x, y);
						if(map[x1y1[1]][x1y1[0]]=='#') n=0;
						else {
							facing=currentFace.up.newDirection;
							x=x1y1[0];
							y=x1y1[1];
							currentFace=currentFace.up.newFace;
							n--;
							
						}
					} else { 
						if(map[y-1][x]=='#') n=0; // barrier, so stop
						else { // no space, no barrier, note - no space fork as covered by face ymin above
							y--;
							n--;
						}
					}
				}
				else if (facing=='E') {
					if(x==currentFace.xMax) { // reached the eastern edge of current face
						x1y1=currentFace.right.getLocation(x, y);
						if(map[x1y1[1]][x1y1[0]]=='#') n=0;
						else {
							facing=currentFace.right.newDirection;
							x=x1y1[0];
							y=x1y1[1];
							currentFace=currentFace.right.newFace;
							n--;
						}
					} else {
						if(map[y][x+1]=='#') n=0;
						else {
							x++;
							n--;
						}
					}
				}
				else if(facing=='S') {
					if(y==currentFace.yMax) { // reached bottom of face
						x1y1=currentFace.down.getLocation(x, y);
						if(map[x1y1[1]][x1y1[0]]=='#') n=0;
						else {
							facing=currentFace.down.newDirection;
							x=x1y1[0];
							y=x1y1[1];
							currentFace=currentFace.down.newFace;
							n--;
						}
					} else { // not at the bottom, so just check the y+1 position
						if(map[y+1][x]=='#') n=0; // barrier, so stop
						else { // no space, no barrier
							y++;
							n--;
						}
					}
				}
				else if (facing=='W') {
					if(x==currentFace.xMin) { // reached the western edge of map
						x1y1=currentFace.left.getLocation(x, y);
						if(map[x1y1[1]][x1y1[0]]=='#') n=0;
						else {
							facing=currentFace.left.newDirection;
							x=x1y1[0];
							y=x1y1[1];
							currentFace=currentFace.left.newFace;
							n--;
						}
					} else {
						if(map[y][x-1]=='#') n=0;
						else {
							x--;
							n--;
						}
					}
				}
			}
		}
		System.out.println("X="+x+", Y="+y+", Facing "+facing);
		System.out.println (((y+1)*1000)+((x+1)*4));
		
	}
	static int navigateWrap(char[][] map, int x, int y, char facing) {
		int num=0;
		if(facing=='N') { // this assumes you are passed x and y co-ordinates relating to a space
			while(y-num>=0 && map[y-num][x]==' ') { // keep going whilst inside array bounds and got a space
				num++;
				if(y-num<0) num-=mapHeight; 
			}
			if(map[y-num][x]=='#') return y;
			else return (y-num);
			
		} else if (facing=='S') {
			while(y+num<mapHeight && map[y+num][x]==' ') {
				num++;
				if(y+num>=mapHeight) num-=mapHeight;
			}
			
			if(map[y+num][x]=='#') return y;
			else return (y+num);
			
		} else if (facing=='E') {
			while(x+num<mapWidth && map[y][x+num]==' ') {
				num++;
				if(x+num>=mapWidth) num-=mapWidth;
			}
			if(map[y][x+num]=='#') return x;
			else return (x+num);
			
		} else if (facing=='W') {
			while(x-num>=0 && map[y][x-num]==' ') {
				num++;
				if(x-num<0) num-=mapWidth;
			}
			if(map[y][x-num]=='#') return x;
			else return (x-num);
		}
		return 0;
	}
	static char turn(char c,char facing) {
		if(c=='L') {
			switch (facing) {
			case 'N':
				facing='W';
				break;
			case 'E':
				facing='N';
				break;
			case 'S':
				facing='E';
				break;
			case 'W':
				facing='S';
			}
		} else if(c=='R') {
			switch(facing) {
			case 'N':
				facing='E';
				break;
			case 'E':
				facing='S';
				break;
			case 'S':
				facing='W';
				break;
			case 'W':
				facing='N';
			} 
		}
		return facing;
	} 
}
