package p1;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleThreadIO {
	
	public static String getCurrentTimeStamp() {
	    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
	}

    private static class RunnableRegEx implements Runnable {
    		
    		static void scanDirectoryContents(File dir) {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					scanDirectoryContents(file);
				} 
			}
		}
    		
        public void run() {
        		String threadName = Thread.currentThread().getName();
    			System.out.println("Thread " + threadName + " starting at " + getCurrentTimeStamp());
    			int threadProcessCounter = 0;
    			try {
    				while(true) {
    					
    					threadProcessCounter++;
            			System.out.println("Thread " + threadName + " started process " + threadProcessCounter);

                    Thread.sleep(15);
 
                    File currentDir = new File("."); 
                    scanDirectoryContents(currentDir);
            			System.out.println("Thread " + threadName + " finished process " + threadProcessCounter);

    				}
            } catch (InterruptedException e) {
            	    System.out.println("Thread " + threadName + " stopped with an error " + e.getMessage());
            }
    			System.out.println("Thread " + threadName + " finished at " + getCurrentTimeStamp());
        }
    }

    public static void main(String args[]) throws InterruptedException {

        int numOfThreads;
        
        if (args.length > 0) {
            numOfThreads = Integer.parseInt(args[0]);
        } else {
        		numOfThreads = 2;
        }

        for (int i=0; i< numOfThreads; i++) {
        		Thread t = new Thread(new RunnableRegEx());
        		t.start();
        }
    }   
}

       
