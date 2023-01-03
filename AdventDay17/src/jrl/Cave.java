package jrl;

import java.util.ArrayList;
import java.util.Arrays;

public class Cave {
	char[][] contents=new char[1000000][AdventDay17.caveWidth];
	int maxRockHeight=0;
	public Cave() {
		Arrays.fill(contents[0],'#');
		int i;
		for(i=1;i<contents.length;i++) {
			Arrays.fill(contents[i],'.');
		}
	}
	public void addShape(Shape s) {
		int i,j;
		for(i=0;i<4;i++) {
			for(j=0;j<4;j++) {
				if(j+s.x<=AdventDay17.caveWidth-1 ) {
					if(contents[s.y+i][s.x+j]=='.' && s.shape[i].charAt(j)=='#') {
						contents[s.y+i][s.x+j]='#';
					} else if(s.shape[i].charAt(j)=='#') {
						System.out.println("ERROR CONDITION");
					}
				}
			}
		}
	}
	public void removeShape(Shape s) {
		int i,j;
		for(i=0;i<4;i++) {
			for(j=0;j<4;j++) {
				if(j+s.x<=AdventDay17.caveWidth-1 ) {
					if(s.shape[i].charAt(j)=='#') {
						contents[s.y+i][s.x+j]='.';
					}
				}
			}
		}
	}
	public void addRestingShape(Shape s) {
		
	}
}
