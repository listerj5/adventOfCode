package jrl;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;



public class AdventDay19 {

	static class Buildpath {
		static int ID=0;
		int sid;
		Blueprint bluePrint;
		int oreMined[]=new int[4];
		int timeElapsed=0;
		Robot nextRobot;
		int[] robots;
		boolean buildChecked=false;
		int nullCounts=0;
		
		public Buildpath(Blueprint bluePrint) {
			this.bluePrint=bluePrint;
			robots=new int[] {1,0,0,0};
			sid=ID;
			ID++;
		}
		public String debugOut() {
			String s="Time elapsed:"+timeElapsed+" robots:"+robots[0]+","+robots[1]+","+robots[2]+","+robots[3]+" mined:"+oreMined[0]+","+oreMined[1]+","+oreMined[2]+","+oreMined[3];
			return s;
		}
		public Buildpath makeNewPath (Buildpath b, Robot nextRobot) {
			Buildpath newb=new Buildpath(b.bluePrint);
			System.arraycopy(b.oreMined, 0, newb.oreMined, 0, b.oreMined.length);
			System.arraycopy(b.robots, 0, newb.robots, 0, b.robots.length);
			newb.timeElapsed=b.timeElapsed;
			newb.nextRobot=nextRobot;
			newb.buildChecked=true;
			if(b.nullCounts>0) newb.nullCounts=b.nullCounts-1; // this is the build path used for positive robot builds, previous code set null counter zero but rather than zero the null counter, just reduce it
			// this means that if null counter was at 2, it will drop back to 1 (which means build pattern will only ever be null-build-null-build, instead of null null build null null build)
			return newb;
		}
		public Buildpath makeNewPath (Buildpath b, Robot nextRobot,int nullCounts) {
			Buildpath newb=new Buildpath(b.bluePrint);
			System.arraycopy(b.oreMined, 0, newb.oreMined, 0, b.oreMined.length);
			System.arraycopy(b.robots, 0, newb.robots, 0, b.robots.length);
			newb.timeElapsed=b.timeElapsed;
			newb.nextRobot=nextRobot;
			newb.buildChecked=true;
			newb.nullCounts=nullCounts;
			return newb;
			
		}
		
		
		
