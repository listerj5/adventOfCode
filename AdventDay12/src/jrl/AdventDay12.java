package jrl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class AdventDay12 {
	final static boolean debugging=false;
	//static ArrayList<Node> newNodes=new ArrayList<Node>(); // keep track of new nodes
	static ArrayList<Node> activeNodes=new ArrayList<Node>(); // nodes active on a particular cycle (i.e. checking adjacent nodes for next step)
	static Node[][] nodes; // store node data here (for each maze / height map square, the best route to the node) 
	static BufferedReader in;    
	static ArrayList<String> bestRoute=new ArrayList(); // store answers from nodes in here (routes to finish)
	static boolean ready=false;
	static int gridHeight;
	static int gridWidth;
	static int startX=0, startY=0, finishX=0, finishY=0;
	static String inputFileLocation="C:/users/701583673/adventDay12.txt";
	public static void main(String[] args) {
		String nextline;
		ArrayList<String> data=new ArrayList(); // store maze in here from file
		char[][] maze; // store maze in here once processed
		char[][] visited; // track where you've been in here
				
		try {
			in=new BufferedReader(new FileReader(inputFileLocation));
			while((nextline=in.readLine())!=null) {
	            data.add(nextline); // read in the maze data line at a time
	        }
			
		} catch (IOException ex) {
			
		}
		gridWidth=data.get(0).length();
		gridHeight=data.size();
		maze=new char [gridHeight][gridWidth]; // store the raw maze data in here (height map)
		visited=new char[gridHeight][gridWidth]; // this is for visualisation of the route
		int x,y;
		for(y=0;y<gridHeight;y++) {
			for(x=0;x<gridWidth;x++) {
				maze[y][x]=data.get(y).charAt(x);
				if(maze[y][x]=='S') {
					startX=x;
					startY=y;
					maze[y][x]='a'; // note - overwrite start value with height a
				}
				if(maze[y][x]=='E') {
					finishX=x;
					finishY=y;
					maze[y][x]='{'; // note - set end to "{" which is 1 value higher than "z"
				}
				visited[y][x]=' ';
			}
		}
		
		nodes=new Node[gridHeight][gridWidth];
		nodes[startY][startX]=new Node(startX, startY,"",0);
		activeNodes.add(nodes[startY][startX]);
		Node.setMaze(maze, nodes, finishX, finishY);
		int n=0;
		while(activeNodes.size()>0) {
			activeNodes.get(0).doCheck();
			n++;
			if(n%100==0 && debugging) {
				System.out.println("Processed "+n+" nodes, currently "+activeNodes.size()+" being considered");
				System.out.println("last node:" + activeNodes.get(0).debugOut());
			}
			activeNodes.remove(0);	
		}
		
		// to visualise best route for part 1, uncomment this line
		
		displayRoute(maze,visited,bestRoute.get(0),startX, startY);
		
		
				
		
		// part 2 - select new starting spots with all locations with letter a
		for(y=0;y<gridHeight;y++) {
			for(x=0;x<gridWidth;x++) {
				if(maze[y][x]=='a') {
					startX=x;
					startY=y;
					nodes=new Node[gridHeight][gridWidth];
					nodes[startY][startX]=new Node(startX, startY,"",0);
					activeNodes.add(nodes[startY][startX]);
					Node.setMaze(maze, nodes, finishX, finishY);
					if(debugging) System.out.println("Starting new maze at "+startX+","+startY);
					while(activeNodes.size()>0) {
						activeNodes.get(0).doCheck();
						activeNodes.remove(0);	
					}
				}
			}
		}
		
		int shortestSolution=bestRoute.get(0).length();
		String shortSol="";
		for(String s:bestRoute) {
			if(s.length()<shortestSolution) {
				shortestSolution=s.length();
				shortSol=s;
			}
		}
		System.out.println("Shortest solution for part 2="+shortestSolution);
		
	}
	static public void addNode(int x, int y, String route, int distance) {
		nodes[y][x]=new Node(x,y,route,distance);
		activeNodes.add(nodes[y][x]);
	}
	static public void activateNode(Node n) {
		activeNodes.add(n);
	}
	
	static void displayRoute(char[][]maze, char[][]visited, String s, int x, int y) {
		int i=0;
		for(char[] c:visited) {
			Arrays.fill(c, '.');
		}
		
		while(i<s.length()) {
			visited[y][x]='O';
			if(s.charAt(i)=='E') {
				x++;
			} else if(s.charAt(i)=='W') {
				x--;
			} else if(s.charAt(i)=='N') {
				y--;
			} else if(s.charAt(i)=='S') {
				y++;
			}
			i++;
		}
		for(y=0;y<gridHeight;y++) {
			String display="";
			for(x=0;x<gridWidth;x++) {
				if(visited[y][x]=='O') display+="O";
				else display+=maze[y][x];
			}
			System.out.println(display);
		}
	}
}
