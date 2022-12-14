package jrl;

import java.util.ArrayList;

public class MyList extends Thing {
	public ArrayList<Thing> lists;
	public String inputData;
	// ArrayList<Integer> numbers;
	// ArrayList<Boolean> indexIsNumber;
	public MyList (String contents) {
		inputData=contents;
		isList=true;
		// constructor will be passed [something,something] where something could be an integer or a list, a list could be integers or lists or both
		// 
		int i;
		for(i=1;i<contents.length()-1;i++) { // loop within the inner bracket
			if(contents.charAt(i)=='[') {
				// start of a new list - need to find the end of this
				if(lists==null) lists=new ArrayList<Thing>();
				int depth=0;
				int j=1;
				
				// the loop here needs to loop until depth==0 and ] 
				// so if 
				boolean done=(depth==0 && contents.charAt(i+j)==']');
				while(!done) {
					if(contents.charAt(i+j)=='[') depth++;
					else if(contents.charAt(i+j)==']') depth--;
					j++;
					done=(depth==0 && contents.charAt(i+j)==']');
				}
				//System.out.println("adding list with string "+contents.substring(i,i+j+1));
				lists.add(new MyList(contents.substring(i,i+j+1)));
				i+=(j+1);
				
			} else {
				if(lists==null) lists=new ArrayList<Thing>();
				if((i==contents.length()-2) || contents.charAt(i+1)==',')  
				{
					lists.add(new MyInteger(Integer.parseInt(contents.substring(i,i+1))));
					i++;
				} else {
					lists.add(new MyInteger(Integer.parseInt(contents.substring(i,i+2))));
					i+=2;
				}
			} 
		}
		//System.out.println("Contents of list:");
		if(lists==null) {
			//System.out.println("List is empty");
		} else {
			//System.out.println("List is "+lists.size()+" long");
			for(i=0;i<lists.size();i++) {
				//lists.get(i).textOutput();
			}
		}
	}
	public void textOutput() {
		if(lists==null) {
			System.out.println("Null list");
			return;
		} else if(lists.size()==0) {
			System.out.println("zero length list");
			return;
		}
		System.out.println("list of length "+lists.size());
		int i;
		for(i=0;i<lists.size();i++) {
			lists.get(i).textOutput();
		}
	}
	public ArrayList<Thing> getList(){
		return lists;
	}
	
	
	
	// comparison rules
	
	// list vs list compare
	// compare values until values are not equal or one list runs out
	// list vs int compare - convert int to list of length 1 and re-run comparison
	
	
	
	
	
	public int compareLists (Thing thing) { // assume this is left list asking the compare - uncomment the System.out.println to enable debugging
		//System.out.println("Comparing "+inputData+ " with "+thing.getInputData());
		if(this.lists==null && thing.getList()!=null) {
			//System.out.println("List compare - this list is null, other one is not, returning true");
			return 1;
		}
		else if(thing.getList()==null && this.lists!=null) {
			//System.out.println("List compare - this list not null and target list is null, returning false");
			return -1;
		} else if (thing.getList()==null && this.lists==null){
			return 0;
		} else {
			int i=0;
			int rValue=0;
			while(i<lists.size() && rValue==0) {
				if(i>=thing.getList().size()) {
					//System.out.println("List compare - target list ran out of values, returning false");
					return -1;
				} else {
					if(lists.get(i).isList && thing.getList().get(i).isList) {
						rValue=lists.get(i).compareLists(thing.getList().get(i));
					} else if (lists.get(i).isList && !thing.getList().get(i).isList) {
						rValue=lists.get(i).compareLists(thing.getList().get(i).convertToList());
					} else if (!lists.get(i).isList && thing.getList().get(i).isList) {
						rValue=lists.get(i).convertToList().compareLists(thing.getList().get(i));
					} else {
						rValue=lists.get(i).compare(thing.getList().get(i));
					}
				}
				i++;
			}
			if(rValue!=0) return rValue;
			
			if(thing.getList().size()>i) 
			{
				return 1;
			}
			else 
			{ 
				return 0;
			}
		}
	}
	public Thing convertToList() {
		return this;
	}
	public int compare(Thing thing) {
		System.out.println("ERROR CONDITION - trying to integer compare for a list");
		return 0;
	}
	public String getInputData() {return inputData;}
	public int getValue() 
	{ 
		System.out.println("ERROR CONDITION");
		return 0;
	}
}
