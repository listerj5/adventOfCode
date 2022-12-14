package jrl;

public class InspectMultiply implements Inspect {
	int multi;
	public InspectMultiply(int multi) {
		this.multi=multi;
	}
	public void CheckItem(Item i) {
		i.updateMultiply(multi);
	}
}
