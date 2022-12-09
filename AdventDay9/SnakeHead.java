
public class SnakeHead extends Snake {
	public SnakeHead(int x, int y) {
		super(x,y);
	}
	public void move(char direction) {
		if(AdventDay9.debugging) System.out.println("Head moving "+direction);
		if(direction=='L') x--;
		else if(direction=='R') x++;
		else if(direction=='U') y++;
		else if(direction=='D') y--;
		tail.move(direction);
	}

}
