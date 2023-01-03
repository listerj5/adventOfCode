package jrl;

public class MonkeyShouter extends Monkey{
	long s;
	public MonkeyShouter(String name, long s) {
		this.name=name;
		this.s=s;
	}
	public long getShout() {return s;} // simple monkey, just shouts out the number
	public void listenToShout(long a, Monkey m) {
		//monkey is a shouter, does nothing
	}

}
