package p3;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LoggerThread {
	
	// Create unique timestamp for each event
	public static String getCurrentTimeStamp(int addTime) {
		Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
		calendar.add(Calendar.MILLISECOND, addTime);
	    return new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss.SSS").format(calendar.getTimeInMillis());
	}

    private static class RunnableLogger implements Runnable {
    	
    		private int numOfUsers;
        private String listOfURLs [];
        private int numOfEvents;
        
        public RunnableLogger(int numOfUsers, String[] listOfURLs, int numOfEvents) {
			this.numOfUsers = numOfUsers;
    			this.listOfURLs = listOfURLs;
    			this.numOfEvents = numOfEvents;
        }
    	
        public void run() {
        		String threadName = Thread.currentThread().getName();
    			System.out.println("Thread " + threadName + " starting at " + getCurrentTimeStamp(0));
    			String threadFileName = threadName + "_events.txt";

    			try (BufferedWriter bw = new BufferedWriter(new FileWriter(threadFileName))) {

    				for (int i=0; i<numOfUsers; i++) {
    					int userId = 1000 + i;
    					for (int j=0; j<listOfURLs.length; j++) {
    						for (int l=0; l< numOfEvents; l++) {
    			                bw.write(getCurrentTimeStamp(l) + " " + listOfURLs[j] + " " + userId +"\n");
    						}
    						
    					}
    				}

    	        } catch (IOException e) {
            	    System.out.println("Thread " + threadName + " stopped with an error " + e.getMessage());
    	        }
    			System.out.println("Thread " + threadName + " finished at " + getCurrentTimeStamp(0));

    				
        }

    }

    public static void main(String args[]) throws InterruptedException {
        
        if (args.length < 4) {
        		System.out.println("Usage: java LoggerThread <numOfUsers> <listOfURLs> <numOfEvents> <numberOfThreads>");
			System.exit(-1);
        }
        
        int numOfUsers = Integer.parseInt(args[0]);
        String [] listOfURLs = args[1].split(",");;
        int numOfEvents = Integer.parseInt(args[2]);
        int numOfThreads = Integer.parseInt(args[3]);

        for (int i=0; i< numOfThreads; i++) {
        		Thread t = new Thread(new RunnableLogger(numOfUsers, listOfURLs, numOfEvents));
        		t.start();
        }
    }   
}
