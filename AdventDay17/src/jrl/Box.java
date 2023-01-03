package jrl;

class Box extends Shape {
	public Box() {
		super(new String[] {"##..","##..","....","...."});
	}
	public Shape getNew() {return new Box();}
	@Override
	boolean checkRightSideCollision(Cave cave) {
		if(x==(caveWidth-2)) return true; // simple, wall is in the way
		if(cave.contents[y][x+2]=='#') return true;
		if(cave.contents[y+1][x+2]=='#') return true;
		return false;
	}
}
