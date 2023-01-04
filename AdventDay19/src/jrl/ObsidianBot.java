package jrl;

public class ObsidianBot extends Robot {
	void mineOre(int[] ore) {
		ore[2]++;
	}
	boolean canBuild(int[] ore) {
		return (ore[0]>=buildCost[0] && ore[1]>=buildCost[1]); 
	}
	void buildBot(int[] ore) {
		ore[0]-=buildCost[0];
		ore[1]-=buildCost[1];
	}
	int getIndex() {return 2;}
}
