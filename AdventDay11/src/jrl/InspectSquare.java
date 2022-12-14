package jrl;

public class InspectSquare implements Inspect {
	public InspectSquare() {
	}
	public void CheckItem(Item i) {
		i.updateSquare();
	}
}
