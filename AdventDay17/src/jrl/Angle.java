package jrl;

class Angle extends Shape {
	public Angle() {
		super(new String[] {"###.","..#.","..#.","...."});
	}
	public Shape getNew() {return new Angle();}
	@Override
	boolean checkLeftSideCollision(Cave cave) { // needs to be overriden by the cross and the angle
		
		if(x==0) return true; // simple, wall is in the way
		
		if(cave.contents[y][x-1]=='#') return true;
		if(cave.contents[y+1][x+1]=='#') return true;
		if(cave.contents[y+2][x+1]=='#') return true;
		return false;
		
	}
	@Override
	boolean checkRightSideCollision(Cave cave) {
		if(x==caveWidth-3) return true; // simple, wall is in the way
		if(cave.contents[y][x+3]=='#') return true;
		if(cave.contents[y+1][x+3]=='#') return true;
		if(cave.contents[y+2][x+3]=='#') return true;
		return false;
	}
}
