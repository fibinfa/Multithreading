package coarselockimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import helper.Utilities;

/**
 * This is class which implements Runnable interface. It overrides the run
 * method which is executed when start() is called. It has a shared data
 * structure shared between all threads.
 * 
 * @author fibin
 *
 */
public class Runner implements Runnable {

	List<String> input;
	int id;
	Map<String, List<Integer>> accumMapShared;
	boolean isFibonacci;

	public Runner(List<String> input, int id, Map<String, List<Integer>> accumMapShared, boolean isFibonacci) {
		this.id = id;
		this.input = input;
		this.accumMapShared = accumMapShared;
		this.isFibonacci = isFibonacci;
	}

	/**
	 * This method is called when thread.start() is called. It is used to find the
	 * average temperature per station from the given list of strings. It parses the
	 * list, eliminates records other than TMAX, and creates an accumulation data
	 * structure with sum and count for each station. It then calculates the average
	 * per station. In addition to this based on a boolean flag, it executes a
	 * fibonacci(17) function before updating the hash map.
	 * 
	 * @param inputList
	 *            - List of strings from the input file
	 */
	public void run() {
		Utilities util = new Utilities();
		// System.out.println("Started thread"+id+" with input size" + input.size());
		for (String line : input) {
			if (line.split(",")[2].contains("TMAX")) {
				String[] values = line.split(",");
				String stationId = values[0];
				int temp = Integer.parseInt(values[3]);
				// If the station id is already present update the value with
				// new sum and count
				//here entire data structure is locked to avoid concurrent updating by multiple threads
				synchronized (accumMapShared) {
					if (accumMapShared.containsKey(stationId)) {
						List<Integer> tempSumCount = new ArrayList<>();
						tempSumCount = accumMapShared.get(stationId);
						// calculate new sum and count
						int accumSum = tempSumCount.get(0) + temp;
						int count = tempSumCount.get(1) + 1;
						tempSumCount.clear();
						tempSumCount.add(accumSum);
						tempSumCount.add(count);
						if (isFibonacci) {
							util.fibonnaci(17);
						}
						accumMapShared.put(stationId, tempSumCount);
					} else {
						// key is not already present, create a new entry
						// with the parsed temperature and count as 1
						List<Integer> newList = new ArrayList<>();
						newList.add(temp);
						newList.add(1);
						 if(isFibonacci) {
						 util.fibonnaci(17);
						 }
						accumMapShared.put(stationId, newList);
					}
				}
			}
		}

	}

}
