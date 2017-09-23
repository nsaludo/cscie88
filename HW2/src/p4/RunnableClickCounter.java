package p4;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class RunnableClickCounter implements Runnable {

	private int clickCounterId;
	private String threadFileName;
	private ConcurrentHashMap<String, ConcurrentHashMap<String, CopyOnWriteArrayList<String>>> userClicksMap;
	
	public RunnableClickCounter(int clickCounterId, String basefileName, ConcurrentHashMap<String, ConcurrentHashMap<String, CopyOnWriteArrayList<String>>> userClicksMap) {
		this.clickCounterId = clickCounterId;
		threadFileName = basefileName +  clickCounterId + "_events.txt";
		
		this.userClicksMap = userClicksMap;
	}

	@Override
	public void run() {
		
		System.out.println(threadFileName);
		try (
			BufferedReader br = Files.newBufferedReader( Paths.get(threadFileName))) {
			String sCurrentLine;
			String[] fields; 
			while ((sCurrentLine = br.readLine()) != null) {
				fields = sCurrentLine.split(" ");
				if (userClicksMap.isEmpty() || !userClicksMap.containsKey(fields[1])) {
					ConcurrentHashMap<String, CopyOnWriteArrayList<String>> submap = new ConcurrentHashMap<String, CopyOnWriteArrayList<String>>();
					CopyOnWriteArrayList<String> sublist =  new CopyOnWriteArrayList<String>();
					sublist.add(fields[0]); // add timestamp to list
					submap.put(fields[2], sublist); // add userid to submap
					userClicksMap.put(fields[1], submap); //put URL to ConcurrentHashMap
  
				} else {
					if (!userClicksMap.get(fields[1]).containsKey(fields[2])) {
						ConcurrentHashMap<String, CopyOnWriteArrayList<String>> submap = new ConcurrentHashMap<String, CopyOnWriteArrayList<String>>();
						CopyOnWriteArrayList<String> sublist =  new CopyOnWriteArrayList<String>();
						sublist.add(fields[0]);
						submap.put(fields[2], sublist);
						userClicksMap.get(fields[1]).putAll(submap);
					} else {
						userClicksMap.get(fields[1]).get(fields[2]).add(fields[0]);
					}					  						  
				}						
			}
			System.out.println("thread [" + clickCounterId + "] counted data file OK: " + threadFileName);
		} catch (Exception e) {
			System.out.println("thread [" + clickCounterId + "]: ERROR: no data file processed: " + e.getMessage());
		}
	}

}
