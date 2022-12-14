package jrl;

public class PointXY {
	int x;
	int y;
	static int minX=10000;
	static int minY=10000;
	static int maxX=0;
	static int maxY=0;
	public PointXY(int x, int y) {
		this.x=x;
		this.y=y;
		updateMinMax();
	}
	public PointXY(String s) {
		String[] xy=s.split(",");
		x=Integer.parseInt(xy[0]);
		y=Integer.parseInt(xy[1]);
		updateMinMax();
	}
	public void updateMinMax() {
		if(x>maxX) maxX=x;
		if(x<minX) minX=x;
		if(y>maxY) maxY=y;
		if(y<minY) minY=y;
	}
	public String xyToString() {
		return new String (Integer.toString(x)+","+Integer.toString(y));
	}
}
