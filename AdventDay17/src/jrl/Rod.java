package jrl;

class Rod extends Shape {
	public Rod() {
		super(new String[] {"#...","#...","#...","#..."});
	}
	public Shape getNew() {return new Rod();}
	@Override
	boolean checkRightSideCollision(Cave cave) {
		if(x==caveWidth-1) return true; // simple, wall is in the way
		if(cave.contents[y][x+1]=='#') return true;
		if(cave.contents[y+1][x+1]=='#') return true;
		if(cave.contents[y+2][x+1]=='#') return true;
		if(cave.contents[y+3][x+1]=='#') return true;
		return false;
	}
}
