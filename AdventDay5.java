/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventday5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author James
 */
public class AdventDay5 {

    static String currentline;
    static String nextline, nextline2, nextline3;
    static BufferedReader in;    
        
        static String inputFileLocation="G:/tempdata/advent5_start.txt";
        static String inputFile2Location="G:/tempdata/advent5_moves.txt";
        
        
    /**
     * @param args the command line arguments
     */
        
    static class Crate_Stack {
            ArrayList <String> crate_data=new ArrayList();
            public Crate_Stack(){
                
            }
            public void addToStack(String s){
                crate_data.add(s);
            }
            public String removeFromStack(){
                String last=crate_data.get(crate_data.size()-1);
                crate_data.remove(crate_data.size()-1);
                return last;
            }
            public String getIndex(int i){
                return crate_data.get(i);
            }
            public int getSize(){
                return crate_data.size();
            }
        }
    static Crate_Stack stacks[]=new Crate_Stack[9]; // if more than 9 crates needed then change this
    public static void main(String[] args) {
        int i;
        String working_crate;
        ArrayList <String> crate_data=new ArrayList();
        int j=0;
        try
        {
            // first, grab starting crate data to build crate stacks
            in=new BufferedReader(new FileReader(inputFileLocation));
            while((nextline=in.readLine())!=null) 
            {
                crate_data.add(nextline);
            }
            // ascii characters 65 to 90 for capital letters
            String working;
            for(j=0;j<9;j++){
                stacks[j]=new Crate_Stack();
            }
            
            for(i=1;i<=crate_data.size();i++){ // work through list back to front to build crate stacks as bottom of stack is last line
                working=crate_data.get(crate_data.size()-i);
                for(j=0;j<9;j++)
                {
                    if((int)working.charAt((j*4)+1)>=65 && (int)working.charAt((j*4)+1)<=90) // crate chars start at second char, spaced 4 apart, charAt is zero indexed
                    {
                        stacks[j].addToStack(Character.toString(working.charAt((j*4)+1)));
                    }
                }   
            }
            debugOut(); // check the stack looks as per input file
            
            in=new BufferedReader(new FileReader(inputFile2Location)); // open the instructions data
            while((nextline=in.readLine())!=null) // read instructions a linen at a time
            {
                int[] orders=new int[3]; // variables to enable primitive parsing of instruction data
                int[] start=new int[3];
                int[] numChars=new int[3];
                String s1, s2, s3;
                
                    start[0]=5; // number of moves starts at character 5
                    numChars[0]=nextline.indexOf(" from"); // might be >9 so find where next part of instruction starts
                    start[1]=nextline.indexOf("from")+5; // from column is 5 chars after "from"
                    numChars[1]=nextline.indexOf(" to"); // might be >9 in a later iteration
                    start[2]=nextline.indexOf("to")+3; // to column
                    numChars[2]=nextline.length(); // is last entry so ends at end of line
                    
                    s1=nextline.substring(start[0],numChars[0]); // get the numbers from the instruction line
                    s2=nextline.substring(start[1],numChars[1]);
                    s3=nextline.substring(start[2],numChars[2]);
                    
                    
                    
                    System.out.println("request to move "+s1+" from stack "+s2+" to stack "+s3); // check this is as expected
                
                    int moves=Integer.parseInt(s1); //parse the strings to ints (might be neater to include in s1-s3 code above)
                    int from=Integer.parseInt(s2);
                    int to=Integer.parseInt(s3);
                
                //PART 1 LOGIC - commented out to allow part 2 logic to run instead
                /*for(i=0;i<moves;i++)
                {
                    working_crate=stacks[from-1].removeFromStack(); // pick up one crate from source stack
                    stacks[to-1].addToStack(working_crate); // add crate to destination stack 
                }
                debugOut(); // check move has worked as expected
                */
                
                //PART 2 LOGIC
                String[] multi_move=new String[moves]; // part 1 vs part 2 difference - store all crates to moves at once in new multimove string array
                for(i=0;i<moves;i++)
                {
                    working_crate=stacks[from-1].removeFromStack();
                    multi_move[moves-i-1]=working_crate; // put multiple crates on the crane
                }
                for(i=0;i<moves;i++)
                {
                    stacks[to-1].addToStack(multi_move[i]); // unload all the crates to the new stack
                }
                debugOut();
                
            }
            
        }

        catch(IOException ex)
        {
                    
        }
    }
    static void debugOut() // function to print out current crate stacks for inspection
    {
        int biggest=0;
        int max_size=0;
        int i,j;
        for(i=0;i<stacks.length;i++) // find the biggest stack as this needs to appear on the first line
        {
            if(stacks[i].getSize()>max_size)
            {
                biggest=i;
                max_size=stacks[i].getSize();
            }
        }
        for(i=max_size-1;i>=0;i--) // workdown from the tallest stack to the bottom
        {
            String outLine="";
            for(j=0;j<stacks.length;j++)
            {
                if(stacks[j].getSize()>i) // check that current height does not exceed stack height
                {
                    outLine=outLine+"["+stacks[j].getIndex(i)+"]";
                }
                else
                {
                    outLine=outLine+"   ";
                }
            }
            System.out.println(outLine);
        }
    } 
           
}
