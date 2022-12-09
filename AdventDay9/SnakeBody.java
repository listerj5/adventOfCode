
public class SnakeBody extends Snake {
	static int segmentNumber=0;
	int segment;
	public SnakeBody(int x, int y, Snake head) {
		super(x,y);
		this.head=head;
		segment=segmentNumber; // supports debugging by numbering the segments
		segmentNumber++;
		}
	public void move(char direction) {
		boolean moved=false;
		if(AdventDay9.debugging) System.out.println("Segment "+segment+ " is at "+x+","+y+" :: head is at "+head.getLocation().x+","+head.getLocation().y);
		if(Location.distanceBetween(head.getLocation(),x,y)>2.01f) {
			// move diagonally
			moved=true;
			if(head.getLocation().x>x) x++;
			if(head.getLocation().x<x) x--;
			if(head.getLocation().y>y) y++;
			if(head.getLocation().y<y) y--;
		} else {
			if(head.getLocation().x-x>1) {
				moved=true;
				this.x++;
			} else if (head.getLocation().x-x<-1) {
				moved=true;
				this.x--;
			}
			if(head.getLocation().y-y>1) {
				moved=true;
				this.y++;
			}
			else if(head.getLocation().y-y<-1) {
				moved=true;
				this.y--;
			}
		}
		if(moved) {
			if(AdventDay9.debugging) System.out.println("Segment "+segment+" now at "+x+","+y);
			tail.move(direction);
		}
	}
}
