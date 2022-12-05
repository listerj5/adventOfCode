/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventday4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author James
 */
public class AdventDay4 {
        static String nextline;
        static BufferedReader in;        
        static String inputFileLocation="G:/tempdata/advent4.txt";
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int i;
        String elf[];
        String numbers[];
        int elf1Start,elf1End;
        int elf2Start,elf2End;
        try

        {

            in=new BufferedReader(new FileReader(inputFileLocation));
           
            int total=0;
            while((nextline=in.readLine())!=null) 

            {
                elf=nextline.split(",");
                numbers=elf[0].split("-");
                elf1Start=Integer.valueOf(numbers[0]);
                elf1End=Integer.valueOf(numbers[1]);
                numbers=elf[1].split("-");
                elf2Start=Integer.valueOf(numbers[0]);
                elf2End=Integer.valueOf(numbers[1]);
                
                // logic below over-writes part 1 logic
                if(elf1Start>=elf2Start && elf1Start<=elf2End){
                    System.out.println(nextline+ " 1st starts in second");
                    total++;
                }
                else if(elf1End>=elf2Start && elf1End<=elf2End){
                    System.out.println(nextline+ " 1st ends in second");
                    total++;
                }
                else if(elf2Start>=elf1Start && elf2Start<=elf1End) {
                    System.out.println(nextline+ " 2nd starts in first");
                    total++;
                }
                else if(elf2End>=elf1Start && elf2End<=elf1End) {
                    System.out.println(nextline+ " 2nd ends in first");
                    total++;
                }
                else{
                    System.out.println(nextline+ " not covered");
                }           
                System.out.println("total: "+total);   
            }
        }
        catch(IOException ex)
        {
                    
        }
    }
}
