

public abstract class Snake {
	int x;
	int y;
	Snake tail;
	Snake head=null;
	public Snake(int x, int y) {
		this.x=x;
		this.y=y;
	}
	public abstract void move(char direction);
	
	public Location getLocation() {
		return new Location(x,y);
	}
	public void setTail(Snake tail) {
		this.tail=tail;
	}
	
}
