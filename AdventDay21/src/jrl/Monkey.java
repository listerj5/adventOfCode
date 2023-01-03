package jrl;

import java.util.ArrayList;

abstract class Monkey implements Shout, Listen {
	static class Add implements MFunction {
		public long doMaths(long a, long b) {return a+b;}
	}
	static class Sub implements MFunction {
		public long doMaths(long a, long b) {return a-b;}
	}
	static class Multi implements MFunction {
		public long doMaths(long a, long b) {return a*b;}
	}
	static class Div implements MFunction {
		public long doMaths(long a, long b) {return a/b;}
	}
	static class Equals implements MFunction {
		public long doMaths(long a, long b) {
			if(a==b) {
				System.out.println("NUMBERS ARE EQUAL");
				return 0L;
			}
			else return a-b;
		}
	}
	String name;
	ArrayList<Listen> listeners=new ArrayList();
	
	public void addShoutListener(Listen l) {
		this.listeners.add(l);
	}
	abstract public void listenToShout(long a, Monkey m);
	public void shout() {
		//System.out.println("Monkey "+name+ " shouts "+getShout());
		for(Listen l:listeners) {
			l.listenToShout(getShout(),this); // add answer in here
		}
	}
	abstract public long getShout();
	
}
