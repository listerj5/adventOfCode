package jrl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AdventDay15 {
	static BufferedReader in;    
	static String inputFileLocation="C:/users/701583673/adventDay15.txt";
	//static String inputFileLocation="C:/users/701583673/day15_test.txt"; // try with test data first
	static boolean debugOut=false;
	static class Beacon{
		int x;
		int y;
		public Beacon(int x, int y) {
			this.x=x;
			this.y=y;
		}
		public boolean sameBeacon(Beacon b) {
			if(this.x==b.x && this.y==b.y) return true;
			else return false;
		}
	}
	static class Sensor{
		static int id;
		static int sensorBound=4000000; // for test data, change this to 20
		int x;
		int y;
		int sid;
		int range;// taxi cab range
		public Sensor(int x, int y, int range) {
			this.x=x;
			this.y=y;
			this.range=range;
			if (debugOut) System.out.println("added sensor with range "+range);
			sid=id;
			id++;
		}
		public FromTo coverageOfRow(int row) { // part one logic, returns how much sensor coverage for a given row
			if(y==row) return new FromTo(x-range,x+range); // centre of sensor is on the row so coverage is from x-range to x+range
			else if(y<row) {
				if(y+range<row) return null; // sensor is North of row in question and range means it doesn't reach south far enough
				else return new FromTo(x-((y+range)-row),x+((y+range)-row)); 
			}
			else { // y must be greater than row, sensor is south of row in question
				if(y-range>row) return null; //range of sensor means it doesn't reach the row in question
				else return new FromTo(x-(row-(y-range)),x+(row-(y-range)));
			}
		}
		public boolean inRange(int tx, int ty) {
			return ((abs(tx-x)+abs(ty-y))<=range);
		}
		public int distanceFrom3to6(int tx, int ty) {
			int rvalue=(((x+y+range)-(tx+ty))/2);
			if(debugOut) System.out.println("distanceFrom3to6 for "+sid+" with loc "+tx+","+ty+" = "+rvalue+", sensor is at "+x+","+y+" with range "+range);
			return rvalue;
		}
		
		public boolean check12to3(ArrayList<Sensor> sensors) {
			// the idea with this function is to run down the top right edge of each sensor area (from X,Y-range to X+range,Y) looking for intersections with other sensor areas
			// when an intersection is detected, jump forward the size of the overlap (to speed up the iteration of the loop) and check each sensor area again, repeating until you are outside the 4M range
			// or a co-ordinate is found with no intersection
			// set as boolean as originally thought would need to check the other edges of each range but I think this would only be the case if the distress beacon was at the boundary edges
			// and thankfully it isn't
			
			if(debugOut) System.out.println("Sensor "+sid+" at location "+x+","+y+" with range "+range+" checking 12to3 overlap");
			int i=0;
			int x1;
			int y1;
			while(i<=range+1) { // checking just beyond the edge, so need to look at range+1, so 
				x1=x+i;
				y1=y+i-(range+1);
				while(x1<0 || y1<0 || x1>sensorBound || y1>sensorBound) {
					if(x1<0) {
						if(debugOut) System.out.println("x off left edge, adjusting x");
						i+=0-x1; // make sure x is in bounds
						y1=y+i-(range+1);
						x1=x+i;
					}
					else if (x1>sensorBound) {
						if(debugOut) System.out.println("Sensor "+sid+" has reached right edge on 12to3 check");
						return true;	
					}
					
				
					if(y1<0) {
						if(debugOut) System.out.println("y off top edge, adjusting y");
						i+=0-y1; // make sure y is in bounds also
						x1=x+i;
						y1=y+i-(range+1);
					}
					else if (y1>sensorBound) {
						if(debugOut) System.out.println("Sensor "+sid+" has reached bottom edge on 12to3 check");
						return true;
					}
				}
				if(i<=range+1) {
					boolean intersectFound=false;
					for(Sensor s:sensors) {
						if(s!=this) {
							if(s.inRange(x1,y1)) {
								if(debugOut) System.out.println("Sensor "+sid+" 12to3 edge intersects with sensor "+s.sid+ " co-ordinates "+x1+","+y1);
								i+=s.distanceFrom3to6(x1,y1)+1;
								intersectFound=true;
								break;
							}		
						}
					}
					if(!intersectFound) {
						System.out.println("No intersect found for sensor "+sid+" at location "+x1+","+y1);
						return false;
					}
				}
			}
			return true;
		}
	}
	static class FromTo{ // somewhere to store the intersections of the sensor areas with a row
		int from;
		int to;
		public FromTo(int from, int to) {
			this.from=from;
			this.to=to;
		}
	}
	
	
	public static void main(String[] args) {
		String nextline;
		ArrayList<Sensor> sensors=new ArrayList<Sensor>();
		ArrayList<Beacon> beacons=new ArrayList<Beacon>();
		try {
			in=new BufferedReader(new FileReader(inputFileLocation));
			while((nextline=in.readLine())!=null) {// read in the data and create all of the beacons and sensors
				String[] s=nextline.split("=");
				int x1=Integer.parseInt(s[1].substring(0,s[1].indexOf(",")));
				int y1=Integer.parseInt(s[2].substring(0,s[2].indexOf(":")));
				int x2=Integer.parseInt(s[3].substring(0,s[3].indexOf(",")));
				int y2=Integer.parseInt(s[4]);
				
				if(beacons.size()==0) beacons.add(new Beacon(x2,y2));
				else {
					boolean newBeacon=true;
					Beacon b1=new Beacon(x2,y2);
					for(Beacon b2:beacons) {
						if(b1.sameBeacon(b2)) {
							newBeacon=false; // note as same beacon is scanned by multiple scanners, need to make sure only add the beacons once to the beacon list
							break;
						}
					}
					if(newBeacon) beacons.add(b1);
				}
				sensors.add(new Sensor(x1,y1,(abs(x1-x2)+abs(y1-y2))));	
	        }
			
			int targetRow=2000000; // for test data for part 1, change this to 10
			
			ArrayList <FromTo> coverage=new ArrayList<FromTo>(); // define row coverage arraylist - each entry is FromTo, which has start of coverage from a sensor and end of coverage
			for(Sensor s:sensors) {
				FromTo f=s.coverageOfRow(targetRow);
				if(f!=null) coverage.add(f);
			}
			Collections.sort(coverage,new SortFromTo()); // sort the list on smallest From value to largest
			
			int rowCover=0;
			int lastXCover=0;
			int i;
			for(i=0;i<coverage.size();i++) { // work along the list of FromTo values
				if(debugOut) System.out.println("Row cover currently "+rowCover);
				if(debugOut) System.out.println("Processing coverage area "+i+" from:"+coverage.get(i).from+" to "+coverage.get(i).to);
				if(i==0) {
					rowCover=1+(coverage.get(i).to-coverage.get(i).from); // first entry is easy - coverage is from "From" to "To"
					lastXCover=coverage.get(i).to;
				}
				else { // subsequent from-to values:
					if(coverage.get(i).from<=lastXCover) // new coverage starts within area already covered 
					{
						if(coverage.get(i).to<lastXCover) { // already covered by previous sensor, no new coverage
						// do nothing, for now
						} else { // extends coverage
							rowCover+=coverage.get(i).to-lastXCover;
							lastXCover=coverage.get(i).to;
						}
					} 
					else { // new coverage starts after gap with previous
						rowCover=1+(coverage.get(i).to-coverage.get(i).from);
						System.out.println("Gap detected at "+lastXCover+" to "+coverage.get(i).from);
						lastXCover=coverage.get(i).to;
						
					}
				}
			}
			System.out.println("Row cover (excluding beacons) is "+rowCover);
			for(Beacon b:beacons) {
				if(b.y==targetRow) {
					System.out.println("Beacon at "+b.x+","+b.y);
					rowCover--;
				}
			}
			// part 1 answer
			System.out.println("Row coverage is "+rowCover);
			
			
			// part 2:: assuming the 4Mx4M matrix is too big to check using part 1 (I never tried...) 
			
			// need to take each sensor in turn and work along the edge of the sensor (within the 0 to 4M bounds)
			
			for(Sensor s:sensors) {
				s.check12to3(sensors); // this checks the 12 to 3 clockface wise edge of each sensor area i.e. the top right edge
				// had originally thought I'd need to check 3to6, 6to9 and 9to12 as well, but on consideration, the Beacon Space will have sensor boundaries in all directions 
				// including below and to the left of it i.e. this test will pick up the 6to9 face of the blank distress beacon.				
			}
		} catch (IOException ex) {
			
		}
	}
	
	static class SortFromTo implements Comparator <FromTo>{
		public int compare(FromTo a, FromTo b) {
			return a.from-b.from;
		}
	}
	
	public static int abs(int value) {
		if (value<0) return (-1*value); else return value;
	}
}
