
public class Location {
	int x;
	int y;
	public Location(int x, int y) {
		this.x=x;
		this.y=y;
	}
	public String stringOut() {
		String s=Integer.toString(x) +","+Integer.toString(y);
		return s;
	}
	static float distanceBetween(Location l1, int x, int y) {
		float xdiff=(float)(l1.x-x);
		float ydiff=(float)(l1.y-y);
		
		float dist=(float)Math.sqrt((xdiff*xdiff)+(ydiff*ydiff));
		return dist;
	}
}