		public void checkBuild() {
			
			if(buildChecked) return;
			
			ArrayList<Robot> options=bluePrint.canBuild(oreMined,robots, timeElapsed);
			int i;
			if(options.size()==0) {
				nextRobot=null;
			} else { // ***** the logic below was adjusted / amended a few times to try and keep the branches down to a minimum. The real puzzler for this challenge is around ore stockpiling 
				 // the other ores don't matter so much as only one type of bot uses them, but as all bots use ore, there needs to be the option to wait for a different bot to build
				
				if(robots[1]==0 && options.size()==2) { // no clay bots yet and there is a choice to make, max ore has not been reached else there would be only 1
					nextRobot=options.get(0); // take the ore path
					bpaths.add(makeNewPath(this,options.get(1))); // fork a waiting path to add clay bot
				} else if(options.size()==1 && robots[0]==bluePrint.maxOre) { // we're at max ore so build the bot you can build
					nextRobot=options.get(0); 
				} else if(options.size()>=2 && robots[0]==bluePrint.maxOre) { // can build clay+ob, ob and geode, clay+geode
					nextRobot=options.get(0);
					for(i=1;i<options.size();i++) {
						bpaths.add(makeNewPath(this,options.get(i)));
					}
				} else if(options.size()==1) {// ore is not maxed so you might want to wait
					// these if branches here are the trickiest bit of the puzzle
					// for part 1 there is sensitivity to the null route (i.e. stockpile for ore) as time is short and sometimes having a bit extra ore from not building a claybot / obsidian bot etc
					// enables the build of a higher value bot
					// for part 2 there is much less sensitivity to the null route as more time means generally most good solutions max out the ore bots as this enables building more claybots
					// which in turn allows more of higher value bots
					// so final code here represents results of a bit of trial and error - the part 1 logic attempts to minimise the number of times a null / stockpile will be added
					// the part 2 logic just wipes out the null path completely, which speeds the answer up greatly. if not, the part 2 logic will run for millions of branches
					/* see below - all 3 pt2 blueprints maxed out on ore bots at 4.
					 * answer for blueprint 1 = 31 quality score is 31
					Time elapsed:32 robots:4,7,7,7 mined:16,45,10,31
					answer for blueprint 2 = 9 quality score is 18
					Time elapsed:32 robots:4,11,8,3 mined:20,36,11,9
					answer for blueprint 3 = 19 quality score is 57
					Time elapsed:32 robots:4,11,8,5 mined:14,47,12,19
					 */ 
					
					
					
					// this fork represents ore robots not maxed out and options presented - in the first fork there is only one option, but it might be beneficial to wait
					// for e.g. extreme scenario this could be a geode bot but building it would exhaust ore which might be best spent on an obsidian bot to help make more geodes later
					// in terms of waiting, the only reason to wait is if ore is low, suitable threshold is 
					// if max ore=4, current ore =4, ore bots=1, you might want to wait up to 3 turns (build at 7, then add 1 ore so next time there is 4 again) 
					// there must be a better way of doing this, e.g. forecasting max ore at each build decision or pruning more vigorously
					
					nextRobot=options.get(0);
					if(AdventDay19.pt1 && nullCounts<(bluePrint.maxOre-robots[0]) && oreMined[0]<(2*bluePrint.maxOre)-robots[0]) { // try same logic here to limit forks - surely you would build thing if you had line of sight of enough ore for 2
						bpaths.add(makeNewPath(this,null,this.nullCounts+1)); 
					}
				} else if(options.size()>=2) {
					nextRobot=options.get(0);
					for(i=1;i<options.size();i++) {
						bpaths.add(makeNewPath(this,options.get(i)));
					}
					if(AdventDay19.pt1 && options.size()!=4 && nullCounts<(bluePrint.maxOre-robots[0]) && oreMined[0]<(2*bluePrint.maxOre)-robots[0]) { // commented this out for the longer part 2 - the best solutions don't wait around if you can build 2 bots
						bpaths.add(makeNewPath(this,null,this.nullCounts+1)); 
					}
				}
			}
		}
		public void mineOre() {
			oreMined[0]+=robots[0];
			oreMined[1]+=robots[1];
			oreMined[2]+=robots[2];
			oreMined[3]+=robots[3];
		}
		public void doBuild() {
			if(nextRobot!=null) {
				nextRobot.buildBot(oreMined);
				robots[nextRobot.getIndex()]++;
			}
			buildChecked=false;
			nextRobot=null;
		}
		
	}

