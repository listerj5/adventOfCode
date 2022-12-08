import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;





public class AdventDay8 {
	static BufferedReader in;    
	static String inputFileLocation="C:/users/701583673/adventDay8.txt";


	public static void main(String[] args) {
		String nextline;
		ArrayList <String> inputData=new ArrayList();
		
		try {
			in=new BufferedReader(new FileReader(inputFileLocation));
			while((nextline=in.readLine())!=null) {
	            inputData.add(nextline);
	        }
	
		} catch (IOException ex) {
			
		}
		int gridWidth=inputData.get(0).length();
		int gridHeight=inputData.size();
		int[][] trees=new int [gridHeight][gridWidth];
		int x,y;
		for(y=0;y<gridHeight;y++) {
			for(x=0;x<gridWidth;x++) {
				trees[y][x]=Integer.parseInt(Character.toString(inputData.get(y).charAt(x)));
			}
		}
		
		int total=0;
		for(y=0;y<gridHeight;y++) {
			for(x=0;x<gridWidth;x++) {
				
				// check each tree visibility to each edge
				// uncomment System.out.println statements to see workings and/or debug logic
				
				int temp;
				boolean tallEnough;

				temp=0;
				tallEnough=true;
				// left check
				while(temp<x)
				{
						if(trees[y][temp]>=trees[y][x]) {
							tallEnough=false;
							break;
						}
						temp++;
				}
				if(tallEnough) {
						//System.out.println("Tree at "+x+","+y+" of height "+trees[y][x]+ " tall enough for view from left");
						total++;
				}
				//check right
				if(!tallEnough) {
					temp=gridWidth-1;
					tallEnough=true;
					while(temp>x)
					{
						if(trees[y][temp]>=trees[y][x]) {
							tallEnough=false;
							break;
						}
						temp--;
					}
					if(tallEnough) {
						//System.out.println("Tree at "+x+","+y+" of height "+trees[y][x]+ " tall enough for view from right");
						total++;
					}	
				}
				//check to top
				if(!tallEnough) {
					temp=0;
					tallEnough=true;
					while(temp<y)
					{
						if(trees[temp][x]>=trees[y][x]) {
							tallEnough=false;
							break;
						}
						temp++;
					}
					if(tallEnough) {
						//System.out.println("Tree at "+x+","+y+" of height "+trees[y][x]+ " tall enough for view from top");
						total++;
					}
				}
				
				//down check
				if(!tallEnough)
				{
					temp=gridHeight-1;
					tallEnough=true;
					while(temp>y)
					{
						if(trees[temp][x]>=trees[y][x]) {
							tallEnough=false;
							break;
						}
						temp--;
					}
					if(tallEnough) {
						//System.out.println("Tree at "+x+","+y+" of height "+trees[y][x]+ " tall enough for view from bottom");
						total++;
					}
				}		
			}
		}
		System.out.println("Total in view="+total);
		
		// part two - loop through trees again, note edges will have score of zero as one direction will have view=0 and 0 x (something)=0
		int left=0;
		int right=0;
		int top=0;
		int bottom=0; // view result from each angle, in case part 2 requires this
		
		
		int top_score=0;
		int tree_score;
		int best_x=0;
		int best_y=0;
		int temp;
		for(y=1;y<gridHeight-1;y++) { // ignore edges
			for(x=1;x<gridWidth-1;x++) {
				left=0;
				right=0;
				top=0;
				bottom=0;
				
				//look left
				temp=x-1;
				while(temp>=0) {
					left++;
					if(trees[y][temp]>=trees[y][x]) break;
					temp--;
				}
				//System.out.println("Tree at "+x+","+y+" can see "+left+" trees to the left");
				
				temp=x+1;
				while(temp<=gridWidth-1) {
					right++;
					if(trees[y][temp]>=trees[y][x]) break;
					temp++;
				}
				//System.out.println("Tree at "+x+","+y+" can see "+right+" trees to the right");
				
				temp=y-1;
				while(temp>=0) {
					top++;
					if(trees[temp][x]>=trees[y][x]) break;
					temp--;
				}
				//System.out.println("Tree at "+x+","+y+" can see "+top+" trees to the top");
				
				temp=y+1;
				while(temp<=gridHeight-1) {
					bottom++;
					if(trees[temp][x]>=trees[y][x]) break;
					temp++;
				}
				//System.out.println("Tree at "+x+","+y+" can see "+bottom+" trees to the bottom");
				
				tree_score=(left*right*top*bottom);
				//System.out.println("Tree score="+tree_score); 
				if(tree_score>top_score) {
					top_score=tree_score;
					best_x=x;
					best_y=y;
				}
				
				
				
			}
		}
		System.out.println("top score "+top_score+ " is tree "+best_x+","+best_y);
		
	}

}
