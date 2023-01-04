package jrl;

public abstract class Robot {
	 // 0=Ore, 1=Clay, 2=Obsidian, 3=Geode 
	int[] buildCost;
	void setBuildCost(int[] buildCost) {this.buildCost=buildCost;}
	abstract void mineOre(int[] ore);
	abstract boolean canBuild(int[] ore);
	abstract void buildBot(int[] ore);
	abstract int getIndex();
}
