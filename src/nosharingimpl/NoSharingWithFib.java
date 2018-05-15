package nosharingimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class implements the no lock version of the program that calculates the
 * average TMAX temperature for each station from the climate data provided by
 * NOAA It takes the location of file as input and outputs the average TMAX
 * temperature along with min, max and avg running time for 10 iterations. Three
 * threads are spawned each having its own input and output. these outputs are
 * consolidated to get the final output
 * 
 * 
 * @author fibin
 *
 */
public class NoSharingWithFib {
	public Map<String, Float> getAvgTempPerStation(List<String> inputList) {
		// dividing input equally
		List<String> inputForThread1 = inputList.subList(0, inputList.size() / 3);
		List<String> inputForThread2 = inputList.subList(inputList.size() / 3, 2 * inputList.size() / 3);
		List<String> inputForThread3 = inputList.subList(2 * inputList.size() / 3, inputList.size());
		// three different outputs
		Map<String, List<Integer>> accumMapForThread1 = new HashMap<>();
		Map<String, List<Integer>> accumMapForThread2 = new HashMap<>();
		Map<String, List<Integer>> accumMapForThread3 = new HashMap<>();
		
		// threads are initialised with input, output and boolean flag for fibonacci
		Thread t1 = new Thread(new Runner(1, inputForThread1, accumMapForThread1, true));
		Thread t2 = new Thread(new Runner(2, inputForThread2, accumMapForThread2, true));
		Thread t3 = new Thread(new Runner(3, inputForThread3, accumMapForThread3, true));

		t1.start();
		t2.start();
		t3.start();
		try {
			// wait till all threads finish execution
			t1.join();
			t2.join();
			t3.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Merging output of three hashmaps
		Map<String, Float> output = new HashMap<>();

		// created a set of output keys from all threads
		Set<String> keySetForOutput = new HashSet<>();
		keySetForOutput.addAll(accumMapForThread1.keySet());
		keySetForOutput.addAll(accumMapForThread2.keySet());
		keySetForOutput.addAll(accumMapForThread3.keySet());

		for (String stationId : keySetForOutput) {
			int sum = 0, count = 0;
			List<Integer> tempSumCount = new ArrayList<>();
			if (accumMapForThread1.containsKey(stationId)) {
				tempSumCount = accumMapForThread1.get(stationId);
				sum += tempSumCount.get(0);
				count += tempSumCount.get(1);
				tempSumCount.clear();
			}
			if (accumMapForThread2.containsKey(stationId)) {
				tempSumCount = accumMapForThread2.get(stationId);
				sum += tempSumCount.get(0);
				count += tempSumCount.get(1);
				tempSumCount.clear();
			}
			if (accumMapForThread3.containsKey(stationId)) {
				tempSumCount = accumMapForThread3.get(stationId);
				sum += tempSumCount.get(0);
				count += tempSumCount.get(1);
				tempSumCount.clear();
			}
			// outputing value to hashmap
			output.put(stationId, (float) sum / count);
		}

		return output;
	}

	/**
	 * This executes the getAverageTemp method 10 times and stores the execution
	 * time in an array. It prints the average, min and max execution time for 10
	 * repetitions
	 * 
	 * @param inputList
	 *            - List of lines of file
	 */
	public void calculateTime(List<String> inputList) {
		long[] executionTimeSeq = new long[10];
		for (int i = 0; i < 10; i++) {
			long start = System.currentTimeMillis();
			getAvgTempPerStation(inputList);
			long end = System.currentTimeMillis();
			// execution time is calculated and stored in an array
			executionTimeSeq[i] = end - start;
		}

		long max = Long.MIN_VALUE;
		long min = Long.MAX_VALUE;
		long sum = 0;

		for (long execTime : executionTimeSeq) {
			sum += execTime;
			if (max < execTime)
				max = execTime;
			if (min > execTime)
				min = execTime;
		}
		System.out.println("Average = " + (float) sum / 10 + "ms");
		System.out.println("Max = " + max + "ms");
		System.out.println("Min = " + min + "ms");
	}
}
