package jrl;

public class InspectAdd implements Inspect {
	int add;
	public InspectAdd(int addition) {
		this.add=addition;
	}
	public void CheckItem(Item i) {
		i.updateAdd(add);
	}
}
