package p1;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class SimpleThread {
	
	public static String getCurrentTimeStamp() {
	    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
	}

    private static class RunnableRegEx implements Runnable {
    	
        public void run() {
        		String threadName = Thread.currentThread().getName();
        		String s = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB";
    			System.out.println("Thread " + threadName + " starting at " + getCurrentTimeStamp());
    			int threadProcessCounter = 0;
    			try {
    				while(true) {
    					
    					threadProcessCounter++;
            			System.out.println("Thread " + threadName + " started process " + threadProcessCounter);

    					//Sleep for 25ms
                    Thread.sleep(15);
                    // Perform a CPU intensive process
            			Pattern.compile("(A+)+").matcher(s).matches();  
                	   
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
        		//Default to 2 threads
        		numOfThreads = 2;
        }

        for (int i=0; i< numOfThreads; i++) {
        		Thread t = new Thread(new RunnableRegEx());
        		t.start();
        }
    }   
}

       