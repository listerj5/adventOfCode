package jrl;

public class ClayBot extends Robot {
	void mineOre(int[] ore) {
		ore[1]++;
	}
	boolean canBuild(int[] ore) {
		return ore[0]>=buildCost[0];
	}
	void buildBot(int[] ore) {
		ore[0]-=buildCost[0];
	}
	int getIndex() {return 1;}
}
