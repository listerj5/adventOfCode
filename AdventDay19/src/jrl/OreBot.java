package jrl;

public class OreBot extends Robot {
	void mineOre(int[] ore) {
		ore[0]++;
	}
	boolean canBuild(int[] ore) {
		return ore[0]>=buildCost[0];
	}
	void buildBot(int[] ore) {
		ore[0]-=buildCost[0];
	}
	int getIndex() {return 0;}
}
