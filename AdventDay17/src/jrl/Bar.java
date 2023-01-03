package jrl;

class Bar extends Shape { // shapes defined so that floor can be on array index 0, so bottom of shape needs to be first entry in string array (so shapes may appear upside down)
	public Bar() {
		super(new String[] {"####","....","....","...."});
	}
	public Shape getNew() {return new Bar();}
	@Override
	boolean checkRightSideCollision(Cave cave) {
		if(x==caveWidth-4) return true; // simple, wall is in the way
		if(cave.contents[y][x+4]=='#') return true;
		return false;
	}	
}
