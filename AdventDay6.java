/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.adventday6;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author james
 */
public class AdventDay6 {
    
    static BufferedReader in;    
    static String inputFileLocation="C:/users/james/advent6.txt";
    
    public static void main(String[] args) {
        int i;
        boolean codeFound;
        try {
            in = new BufferedReader(new InputStreamReader(
              new FileInputStream(inputFileLocation)));
            int input;
            String char4="";
            String char14="";
            int count=0;
            while ((input = in.read()) != -1) {
                int ch = (char) input;
                if (Character.isLetter(ch)) {
                    count++;
                    if(count<=4){// code - first part
                        char4=char4+(char)ch;
                    } else {
                        char4=char4.substring(1); // new string, use last 3 chars of previous string
                        char4+=(char)ch; // add the new character to the end
                        codeFound=true; // assume all chars unique until multiple chars of same type found
                        for(i=0;i<char4.length();i++) //look for multiple occurances of each char in the string
                        {
                            if(countCharInString(char4.charAt(i),char4)>1){
                                codeFound=false;
                                break;
                            }
                        }
                        if(codeFound){// answer to first part
                            //System.out.println("Code "+char4+ " found after "+count+" chars read");
                        }
                    }
                    if(count<=14){ // code - second part
                        char14+=(char)ch;  
                    } else {
                        char14=char14.substring(1);
                        char14+=(char)ch;
                        codeFound=true;
                        for(i=0;i<char14.length();i++)
                        {
                            if(countCharInString(char14.charAt(i),char14)>1){
                                codeFound=false;
                                break;
                            }
                        }
                        if(codeFound){
                            System.out.println("Code "+char14+ " found after "+count+" chars read");
                        }
                    }
                }
            }
        }         
        catch(IOException ex)
        {

        }
    }
    static int countCharInString(char c, String s)
    {
        int counter=0;
        int i;
        for(i=0;i<s.length();i++){
            if(s.charAt(i)==c) counter++;
        }
        return counter;
    }
}
