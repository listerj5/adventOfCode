import java.util.ArrayList;

public class SnakeTail extends SnakeBody{
	ArrayList<Location> visited=new ArrayList();
	
	public SnakeTail(int x, int y, Snake head) {
		super(x,y,head);
		visited.add(new Location(x,y));
	}
	
	public void move(char direction) {
		boolean moved=false;
		if(AdventDay9.debugging) System.out.println("Tail at "+x+","+y+" :: head is at "+head.getLocation().x+","+head.getLocation().y);
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
		if (moved) {
			
			Location loc=new Location(x,y);
			if(AdventDay9.debugging) System.out.println("Tail moved to "+loc.stringOut());
			boolean alreadyVisited=false;
			for(Location l:visited) {
				if(l.stringOut().equals(loc.stringOut())) {
					alreadyVisited=true;
					break;
				}
			}
			if(!alreadyVisited) {
				if(AdventDay9.debugging) System.out.println("this is a new location");
				visited.add(loc);
			}
		}
	}
	public int getSpacesVisited() {return visited.size();}

}
