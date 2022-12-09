import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class AdventDay9 {
	static BufferedReader in;    
	static String inputFileLocation="C:/users/701583673/adventDay9.txt";
	static boolean debugging=false; // set to true if you want reports on the snake segments moving
	public static void main(String[] args) {
		String nextline;
		char direction;
		int numMoves=0;
		
		//initialise the snake
		int snakeLength=10; // set to 2 for first half of puzzle ### NOTE not tested since coding part 2
		SnakeHead head=new SnakeHead(0,0);
		Snake lastSegment=head;
		snakeLength--;
		while(snakeLength>1) {
			SnakeBody body=new SnakeBody(0,0,lastSegment); // add segments to the snake, make sure tail and head set for each
			lastSegment.setTail(body);
			snakeLength--;
			lastSegment=body;
		}
		
		SnakeTail tail=new SnakeTail(0,0,lastSegment); // add final segment to snake (the tail segment)
		lastSegment.setTail(tail); // point the last bit of body to the tail (for cascading move commands)
		
		try {
			in=new BufferedReader(new FileReader(inputFileLocation));
			while((nextline=in.readLine())!=null) {
	            if(debugging) System.out.println(nextline);
				direction=nextline.charAt(0);
	            numMoves=Integer.parseInt(nextline.substring(2));
	            while(numMoves>0) {
	            	head.move(direction);
	            	numMoves--;
	            }
	        }
			System.out.println("Tail spaces visited="+tail.getSpacesVisited());
	
		} catch (IOException ex) {
			
		}
	}

}
