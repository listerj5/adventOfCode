package jrl;

import java.util.ArrayList;

class Blueprint {
	Robot oreBot;
	Robot clayBot;
	Robot obBot;
	Robot geoBot;
	int maxOre=1;
	int maxClay=1;
	int maxOb=1;
	ArrayList<Robot> canBuild=new ArrayList<Robot>();
	
	
	public Blueprint(int[] ore, int[] clay, int[] oBot, int[] geo)
	{
		oreBot=new OreBot();
		oreBot.setBuildCost(ore);
		clayBot=new ClayBot();
		clayBot.setBuildCost(clay);
		if(clay[0]>maxOre) maxOre=clay[0];
		obBot=new ObsidianBot();
		obBot.setBuildCost(oBot);
		if(oBot[0]>maxOre) maxOre=oBot[0];
		geoBot=new GeodeBot();
		geoBot.setBuildCost(geo);
		if(geo[0]>maxOre) maxOre=geo[0];
		maxClay=oBot[1];
		maxOb=geo[2];
	}
	public ArrayList<Robot> canBuild(int[] ore, int[] robots, int time) {
		canBuild.clear();
		if(geoBot.canBuild(ore)) {
			canBuild.add(geoBot);
			//System.out.println("can build geode bot");
		} else if(time>(AdventDay19.timeLimit-2)) return canBuild; // don't bother as it won't make a difference
	
		if(obBot.canBuild(ore) && robots[2]<maxOb) {
			canBuild.add(obBot);
			//System.out.println("can build Obsidian bot");
		} else if(time>(AdventDay19.timeLimit-3)) return canBuild; // don't bother, if you can't build ob bot now then it won't affect whether you can build geoBot
		
		if(clayBot.canBuild(ore) && robots[1]<maxClay) {
			canBuild.add(clayBot);
			//System.out.println("can build clay bot");
		} else if(time>(AdventDay19.timeLimit-4)) return canBuild;
		
		
		if(oreBot.canBuild(ore) && robots[0]<maxOre) {
			canBuild.add(oreBot);
			//System.out.println("can build ore bot");
		}
		return canBuild;
	}
}

