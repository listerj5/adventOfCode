package jrl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class AdventDay16 {
	
	static class Valve {
		String name;
		String[] valves;
		int flowRate;
		boolean open=false;
		ArrayList<Valve> connectedValves;
		ArrayList<Route> bestRoutes=new ArrayList<Route>();
		
		
		public Valve(String name, String valveString) {
			this.name=name;
			valves=valveString.split(", ");
		}
		public void linkValves(ArrayList<Valve> valveList) {
			connectedValves=new ArrayList<Valve>();
			for(String s:valves) {
				for(Valve v:valveList) {
					if(v.name.equals(s)) { 
						connectedValves.add(v);
						break;
					}
				}
			}
		}
		public void setFlowrate(int flow) {this.flowRate=flow;}
	}
	static class History {
		History previous;
		Route route;
		Valve location;
		int flowBankThisRoute=0;
		int totalFlowBank=0;
		int totalTime=0; // could be 1, needs checking
		public History(History h, Route r) {
			this.previous=h;
			this.route=r;
			this.totalTime=h.totalTime+r.cost;
			flowBankThisRoute=r.end.flowRate*(totalCycles-totalTime);
			totalFlowBank=h.totalFlowBank+flowBankThisRoute;
			location=r.end;
		}
		public History(Valve location) { // for starting
			this.location=location;
		}
		public boolean contains(Valve v) {
			if(route!=null && route.end==v) return true;
			else if (previous==null) return false;
			else return previous.contains(v);
		}
		public void add(Route r) {
			totalTime+=r.cost;
		}
		public String routeInfo() {
			if(previous!=null) {
				return (previous.routeInfo()+this.textOut());
			} else return this.textOut();
		}
		public String detailedRouteInfo() {
			if(previous!=null) {
				return (previous.detailedRouteInfo()+" "+route.route);
			} else {
				if(route==null) return "START"; else return route.route;
			}
		}
		public String textOut() {
			return (" | "+ location.name+ " ("+ location.flowRate+ ") time="+totalTime);
		}
	}
	static class DoubleAction{ // this for the elephant and human effort combined
		History a1; // keep track of the nodes that each has visited
		History a2;
		static int maxCost=4; // this is the max route cost allowed, so don't consider routes that are further away than this
		public DoubleAction(History a1, History a2) {
			this.a1=a1;
			this.a2=a2;
		}
		/*public DoubleAction(History a1, History a2, int maxCost) {  // considered having this constructor but decided to use static value for max route cost instead
			this(a1,a2);
			this.maxCost=maxCost;
		}*/
		public int getMinTime() {// this is used to work out how much time has been used (which in turn is used to drive how much left and max possible additional scoring)
			
			if(a1.totalTime<a2.totalTime) return a1.totalTime; // this pruning combined with simple (but overly optimistic) remaining score mechanism provides minimal pruning. 
			else return a2.totalTime;
			
			// alternative getMinTime for pruning (an approximation which is not 100% correct, risks missing some solutions but given scoring is maximally optimistic it seems to work and prune better
			//return (a1.totalTime+a2.totalTime)/2; // try this pruning, use average - there is high degree of optimism in prune targets, this average whilst not strictly correct is close enough
		}
		public DoubleAction doAction(ArrayList<DoubleAction> activeActions) {
			History next1=a1;
			History next2=a2;
			boolean freewheel; // freewheel is used to detect when no additional routes have been added, so effectively the solution is done and any remaining time the actor sits idle.
			if (a1!=null && a2!=null) { 
				if (a1.totalTime<=25 && a2.totalTime<=25) { // if both actors have time left, evaluate the pair together
					freewheel=true;
					for(Route r:a1.location.bestRoutes) { // pick another route
						if(r.cost>maxCost) break;
						if( a1.totalTime+r.cost<=totalCycles && !a1.contains(r.end) && !a2.contains(r.end)) { // not one already used by either actor
							next1=new History(a1,r);
							freewheel=false;
							for(Route r2:a2.location.bestRoutes) {
								if(r2.cost>maxCost) break;
								if(a2.totalTime+r2.cost<=totalCycles && !r2.end.equals(r.end) && !a1.contains(r2.end) && !a2.contains(r2.end)) {
									next2=new History(a2,r2);
									activeActions.add(new DoubleAction(next1, next2));
								}
							}
						}
					} // ideally if freewheel, would still try and find another route for r2, but given there will be two solution - elephant and man swapped this should be ok
					if (freewheel) return this;
					
				} else if (a1.totalTime<=25) { // if only one actor has enough time left only consider new routes for this actor
					// only add a route for a1
					freewheel=true;
					for(Route r:a1.location.bestRoutes) { // pick another route
						if(r.cost>maxCost) break;
						if(a1.totalTime+r.cost<=totalCycles && !a1.contains(r.end) && !a2.contains(r.end)) { // not one already used by either actor
							next1=new History(a1,r);
							activeActions.add(new DoubleAction(next1, a2));
							freewheel=false;
						}
					}
					if(freewheel) return this;
					
				} else if (a2.totalTime<=25) { // if only one actor has enough time left only consider new routes for this actor
					// only add a route for a2
					freewheel=true;
					for(Route r2:a2.location.bestRoutes) {
						if(r2.cost>maxCost) break;
						if(a2.totalTime+r2.cost<=totalCycles && !a1.contains(r2.end) && !a2.contains(r2.end)) {
							next2=new History(a2,r2);
							activeActions.add(new DoubleAction(a1, next2));
							freewheel=false;
						}
					}
					if(freewheel) return this;
						
				} else {
					return this; 
					
				}
			}
			return null;
		}
		public int getPressure() {
			return a1.totalFlowBank+a2.totalFlowBank;
		}
	}
	
	static class Action{
		History history;
		static int maxCost=5;
		static int pt1Cycles=30;
		boolean freeWheel;
		//int totalFlowRate=0;
		public Action(History h) { // removed flowrate
			this.history=h;
		}
		public Action doAction(ArrayList<Action> activeActions) {
			//System.out.println("Doing action at "+location.name+", time="+time);
			History next=history;
			if (history.totalTime>=30) return this;
			
			freeWheel=true;
			for(Route r:history.location.bestRoutes) {
				if(r.cost>maxCost) break;
				if(history.totalTime+r.cost<=pt1Cycles && !history.contains(r.end)) {
					next=new History(history,r);
					activeActions.add(new Action(next));
					freeWheel=false;
				}
			}
			if(freeWheel) return this;
			return null;
		}
		public int getPressure() {
			return history.totalFlowBank;
		}
	}
	static class RouteAction{
		Valve location;
		Valve last;
		Valve target;
		String history;
		int time;
		public RouteAction(Valve v, String s, int time, Valve target) {
			location=v;
			history=s;
			this.time=time;
			this.target=target;
		}
		public String doAction(ArrayList<RouteAction> activeActions) {
			if(location==target) return history;
			
			//System.out.println("Doing Route Action at "+location.name+", time="+time);
			
			if(time>30) return null;
			
			for(Valve v:location.connectedValves) {
				if(!history.contains(v.name)){
					activeActions.add(new RouteAction(v,history+"-"+v.name,time+1,target));
				} 
			}
			return null;
		}
	}
	static class Route{
		Valve start;
		Valve end;
		String route;
		int cost;
		public Route(Valve s, Valve e, String route) {
			this.start=s;
			this.end=e;
			this.route=route;
			String[] steps=route.split("-");
			this.cost=(steps.length)-1;
		}
	}
	static class RouteSorter implements Comparator <Route>{
		public int compare(Route r1, Route r2) {
			return r1.cost-r2.cost;
		}
	}
	static class RouteSorterPressure implements Comparator <Route> {
		public int compare(Route r1, Route r2) {
			return r2.end.flowRate-r1.end.flowRate;
		}
	}
	static class RouteSorterMax implements Comparator <Route> {
		public int compare(Route r1, Route r2) {
			return (r2.end.flowRate*(12-r2.cost))-(r1.end.flowRate*(12-r1.cost));
		}
	}
	
	static int totalCycles=26;
	static int[] pressures;
	static int[] pressurePairs;
	int totalFlowRate=0;
	static BufferedReader in;    
	static String inputFileLocation="C:/users/701583673/adventDay16.txt";
	static int[] flowDeltas=new int[totalCycles+1];
	static int[] flowTargets=new int[totalCycles+6]; // allow a bit of extra in case logic tries to add a route path which takes the total time over the time limit
		// these targets will be defined later by working out if in optimal case route ends up closest to the best valves what could be scored with remaining time / pressure 
		// so assumes for e.g. on part 2 pair logic that one actor is at the valve closest to best valve and the other is closest to second best (as it happens, route cost is 3 for both scenarios, pressure is 23 and 25) 
		// and repeat, assuming next best valve pair is again only 3 away (so wildly overly optimistic, but simple enough to have one array with values in to aid pruning)
	
	//static String inputFileLocation="C:/users/701583673/day16_test.txt";
	public static void main(String[] args) {
		String nextline;
		ArrayList<Valve> valves=new ArrayList<Valve>();
		ArrayList<Valve> usefulValves= new ArrayList<Valve>();
		ArrayList<Action> activeActions=new ArrayList<Action>();
		ArrayList<String> solutions=new ArrayList();	
		
		try {
			in=new BufferedReader(new FileReader(inputFileLocation));
			while((nextline=in.readLine())!=null) {
				String[] split1=nextline.split("="); 				// first challenge - read in data, initialise valves
				// valve name is always at chars 6 and 7 i.e. substring (6,8)
				// connected valves are comma separated
				int flowrate=Integer.parseInt(split1[1].substring(0,split1[1].indexOf(";")));
				String[] split2=split1[1].split("valve");
				String valString;
				if(split2[1].charAt(0)=='s') {
					valString=split2[1].substring(2);
				} else {
					valString=split2[1].substring(1);
				}
				Valve v=new Valve(split1[0].substring(6,8),valString);
				v.setFlowrate(flowrate);
				valves.add(v);
	        }
			
			for(Valve v:valves) {
				v.linkValves(valves);
			}
			for(Valve v:valves) {
				if(v.flowRate>0) usefulValves.add(v);
			}
			
			System.out.println("There are "+usefulValves.size()+" useful valves");
			
			Valve startV=getValve(valves, "AA");
					
			for(Valve v2:usefulValves) { // populate all routes from start valve to useful valves
				solutions.clear();
				if(startV!=v2) {
					// find shortest route to v2
					ArrayList<RouteAction> activeRoutes=new ArrayList<RouteAction>();
					activeRoutes.add(new RouteAction(startV,"",0,v2));
					while(activeRoutes.size()>0) {
						String outcome=activeRoutes.get(0).doAction(activeRoutes);
						if(outcome!=null) solutions.add(outcome+"-O");
						activeRoutes.remove(0);
					}
				}
				int i;
				ArrayList<Route> routes=new ArrayList();
				for(i=0;i<solutions.size();i++) {
					routes.add(new Route(startV,v2,solutions.get(i)));
				}
				if(routes.size()>0) {
					Collections.sort(routes,new RouteSorter()); // this sorts by shortest route first to the valve in question
					//*** uncomment to see complete routes list
					//System.out.println("Showing routes for start node "+v.name+" to "+v2.name);
					//for(Route r:routes) {
					//	System.out.println(r.route + " with cost "+r.cost);
					//}
					startV.bestRoutes.add(routes.get(0)); // add the best route to the valve in question
				}
			}
			System.out.println("Start valve has "+startV.bestRoutes.size()+ " routes");
			Collections.sort(startV.bestRoutes,new RouteSorterPressure()); // this sorts the valves - highest pressure first
			
			pressures=new int[startV.bestRoutes.size()]; // because start valve has all useful valves in list, use this list to also populate the pruning score count
			int temp,t;
			for(temp=0;temp<startV.bestRoutes.size();temp++) {
				System.out.println(startV.bestRoutes.get(temp).end.name+"-"+startV.bestRoutes.get(temp).end.flowRate+" - routeCost="+startV.bestRoutes.get(temp).cost);
				pressures[temp]=startV.bestRoutes.get(temp).end.flowRate;
			}
			pressurePairs=new int[7]; // pairs is used for part 2 - this works out the best pairs (by pressure) of valves and is used in simplified approximation of flow rate left
			for(temp=0;temp<pressurePairs.length;temp++) {
				pressurePairs[temp]=pressures[temp*2]+pressures[(temp*2)+1];
			}
			for(t=0;t<pressurePairs.length;t++) {
				for(temp=4+(t*3);temp<=flowDeltas.length;temp++) {
					flowDeltas[flowDeltas.length-temp]=flowDeltas[flowDeltas.length-temp]+(pressurePairs[t]*((temp-(3*t)-3)));
				}
			}
			Collections.sort(startV.bestRoutes,new RouteSorter()); // this sorts the valves - closest first
			
			
			for(Valve vx:usefulValves) { //; now do similar process but work out routes between useful valves
				for(Valve v2:usefulValves) {
					solutions.clear();
					if(vx!=v2) {
						// find shortest route to v2
						ArrayList<RouteAction> activeRoutes=new ArrayList<RouteAction>();
						activeRoutes.add(new RouteAction(vx,"",0,v2));
						while(activeRoutes.size()>0) {
							String outcome=activeRoutes.get(0).doAction(activeRoutes);
							if(outcome!=null) solutions.add(outcome+"-O");
							activeRoutes.remove(0);
						}
					}
					int i;
					ArrayList<Route> routes=new ArrayList();
					for(i=0;i<solutions.size();i++) {
						routes.add(new Route(vx,v2,solutions.get(i)));
					}
					if(routes.size()>0) {
						Collections.sort(routes,new RouteSorter());
						//System.out.println("Showing routes for "+vx.name+" to "+v2.name);
						//for(Route r:routes) {
						//	System.out.println(r.route + " with cost "+r.cost);
						//}
						
						vx.bestRoutes.add(routes.get(0));
						
					} else {
						// System.out.println("ISSUE - unreachable useful valve "+vx.name+","+v2.name);
					}
				}
				System.out.println("Valve "+vx.name+" has "+vx.bestRoutes.size()+ " routes");
				Collections.sort(vx.bestRoutes,new RouteSorter());
				for(Route r:vx.bestRoutes) System.out.println(r.end.name+"-"+r.end.flowRate+" - routeCost="+r.cost);
			}
		
			int solutionsCount=0;
			int bestSolution=0;
			Action bestRoutes1=null;
			boolean solutionImproving=true;
			totalCycles=30;
			while(solutionImproving) { // note best answer found with route cost of 7, so add "&& DoubleAction.maxCost<7" to this 'if' clause to skip watching program evaluate 5M routes
				Action.maxCost++;
				solutionImproving=false;
				activeActions.add(new Action(new History(startV))); // add the starting node
				System.out.println("started looking for solutions with max route cost of "+Action.maxCost);
				while(activeActions.size()>0) {
					
					//int tTest=dActionList.get(0).getMinTime(); // pruning logic here
					//if(tTest>7 && dActionList.get(0).getPressure()<flowTargets[tTest]) {
					//	pruned++;
					//	if(pruned%5000==0) System.out.println("Pruned: "+pruned+", in play="+dActionList.size()+", last prune="+dActionList.get(0).getPressure()+","+dActionList.get(0).a1.totalTime+","+dActionList.get(0).a2.totalTime);
	
					//}
					//else {
						
					Action outcome=activeActions.get(0).doAction(activeActions);
						if(outcome!=null) {
							solutionsCount++;
							if(solutionsCount%1000==0) System.out.println("Solutions: "+solutionsCount+", in play="+activeActions.size()+", last solution="+outcome.getPressure()); 
							if(outcome.getPressure()>bestSolution) {
								System.out.println("found solution with "+outcome.getPressure());
								bestSolution=outcome.getPressure();
								bestRoutes1=outcome;
								solutionImproving=true;
							}
						}
					
					activeActions.remove(0);
				}
			}
			
			System.out.println("Part 1 solution found with answer of "+bestSolution);
			
			

			Scanner reader = new Scanner(System.in); 
			System.out.println("Part 1 has been solved, enter Y or y to continue with part 2 or something else to stop the program");
			String input=reader.next();
			reader.close();
			
			
			if(!input.equals("Y") && !input.equals("y")) return;
			
			ArrayList<DoubleAction> dActionList=new ArrayList<DoubleAction>(); // this arraylist to store the current routes being worked on
			
			bestSolution=0;
			solutionsCount=0;
			int pruned=0;
			totalCycles=26;
			DoubleAction bestRoutes=null;
			
			solutionImproving=true; // loop whilst a better solution is found vs previous limit on route cost
			while(solutionImproving) { // note best answer found with route cost of 7, so add "&& DoubleAction.maxCost<7" to this 'if' clause to skip watching program evaluate 5M routes
				DoubleAction.maxCost++;
				solutionImproving=false;
				dActionList.add(new DoubleAction(new History(startV),new History(startV))); // add the starting node
				System.out.println("started looking for solutions with max route cost of "+DoubleAction.maxCost);
				while(dActionList.size()>0) {
					
					int tTest=dActionList.get(0).getMinTime(); // pruning logic here
					if(tTest>7 && dActionList.get(0).getPressure()<flowTargets[tTest]) {
						pruned++;
						if(pruned%5000==0) System.out.println("Pruned: "+pruned+", in play="+dActionList.size()+", last prune="+dActionList.get(0).getPressure()+","+dActionList.get(0).a1.totalTime+","+dActionList.get(0).a2.totalTime);
	
					}
					else {
						DoubleAction outcome=dActionList.get(0).doAction(dActionList);
						if(outcome!=null) {
							solutionsCount++;
							if(solutionsCount%1000==0) System.out.println("Solutions: "+solutionsCount+", in play="+dActionList.size()+", last solution="+outcome.getPressure()); 
							if(outcome.getPressure()>bestSolution) {
								System.out.println("found solution with "+outcome.getPressure());
								bestSolution=outcome.getPressure();
								bestRoutes=outcome;
								updateTargets(bestSolution,true);
								solutionImproving=true;
							}
						}
					}
					dActionList.remove(0);
				}
			}
			
			System.out.println("Best route was as follows, with score of "+bestSolution);
			System.out.println(bestRoutes.a1.routeInfo());
			System.out.println(bestRoutes.a2.routeInfo());
			System.out.println(bestRoutes.a1.detailedRouteInfo());
			System.out.println(bestRoutes.a2.detailedRouteInfo());
						
		} catch (IOException ex) {
			System.out.println("ERROR HAS OCCURED");
		}
	}
	static void updateTargets(int best, boolean pair) {
		
		int i;
		if(pair) {
			for(i=0;i<flowDeltas.length;i++) {
				flowTargets[i]=best-flowDeltas[i];
				if(flowTargets[i]<0) flowTargets[i]=0;
			}
			
		}
		
	}
	static Valve getValve(ArrayList<Valve> valves, String s) {
		for(Valve v:valves) {
			if(s.equals(v.name)) return v;
		}
		return null;
	}
}
