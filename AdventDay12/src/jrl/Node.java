package jrl;

import java.util.ArrayList;

public class Node {
	static char[][] mazePointer;
	static Node[][] nodePointer;
	static int finishX;
	static int finishY;
	String bestRoute;
	int routeCost;
	int x;
	int y;
	public Node(int x, int y, String route, int routeCost) {
		this.x=x;
		this.y=y;
		this.bestRoute=route;
		this.routeCost=routeCost;
	}
	public void updateRouteCost(String route, int routeCost) {
		this.bestRoute=route;
		this.routeCost=routeCost;
	}
	static void setMaze(char[][] maze, Node[][] nodes, int finX, int finY) 
	{
		mazePointer=maze;
		finishX=finX;
		finishY=finY;
		nodePointer=nodes;
	}
	public void doCheck() {
		int targetX;
		int targetY;
		char nextDirection='E';
		char currentSpot=mazePointer[y][x];
		//AdventDay12.writeToWindow("X", x+(y*(AdventDay12.gridWidth+1)));
		
		if(currentSpot=='{') {
			System.out.println("FINISHED!!");
			System.out.println("route="+bestRoute);
			System.out.println("route length="+bestRoute.length());
			AdventDay12.bestRoute.add(bestRoute);
		}
		// try directions - east first, then west, then south then north
		
		targetX=x+1;
		targetY=y;		
		if(targetInBounds(targetX,targetY)){
			if(((mazePointer[targetY][targetX])-1)<=currentSpot && (mazePointer[targetY][targetX])!='a'){
				if(nodePointer[targetY][targetX]==null) { // not been here before
					//System.out.println("adding node");
					AdventDay12.addNode(targetX, targetY, bestRoute+nextDirection,routeCost+1);
				}
				else if(nodePointer[targetY][targetX].routeCost>routeCost+1) {
					//System.out.println("Overwriting route at "+targetX+","+targetY+" old cost="+nodePointer[targetY][targetX].routeCost+" new="+routeCost+1);
					nodePointer[targetY][targetX].updateRouteCost(bestRoute+nextDirection,routeCost+1);
					AdventDay12.activateNode(nodePointer[targetY][targetX]);
				}
			}
		}
		nextDirection='W';
		targetX=x-1;
		if(targetInBounds(targetX,targetY)){
			if(((mazePointer[targetY][targetX])-1)<=currentSpot && (mazePointer[targetY][targetX])!='a'){
				if(nodePointer[targetY][targetX]==null) { // not been here before
					//System.out.println("adding node");
					AdventDay12.addNode(targetX, targetY, bestRoute+nextDirection,routeCost+1);
				}
				else if(nodePointer[targetY][targetX].routeCost>routeCost+1) {
					//System.out.println("Overwriting route at "+targetX+","+targetY+" old cost="+nodePointer[targetY][targetX].routeCost+" new="+routeCost+1);
					nodePointer[targetY][targetX].updateRouteCost(bestRoute+nextDirection,routeCost+1);
					AdventDay12.activateNode(nodePointer[targetY][targetX]);
				}
			}
		}
		targetX=x;
		targetY=y+1;
		nextDirection='S';
		if(targetInBounds(targetX,targetY)){
			if(((mazePointer[targetY][targetX])-1)<=currentSpot && (mazePointer[targetY][targetX])!='a'){
				if(nodePointer[targetY][targetX]==null) { // not been here before
					//System.out.println("adding node");
					AdventDay12.addNode(targetX, targetY, bestRoute+nextDirection,routeCost+1);
				}
				else if(nodePointer[targetY][targetX].routeCost>routeCost+1) {
					//System.out.println("Overwriting route at "+targetX+","+targetY+" old cost="+nodePointer[targetY][targetX].routeCost+" new="+routeCost+1);
					nodePointer[targetY][targetX].updateRouteCost(bestRoute+nextDirection,routeCost+1);
					AdventDay12.activateNode(nodePointer[targetY][targetX]);
				}
			}
		}
		targetY=y-1;
		nextDirection='N';
		if(targetInBounds(targetX,targetY)){
			if(((mazePointer[targetY][targetX])-1)<=currentSpot && (mazePointer[targetY][targetX])!='a'){
				if(nodePointer[targetY][targetX]==null) { // not been here before
					//System.out.println("adding node");
					AdventDay12.addNode(targetX, targetY, bestRoute+nextDirection,routeCost+1);
				}
				else if(nodePointer[targetY][targetX].routeCost>routeCost+1) {
					//System.out.println("Overwriting route at "+targetX+","+targetY+" old cost="+nodePointer[targetY][targetX].routeCost+" new="+routeCost+1);
					nodePointer[targetY][targetX].updateRouteCost(bestRoute+nextDirection,routeCost+1);
					AdventDay12.activateNode(nodePointer[targetY][targetX]);
				}
			}
		}
	}
	public String debugOut() {
		String s="Loc:"+x+","+y+" - best route "+bestRoute+" - route length"+routeCost;
		return s;
	}
	boolean targetInBounds(int tX, int tY) {
		return (tX<=mazePointer[0].length-1 && tX>=0 && tY<=mazePointer.length-1 && tY>=0);
	}
}
