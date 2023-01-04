package jrl;

public class GeodeBot extends Robot {
	void mineOre(int[] ore) {
		ore[3]++;
	}
	boolean canBuild(int[] ore) {
		return (ore[0]>=buildCost[0] && ore[2]>=buildCost[2]);
	}
	void buildBot(int[] ore) {
		ore[0]-=buildCost[0];
		ore[2]-=buildCost[2];
	}
	int getIndex() {return 3;}
}
