package jrl;

public class MonkeyMath extends Monkey {
	Monkey m1;
	Monkey m2;
	String mName1;
	String mName2;
	MFunction func;
	long val1;
	long val2;
	boolean hear1=false;
	boolean hear2=false;
	public MonkeyMath(String name, String m1, String m2, char op) {
		this.name=name;
		this.mName1=m1;
		this.mName2=m2;
		if(op=='+') func=new Add();
		else if(op=='-') func=new Sub();
		else if(op=='*') func=new Multi();
		else if(op=='/') func=new Div();
	}
	
	public long getShout() {
		return func.doMaths(val1, val2);
	}
	public void listenToShout(long a, Monkey m) {
		if(m==m1) {
			val1=a;
			hear1=true;
		}
		if(m==m2) {
			val2=a;
			hear2=true;
		}
		if(hear1 && hear2) {
			//System.out.println("Monkey Math has heard both shouters ("+mName1+","+mName2+") who shouted "+m1.getShout()+","+m2.getShout());
			super.shout();
		}
		
	}
	public void setMonkey1(Monkey m) {
		this.m1=m;
		m.addShoutListener(this);
	}
	public void setMonkey2(Monkey m) {
		this.m2=m;
		m.addShoutListener(this);
	}
	public void reset() {
		hear1=hear2=false;
	}
}
