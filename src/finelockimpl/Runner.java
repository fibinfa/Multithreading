package finelockimpl;

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
	Object dummyObject;

	public Runner(List<String> input, int id, Map<String, List<Integer>> accumMapShared, boolean isFibonacci,
			Object dummyObject) {
		this.id = id;
		this.input = input;
		this.accumMapShared = accumMapShared;
		this.isFibonacci = isFibonacci;
		this.dummyObject = dummyObject;
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
		System.out.println("Started thread" + id + " with input size" + input.size());
		for (String line : input) {
			if (line.split(",")[2].contains("TMAX")) {
				String[] values = line.split(",");
				String stationId = values[0];
				int temp = Integer.parseInt(values[3]);
				// If the station id is already present update the value with
				// new sum and count
				if (accumMapShared.containsKey(stationId)) {
					List<Integer> tempSumCount = new ArrayList<>();
					// synchronise the if and else block separately with two different
					// objects so that both can be executed parallelly by two threads
					// here the lock is put not on the entire map object
					// but the value of that stationId in the map
					synchronized (accumMapShared.get(stationId)) {
						tempSumCount = accumMapShared.get(stationId);
						// calculate new sum and count
						int accumSum = tempSumCount.get(0) + temp;
						int count = tempSumCount.get(1) + 1;
						// tempSumCount.clear();
						List<Integer> newTempSumCount = new ArrayList<>();
						newTempSumCount.add(accumSum);
						newTempSumCount.add(count);
						if (isFibonacci) {
							util.fibonnaci(17);
						}
						accumMapShared.put(stationId, newTempSumCount);
					}
				} else {
					// key is not already present, create a new entry
					// with the parsed TMAX value and count as 1
					// here a dummy object is shared between multiple threads
					// and it is locked, so that two threads can simultaneously execute if and
					// else .also two threads simultaneously accessing the
					// else block is handled by again checking whether key is already present once
					// again 
					synchronized (dummyObject) {
						if (accumMapShared.containsKey(stationId)) {
							List<Integer> tempSumCount = new ArrayList<>();
							// here the lock is put not on the entire map object
							// but the value of that stationId in the map
							synchronized (accumMapShared.get(stationId)) {
								tempSumCount = accumMapShared.get(stationId);
								// calculate new sum and count
								int accumSum = tempSumCount.get(0) + temp;
								int count = tempSumCount.get(1) + 1;
								// tempSumCount.clear();
								List<Integer> newTempSumCount = new ArrayList<>();
								newTempSumCount.add(accumSum);
								newTempSumCount.add(count);
								if (isFibonacci) {
									util.fibonnaci(17);
								}
								accumMapShared.put(stationId, newTempSumCount);
							}
						} else {
							List<Integer> newList = new ArrayList<>();
							newList.add(temp);
							newList.add(1);
							if (isFibonacci) {
								util.fibonnaci(17);
							}
							accumMapShared.put(stationId, newList);
						}
					}
				}
			}
		}

	}

}
