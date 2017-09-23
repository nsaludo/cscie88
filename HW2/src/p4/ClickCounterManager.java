package p4;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ClickCounterManager {

	private ExecutorService workersThreadPoolService = null;
	private int numberOfThreads = 2; // default number of threads
	private long generationTimeoutInMS = 5000l; //default timeout for data generation in MS

	public ClickCounterManager() {
	}
	
	public void runClickCounters(String baseFileName, int numberOfDataFiles){

		ConcurrentHashMap<String, ConcurrentHashMap<String, CopyOnWriteArrayList<String>>> userClicksMap = new ConcurrentHashMap<String, ConcurrentHashMap<String,CopyOnWriteArrayList<String>>>();
		workersThreadPoolService = Executors.newFixedThreadPool(numberOfThreads);
		for (int i=0; i<numberOfDataFiles; i++) {
			workersThreadPoolService.submit(
				new RunnableClickCounter(i, baseFileName , userClicksMap));
		}
	   	System.out.println("runClickCounter(): started all click counter threads, waiting for completion");
		workersThreadPoolService.shutdown();
		try {
			workersThreadPoolService.awaitTermination(generationTimeoutInMS, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
		   	System.out.println("Got InterruptedException while shutting down click counter, aborting");
		}
	   	
		System.out.println("runClickCounter(): all threads completed");
	   	
		// Now query the userClickMaps nested ConcurrentHashMap
	   	System.out.println("Query 1: get count of unique URLs => " + userClicksMap.size());
	   	for (Entry<String, ConcurrentHashMap<String, CopyOnWriteArrayList<String>>> entry : userClicksMap.entrySet()) {
	        System.out.println("Query 2: get count of unique visitors per URL :" + 
	        entry.getKey() + " # of unique userId : " +entry.getValue().size());
	        ConcurrentHashMap<String, CopyOnWriteArrayList<String>> insidemap = entry.getValue();
	        	for (Entry<String, CopyOnWriteArrayList<String>> entry2 : insidemap.entrySet()) {
	        		System.out.println("Query 3: get count of unique (by userId) clicks per URL " + ""
		    		+ entry.getKey() + " UserId : " + entry2.getKey() +" # of Unique Clicks : "
		    		+entry2.getValue().size());
	        	}
	   	}       
	}

	public static void main(String[] args) {

		if (args.length < 2) {
			System.out.println("Usage: java ClickCounterManager <baseFileName> <numberOfDataFiles>");
			System.exit(-1);
		}		
		String baseFileName = args[0];
		int numberOfDataFiles = Integer.parseInt(args[1]);
		ClickCounterManager manager = new ClickCounterManager();		
		manager.runClickCounters(baseFileName, numberOfDataFiles);
		System.out.println("Process Completed");
	}

}
