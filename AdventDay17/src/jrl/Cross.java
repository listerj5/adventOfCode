package jrl;

class Cross extends Shape {
	public Cross() {
		super(new String[] {".#..","###.",".#..","...."});
	}
	public Shape getNew() {return new Cross();}
	@Override
	boolean checkBottomCollision(Cave cave) {
		if(y==1) return true;
		if(cave.contents[y-1][x+1]=='#') return true;
		if(cave.contents[y][x]=='#') return true; // cross can catch on the sticky out bits
		if(cave.contents[y][x+2]=='#') return true;
		return false;
	}
	@Override
	boolean checkRightSideCollision(Cave cave) {
		if(x==caveWidth-3) return true; // simple, wall is in the way
		if(cave.contents[y][x+2]=='#') return true;
		if(cave.contents[y+1][x+3]=='#') return true;
		if(cave.contents[y+2][x+2]=='#') return true;
		
		return false;
	}
	boolean checkLeftSideCollision(Cave cave) {
		if(x==0) return true; // simple, wall is in the way
		if(cave.contents[y][x]=='#') return true;
		if(cave.contents[y+1][x-1]=='#') return true;
		if(cave.contents[y+2][x]=='#') return true;
		return false;
	}
}
