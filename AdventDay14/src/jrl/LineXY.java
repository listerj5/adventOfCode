package jrl;

public class LineXY {
	PointXY start;
	PointXY end;
	public LineXY (PointXY a, PointXY b) {
		start=a;
		end=b;
	}
	public String debugOut() {
		String s="Line start: "+start.xyToString()+", line end: "+end.xyToString();
		return s;
	}
	public void drawLine(char[][] cave, int indent) {
		int n=0;
		if(start.x==end.x) {
			// horizontal line
			if(end.y<=start.y) {
				while(end.y+n<=start.y) {
					cave[end.y+n][end.x-indent]='#';
					n++;
				}
			} else {
				while(end.y-n>=start.y) {
					cave[end.y-n][end.x-indent]='#';
					n++;
				}
			}
		} else {
			if(end.x<=start.x) {
				while(end.x+n<=start.x) {
					cave[end.y][end.x+n-indent]='#';
					n++;
				}
			} else {
				while(end.x-n>=start.x) {
					cave[end.y][end.x-n-indent]='#';
					n++;
				}
			}
		}
	}
}
