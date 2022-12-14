package jrl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class AdventDay14 {

	static BufferedReader in;    
	static String inputFileLocation="C:/users/701583673/adventDay14.txt";
	//static String inputFileLocation="C:/users/701583673/day14_test.txt";
	public static void main(String[] args) {
		String nextline;
		String[] points;
		PointXY lastPoint;
		PointXY nextPoint;
		int caveWidth=0;
		int caveHeight=0;
		int caveIndent=0;
		int sandStart=0;
		ArrayList<LineXY> lines=new ArrayList();
		int n;
		try {
			in=new BufferedReader(new FileReader(inputFileLocation));
			while((nextline=in.readLine())!=null) {
				points=nextline.split(" -> "); // split points on text ->
				lastPoint=new PointXY(points[0]); // PointXY can take string as constructor and parse
				for(n=1;n<points.length;n++){
					nextPoint=new PointXY(points[n]); 
					lines.add(new LineXY(lastPoint,nextPoint)); // add lines to arrayList (each line with two points, start and end)
					lastPoint=nextPoint;
				}
	        }
			
		} catch (IOException ex) {
			
		}
		
		// the cave could have lots of blank space, looks like most lines are within 30 spaces to left/right of sand ingress
		// for part 1 can set the cave edges to allow 1 width boundary to left, right and height
		// for part 2, solution will look like a pyramid of sand, width twice the height+1, centred on 500
		// dimensions below are for part 2, a bit overkill for part 1
		
		//PointXY.maxX+=2;
		//PointXY.minX-=2;
		//PointXY.maxY+=1;
		
		PointXY.maxY+=2; // increasing height of "play" area by 2 to allow for infinite floor area in part 2
		PointXY.maxX=500+2+PointXY.maxY;
		PointXY.minX=500-2-PointXY.maxY;
		
		
		
		caveWidth=PointXY.maxX-PointXY.minX;
		caveHeight=PointXY.maxY;
		caveIndent=PointXY.minX;
		sandStart=500-PointXY.minX;
		// note - end of part 1 sand simulation will occur when a sand particle reaches maxY (it will be descending into the abyss at this point)
		
		char[][] cave=new char[caveHeight][caveWidth];
		for(char[] c:cave) {
			Arrays.fill(c, '.');
		}
		System.out.println("Starting Cave");
		
		for(LineXY l:lines) {
			l.drawLine(cave, caveIndent);
		}
		cave[0][sandStart]='+';
		System.out.println("Indent="+caveIndent);
		drawCave(cave);
		
		System.out.println("===================");
		System.out.println("====ADDING SAND====");
		System.out.println("===================");
		
		
		boolean done=false;
		int counter=0;
		PointXY sand=null;
		
			while(true) {
				if(cave[0][sandStart]=='O') break; // part 2 - stop if the sand spawn point is blocked
				
				sand=new PointXY(sandStart,0); // otherwise spawn a new sand blob 
				Boolean moving=true;
				while(sand.y<caveHeight-1 && moving) {
					if(cave[sand.y+1][sand.x]=='.') sand.y++; // way down is free of obstruction
					else { // way down is not free of obstruction
						if(cave[sand.y+1][sand.x-1]=='.') { // check down left
							sand.y++;
							sand.x--;
						} else if (cave[sand.y+1][sand.x+1]=='.') { // check down right
							sand.y++;
							sand.x++;
						} else {
							moving=false;
							cave[sand.y][sand.x]='O';
							counter++; 
						}
					}
				}
				// below is part 2 code to stop the sand falling into the abyss
				// replaces "break" statement here which ends part 1
				//if(sand.y>=caveHeight-1) break; // sand has fallen into the abyss
				
				
				if(sand.y==caveHeight-1) {
					cave[sand.y][sand.x]='O';
					counter++; 
				}
				
			}
		
		drawCave(cave);
		System.out.println("Sand count="+counter);
	}
	static void drawCave(char[][] cave) {
		String s;
		int i,j;
		for(i=0;i<cave.length;i++) {
			s="";
			for(j=0;j<cave[i].length;j++) {
				s+=cave[i][j];
			}
			System.out.println(s);
		}
	}
}
