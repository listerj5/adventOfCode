package jrl;



public class Shape {
		static int caveWidth=AdventDay17.caveWidth;
		String[] shape;
		int x;
		int y;
		public Shape(String[] s) {
			this.shape=s;
		}
		public Shape getNew() {return new Shape(shape);}
		
		void setLocation(int x, int y) {this.x=x; this.y=y;}
		
		boolean checkBottomCollision(Cave cave) {
			if(y==1) return true;
			int i;
			for(i=0;i<shape[0].length();i++) {
				if(shape[0].charAt(i)=='#' && cave.contents[y-1][(x+i)]=='#') return true;
			}
			return false;
		}
		boolean checkLeftSideCollision(Cave cave) { // needs to be overriden by the cross and the angle
			int i;
			if(x==0) return true; // simple, wall is in the way
			
			for(i=0;i<shape.length;i++)
			{
				if(shape[i].charAt(0)=='#' && cave.contents[y+i][x-1]=='#') return true;
			}
			
			return false;
		}
		boolean checkRightSideCollision(Cave cave) { // overriden by all subclasses
			return true;
		}
	
	
	
	
	
	
}
