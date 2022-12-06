/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventday6;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author James
 */
public class AdventDay6 {

    /**
     * @param args the command line arguments
     */
    
    static BufferedReader in;    
        
    static String inputFileLocation="G:/tempdata/advent6.txt";
    
    public static void main(String[] args) {
        
    
        try {
            in = new BufferedReader(new InputStreamReader(
              new FileInputStream(inputFileLocation)));
            int input;
            String char4="";
            int count=0;
            while ((input = in.read()) != -1) {
                int ch = (char) input;
                if (Character.isLetter(ch)) {
                    count++;
                    if(count<4){
                        char4=char4+(char)ch;
                    } else {
                        
                    }
                
                
                
                }
                
                
            }
        }         
        catch(IOException ex)
        {

        }
    }
}
