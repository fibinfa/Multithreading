package coarselockimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements the no lock version of the program that calculates the
 * average TMAX temperature for each station from the climate data provided by
 * NOAA It takes the location of file as input and outputs the average TMAX
 * temperature along with min, max and avg running time for 10 iterations. Three
 * threads are spawned and they are synchronised over the entire data structure
 * performance is compensated for accuracy of data
 * 
 * @author fibin
 *
 */
public class CoarseLock {
	// accumMapShared - HashMap that stores the accum sum and count per station.
	// Shared between
	// all threads
	Map<String, List<Integer>> accumMapShared = new HashMap<>();

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

	/**
	 * This method is used to find the average temperature per station from the
	 * given list of strings. It parses the list, eliminates records other than
	 * TMAX, and creates an accumulation data structure with sum and count for each
	 * station. It then calculates the average per station.
	 * 
	 * @param inputList
	 *            - List of strings from the input file
	 * @return map - Returns a map with average TMAX temperature per station
	 */
	public Map<String, Float> getAvgTempPerStation(List<String> inputList) {
		// dividing the input equally for three threads
		List<String> inputForThread1 = inputList.subList(0, inputList.size() / 3);
		List<String> inputForThread2 = inputList.subList(inputList.size() / 3, 2 * inputList.size() / 3);
		List<String> inputForThread3 = inputList.subList(2 * inputList.size() / 3, inputList.size());

		// initialise threads with input, thread id, shared output and
		// boolean(isFibonacci)
		Thread t1 = new Thread(new Runner(inputForThread1, 1, accumMapShared, false));
		Thread t2 = new Thread(new Runner(inputForThread2, 2, accumMapShared, false));
		Thread t3 = new Thread(new Runner(inputForThread3, 3, accumMapShared, false));

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

		// calculate average temperature per station
		Map<String, Float> output = new HashMap<>();
		for (String key : accumMapShared.keySet()) {
			List<Integer> sumCount = accumMapShared.get(key);
			// average = accumulated sum/ accumulated count
			output.put(key, (float) sumCount.get(0) / sumCount.get(1));
		}

		return output;
	}

}
