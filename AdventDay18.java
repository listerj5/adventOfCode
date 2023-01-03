package jrl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AdventDay18 {
	
	static class Cube{
		int[] xyz=new int[3];
		int total;
		int faces=6;
		ArrayList<Cube> touching=new ArrayList();
		public Cube(int x, int y, int z) {
			xyz[0]=x;
			xyz[1]=y;
			xyz[2]=z;
			total=x+y+z;
		}
		public void touchFound(Cube c) {
			faces--;
			touching.add(c);
		}
	}

	static BufferedReader in;    
	static String inputFileLocation="C:/users/701583673/adventDay18.txt";
	//static String inputFileLocation="C:/users/701583673/day13_test.txt";
	public static void main(String[] args) {
		String nextline;
		String[] split;
		ArrayList<Cube> cubes=new ArrayList();
		try {
			in=new BufferedReader(new FileReader(inputFileLocation));
			while((nextline=in.readLine())!=null) {
				split=nextline.split(",");
				cubes.add(new Cube(Integer.parseInt(split[0])+1,Integer.parseInt(split[1])+1,Integer.parseInt(split[2])+1));
	        }
		} catch (IOException ex) {
			
		}
		for(Cube c1:cubes) {
			for(Cube c2:cubes) {
				if(c1!=c2 && !c1.touching.contains(c2)) { // check if already checked
					if(c1.total==c2.total+1 || c1.total==c2.total-1) { // chance of a match
						if(c1.xyz[0]==c2.xyz[0] && c1.xyz[1]==c2.xyz[1]) {
							c1.touchFound(c2);
							c2.touchFound(c1);
						} else if (c1.xyz[0]==c2.xyz[0] && c1.xyz[2]==c2.xyz[2]) {
							c1.touchFound(c2);
							c2.touchFound(c1);
						} else if (c1.xyz[1]==c2.xyz[1] && c1.xyz[2]==c2.xyz[2]) {
							c1.touchFound(c2);
							c2.touchFound(c1);
						}
						
					}
				}
			}
			
		}
		int facesSeen=0;
		int xmin=100;
		int xmax=0;
		int ymin=100;
		int ymax=0;
		int zmin=100;
		int zmax=0;
		for(Cube c1:cubes) {
			facesSeen+=c1.faces;
			if(c1.xyz[0]<xmin) xmin=c1.xyz[0];
			if(c1.xyz[0]>xmax) xmax=c1.xyz[0];
			
			if(c1.xyz[1]<ymin) ymin=c1.xyz[1];
			if(c1.xyz[1]>ymax) ymax=c1.xyz[1];
			
			if(c1.xyz[2]<zmin) zmin=c1.xyz[2];
			if(c1.xyz[2]>zmax) zmax=c1.xyz[2]; // min max is 0-19 for each dimension, so could start at 20,20,20 and work inwards from there (back to zero, though the cube at zero would potentially have a face into -1)
			
		}
		System.out.println("num faces seen=" +facesSeen);
		System.out.println("min maxs "+xmin+","+xmax+" "+ymin+","+ymax+" "+zmin+","+zmax);
		
		boolean[][][] done=new boolean[22][22][22];
		boolean xpos, xneg, ypos,yneg,zpos,zneg;
		
		ArrayList<Cube> air=new ArrayList();
		air.add(new Cube(0,0,0));
		done[0][0][0]=true;
		int eFaces=0;
		
		while(air.size()>0) {
			xpos=xneg=ypos=yneg=zpos=zneg=false;
			System.out.println("evaluating cube at "+air.get(0).xyz[0]+","+air.get(0).xyz[1]+","+air.get(0).xyz[2]);
			for(Cube c:cubes) {
				
				if(c.total==air.get(0).total+1){ // c is +1 from air
					if(c.xyz[0]==air.get(0).xyz[0] && c.xyz[1]==air.get(0).xyz[1]) {
						//x an y same, z must be 
						zpos=true;
						eFaces++;
						System.out.println("face found zpos");
						
					} else if (c.xyz[0]==air.get(0).xyz[0] && c.xyz[2]==air.get(0).xyz[2]) {
						ypos=true;
						eFaces++;
						System.out.println("face found ypos");
						
					} else if (c.xyz[1]==air.get(0).xyz[1] && c.xyz[2]==air.get(0).xyz[2]) {
						xpos=true;
						eFaces++;
						System.out.println("face found xpos");
						
					}
				} else if (c.total==air.get(0).total-1) { // c is -1 from air 
					if(c.xyz[0]==air.get(0).xyz[0] && c.xyz[1]==air.get(0).xyz[1]) {
						zneg=true;
						eFaces++;
						System.out.println("face found zneg");
						
					} else if (c.xyz[0]==air.get(0).xyz[0] && c.xyz[2]==air.get(0).xyz[2]) {
						yneg=true;
						eFaces++;
						System.out.println("face found yneg");
						
					} else if (c.xyz[1]==air.get(0).xyz[1] && c.xyz[2]==air.get(0).xyz[2]) {
						xneg=true;
						eFaces++;
						System.out.println("face found xneg");
						
					}
				}
				
			}
			if(!xpos && air.get(0).xyz[0]<21 && !done[air.get(0).xyz[0]+1][air.get(0).xyz[1]][air.get(0).xyz[2]]) {
				air.add(new Cube(air.get(0).xyz[0]+1,air.get(0).xyz[1],air.get(0).xyz[2]));
				done[air.get(0).xyz[0]+1][air.get(0).xyz[1]][air.get(0).xyz[2]]=true;				
			}
			if(!xneg && air.get(0).xyz[0]>0 && !done[air.get(0).xyz[0]-1][air.get(0).xyz[1]][air.get(0).xyz[2]]) {
				air.add(new Cube(air.get(0).xyz[0]-1,air.get(0).xyz[1],air.get(0).xyz[2]));
				done[air.get(0).xyz[0]-1][air.get(0).xyz[1]][air.get(0).xyz[2]]=true;				
			}
			if(!ypos && air.get(0).xyz[1]<21 && !done[air.get(0).xyz[0]][air.get(0).xyz[1]+1][air.get(0).xyz[2]]) {
				air.add(new Cube(air.get(0).xyz[0],air.get(0).xyz[1]+1,air.get(0).xyz[2]));
				done[air.get(0).xyz[0]][air.get(0).xyz[1]+1][air.get(0).xyz[2]]=true;				
			}
			if(!yneg && air.get(0).xyz[1]>0 && !done[air.get(0).xyz[0]][air.get(0).xyz[1]-1][air.get(0).xyz[2]]) {
				air.add(new Cube(air.get(0).xyz[0],air.get(0).xyz[1]-1,air.get(0).xyz[2]));
				done[air.get(0).xyz[0]][air.get(0).xyz[1]-1][air.get(0).xyz[2]]=true;				
			}
			
			if(!zpos && air.get(0).xyz[2]<21 && !done[air.get(0).xyz[0]][air.get(0).xyz[1]][air.get(0).xyz[2]+1]) {
				air.add(new Cube(air.get(0).xyz[0],air.get(0).xyz[1],air.get(0).xyz[2]+1));
				done[air.get(0).xyz[0]][air.get(0).xyz[1]][air.get(0).xyz[2]+1]=true;				
			}
			if(!zneg && air.get(0).xyz[2]>0 && !done[air.get(0).xyz[0]][air.get(0).xyz[1]][air.get(0).xyz[2]-1]) {
				air.add(new Cube(air.get(0).xyz[0],air.get(0).xyz[1],air.get(0).xyz[2]-1));
				done[air.get(0).xyz[0]][air.get(0).xyz[1]][air.get(0).xyz[2]-1]=true;				
			}
			air.remove(0);
		}
		System.out.println("EFACES "+eFaces);
	}
}
