package nosharingimpl;

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

	// each thread has its own input and output which is then consolidated
	private List<String> inputList;
	private Map<String, List<Integer>> accumMap;
	private int id;
	private boolean isFibonacci;

	public Runner(int id, List<String> input, Map<String, List<Integer>> accumMap, boolean isFibonacci) {
		this.inputList = input;
		this.accumMap = accumMap;
		this.id = id;
		this.isFibonacci = isFibonacci;
	}

	public void run() {
		System.out.println("Started thread" + id + " with input size" + inputList.size());
		createAccumMap(inputList, accumMap);
	}

	/**
	 * This method is called when thread.start() is called. It is used to find the
	 * average temperature per station from the given list of strings. It parses the
	 * list, eliminates records other than TMAX, and creates an accumulation data
	 * structure with sum and count for each station. It then calculates the average
	 * per station. In addition to this based on a boolean flag, it executes a
	 * fibonacci(17) function before updating the hash map.
	 * each thread has its own output. No synchronisation is needed
	 * 
	 * @param inputList
	 *            - List of strings from the input file
	 * 
	 */
	private void createAccumMap(List<String> inputList, Map<String, List<Integer>> accumMap) {
		Utilities util = new Utilities();
		for (String line : inputList) {
			if (line.split(",")[2].contains("TMAX")) {
				String[] values = line.split(",");
				String stationId = values[0];
				int temp = Integer.parseInt(values[3]);
				// If the station id is already present update the value with
				// new sum and count
				if (accumMap.containsKey(stationId)) {
					List<Integer> tempSumCount = accumMap.get(stationId);
					// calculate new sum and count
					int accumSum = tempSumCount.get(0) + temp;
					int count = tempSumCount.get(1) + 1;
					List<Integer> newTempSumCount = new ArrayList<>();
					newTempSumCount.add(accumSum);
					newTempSumCount.add(count);
					if (isFibonacci) {
						util.fibonnaci(17);
					}
					accumMap.put(stationId, newTempSumCount);
				} else {
					// key is not already present, create a new entry
					// with the parsed TMAX value and count as 1
					List<Integer> newList = new ArrayList<>();
					newList.add(temp);
					newList.add(1);
					if (isFibonacci) {
						util.fibonnaci(17);
					}
					accumMap.put(stationId, newList);
				}
			}
		}
	}

}
