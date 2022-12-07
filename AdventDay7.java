import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AdventDay7 {
	
	static class AFile {
		int size;
		String name;
		public AFile(int size, String name) {
			this.size=size;
			this.name=name;
		}
		int getSize() {
			return size;
		}
	}
	static class ADirectory {
		ArrayList <ADirectory> directories=new ArrayList();
		ArrayList <AFile> files=new ArrayList();
		String name;
		String path;
		ADirectory parent;
		int local_size=0;
		int total_size=0;
		public ADirectory(String name, ADirectory parent)
		{
			this.name=name;
			this.parent=parent;
			if(parent!=null) this.path=parent.path+"/"+parent.name;
		}
		public void addFile(AFile file) {
			// System.out.println("attempting to add file "+file.name); uncomment for debugging
			boolean alreadyExists=false;
			for(AFile f:files) {
				if (f.name.equals(file.name)) {
					System.out.println("File already exists");
					alreadyExists=true;
					break;
				}
			}
			if(!alreadyExists) {
				files.add(file);
			}
		}
		public void addDirectory(ADirectory directory)
		{
			// System.out.println("attempting to add dir "+directory.name); uncomment for debugging
			boolean alreadyExists=false;
			for(ADirectory d:directories) {
				if (d.name.equals(directory.name)) {
					System.out.println("Directory already exists");
					alreadyExists=true;
					break;
				}
			}
			if(!alreadyExists) {
				directories.add(directory);
				masterList.add(directory);
			}
		}
		public ADirectory changeDirectory(String targetDir)
		{
			// System.out.println("changing directory to "+targetDir); uncomment for debugging
			if(targetDir.equals("..")) return parent;
			else {
				for(ADirectory d:directories) {
					if (d.name.equals(targetDir)) return d;
				}
				return null;
			}
		}
		public int getSize()
		{
			int size=0;
			for(AFile f:files) {
				size+=f.getSize();
			}
			for(ADirectory d:directories) {
				size+=d.getSize();
			}
			return size;
		}
		
	}
	
	static ADirectory currentDir;
	static ArrayList<ADirectory> masterList=new ArrayList();
	static BufferedReader in;    
	static String inputFileLocation="C:/users/701583673/adventday7.txt";
	public static void main(String[] args) {
		
		String nextline;
        currentDir=new ADirectory("/",null);
        String lastCommand="";
        int total=0;
        
        try {
        	in=new BufferedReader(new FileReader(inputFileLocation));
        	nextline=in.readLine(); // first line is special case to set things going, sets to root directory
        	currentDir=new ADirectory("/",null);
        	masterList.add(currentDir);
        	
        	
            while((nextline=in.readLine())!=null) {
                //System.out.println(nextline); - uncomment these for debugging
                //System.out.println("Current Directory is "+currentDir.name); // uncomment for debugging
            	if(nextline.charAt(0)=='$') {
                	// command
                	if(nextline.substring(0, 4).equals("$ ls")) {
                		lastCommand="LIST"; // in case part 2 has additional commands, like delete or move dir!
                		
                	} else if(nextline.substring(0,4).equals("$ cd")) {
                		lastCommand="CD";
                		currentDir=currentDir.changeDirectory(nextline.substring(5));
                	}
                } else {
                	if(lastCommand.equals("LIST"))
                	{
                		if(nextline.substring(0,3).equals("dir")) {
          
                			currentDir.addDirectory(new ADirectory(nextline.substring(4),currentDir));
                		} else {
                			int spaceAt=nextline.indexOf(" ");
                			int fileSize=Integer.parseInt(nextline.substring(0,spaceAt));
                			String filename=nextline.substring(spaceAt+1);
                			currentDir.addFile(new AFile(fileSize,filename));
                		}
                	}
                }
            }
            //part 1
            for(ADirectory d:masterList) {
            	System.out.println(d.path + " - "+d.name + " :: "+d.getSize());
            	if (d.getSize()<=100000) {
            		total+=d.getSize();
            		System.out.println("Total :"+total);
            	}
            }
            //part 2
            int usedSpace=masterList.get(0).getSize();
            int extraSpaceNeeded=30000000-(70000000-usedSpace);
            System.out.println("used space="+usedSpace+", space needed="+extraSpaceNeeded);
            int smallest=30000000;
            for(ADirectory d:masterList) {
            	if (d.getSize()>=extraSpaceNeeded && d.getSize()<smallest) {
            		smallest=d.getSize();
            	}
            }
            System.out.println("Smallest directory to delete is "+smallest);
            
        }         
        catch(IOException ex)
        {

        }
	}
	
}