	static BufferedReader in;    
	static ArrayList<Buildpath> bpaths=new ArrayList<Buildpath>();
	static int timeLimit=32;
	static String inputFileLocation="C:/users/701583673/adventDay19.txt";
	static boolean pt1=false;
	public static void main(String[] args) {
		
		Blueprint bluePrint1=new Blueprint(new int[] {4,0,0},new int[] {2,0,0},new int[] {3,14,0},new int[] {2,0,7}); // the test blueprints
		Blueprint bluePrint2=new Blueprint(new int[] {2,0,0},new int[] {3,0,0},new int[] {3,8,0},new int[] {3,0,12});
		String nextline;
		ArrayList<Blueprint> blueprints=new ArrayList();
		int[] geodeTest=new int[] {28,21,15,10,6,3,1,0,0,0,0};
		
		try {
			
			
			
			in=new BufferedReader(new FileReader(inputFileLocation));
			while((nextline=in.readLine())!=null) {
				
				//Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 11 clay. Each geode robot costs 4 ore and 8 obsidian.
				//	0       1   2    3    4     5   6  7     8   9    10    11   12 13   14    15       16    17  18 19  20 21  22    23   24     25    26  27 28 29 30  31
				String[] costs=nextline.split(" ");
				int[] ore=new int[3];
				int[] clay=new int[3];
				int[] ob=new int[3];
				int[] g=new int[3];
				ore[0]=Integer.parseInt(costs[6]);
				clay[0]=Integer.parseInt(costs[12]);
				ob[0]=Integer.parseInt(costs[18]);
				ob[1]=Integer.parseInt(costs[21]);
				g[0]=Integer.parseInt(costs[27]);
				g[2]=Integer.parseInt(costs[30]);
				blueprints.add(new Blueprint(ore,clay,ob,g));
				System.out.println("Blue print created with "+ore[0]+","+clay[0]+","+ob[0]+","+ob[1]+","+g[0]+","+g[2]);
				
	        }
			
		} catch (IOException ex) {
			
		}
		
		int b_num=0;
		int b_max=0;
		int quality=0;
		int[] answer=new int[3];
		String[] best=new String[blueprints.size()];
		
//		blueprints.clear();
//		blueprints.add(bluePrint1); for the test blueprints
//		blueprints.add(bluePrint2);
//		for(b_num=0;b_num<2;b_num++) {
		System.out.println("Do you want to run part 1 or part 2? (enter 1 or 2)");
		Scanner scanner=new Scanner(System.in);
		String input=scanner.next();
		
		if(input.equals("1")) {
			timeLimit=24;
			pt1=true;
			b_max=blueprints.size();
			answer=new int[blueprints.size()];
		} else if (input.equals("2")) {
			pt1=false;
			timeLimit=32;
			b_max=3;
			answer=new int[3];
		} else return;
		
		for(b_num=0;b_num<b_max;b_num++) { 
			
			bpaths.clear();
			bpaths.add(new Buildpath(blueprints.get(b_num)));
		
			int maxGeodes=0;
			int pruned=0;
			int pathsCount=0;
			
			while(bpaths.size()>0) {
			
				if(maxGeodes>0 && bpaths.get(0).timeElapsed>=(timeLimit-7)) {
					if(geodeTest[bpaths.get(0).timeElapsed-(timeLimit-7)]+bpaths.get(0).oreMined[3]+((timeLimit-bpaths.get(0).timeElapsed)*bpaths.get(0).robots[3])<maxGeodes) {
						pruned++;
						if(pruned%10000==0) {
							System.out.println("pruned "+pruned+ ", completed "+pathsCount+", paths still in play ="+bpaths.size());
							System.out.println(bpaths.get(0).debugOut());
						}
							
						bpaths.remove(0);
					}
					else {
						bpaths.get(0).checkBuild();
						bpaths.get(0).mineOre();
						bpaths.get(0).doBuild();
						bpaths.get(0).timeElapsed++;
						if(bpaths.get(0).timeElapsed==timeLimit) {
							if(bpaths.get(0).oreMined[3]>maxGeodes) {
								maxGeodes=bpaths.get(0).oreMined[3];
								System.out.println("new winner found - max is "+maxGeodes);
								best[b_num]=bpaths.get(0).debugOut();
							}
							bpaths.remove(0);
							pathsCount++;
						}
					}
				} else {
					bpaths.get(0).checkBuild();
					bpaths.get(0).mineOre();
					bpaths.get(0).doBuild();
					bpaths.get(0).timeElapsed++;
					if(bpaths.get(0).timeElapsed==timeLimit) {
						if(bpaths.get(0).oreMined[3]>maxGeodes) {
							maxGeodes=bpaths.get(0).oreMined[3];
							System.out.println("new winner found - max is "+maxGeodes);
							best[b_num]=bpaths.get(0).debugOut();
						}
						bpaths.remove(0);
						pathsCount++;
					}
				}
			
				if(pathsCount%10000==0) System.out.println("paths done="+pathsCount+ " paths still in play ="+bpaths.size());

			}
			System.out.println("number "+(b_num+1)+ " yielded "+maxGeodes);
			quality+=((b_num+1)*maxGeodes);
			answer[b_num]=maxGeodes;
		}
		int totalQual=0;
		for(b_num=0;b_num<answer.length;b_num++) {
			System.out.println("answer for blueprint "+(b_num+1)+" = "+answer[b_num]+" quality score is "+(b_num+1)*answer[b_num]);
			if(best[b_num]!=null) System.out.println(best[b_num]);
			totalQual+=(b_num+1)*answer[b_num];
		}
		System.out.println("total quality is "+totalQual);
	}
		
		
}
