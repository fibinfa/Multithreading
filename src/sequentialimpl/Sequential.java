package sequentialimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements the sequential version of the program that calculates
 * the average TMAX temperature for each station from the climate data provided
 * by NOAA It implements two methods which outputs the average TMAX
 * temperature along with min, max and avg running time for 10 iterations
 * 
 * @author fibin
 *
 */
public class Sequential {

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
		// accumulation data structure with station id and corresponding accumulated
		// sum and count of TMAX
		Map<String, List<Integer>> accumMap = new HashMap<>();
		List<Integer> tempSumCount = new ArrayList<>();
		for (String line : inputList) {
			if (line.split(",")[2].contains("TMAX")) {
				String[] values = line.split(",");
				String stationId = values[0];
				int temp = Integer.parseInt(values[3]);
				// If the station id is already present update the value with
				// new sum and count
				if (accumMap.containsKey(stationId)) {
					tempSumCount = accumMap.get(stationId);
					// calculate new sum and count
					int accumSum = tempSumCount.get(0) + temp;
					int count = tempSumCount.get(1) + 1;
					tempSumCount.clear();
					tempSumCount.add(accumSum);
					tempSumCount.add(count);
					accumMap.put(stationId, tempSumCount);
				} else {
					// key is not already present, create a new entry
					// with the parsed temperature and count as 1
					List<Integer> newList = new ArrayList<>();
					newList.add(temp);
					newList.add(1);
					accumMap.put(stationId, newList);
				}
			}
		}

		// calculate average temperature per station
		Map<String, Float> output = new HashMap<>();
		for (String key : accumMap.keySet()) {
			List<Integer> sumCount = accumMap.get(key);
			// average = accumulated sum/ accumulated count
			output.put(key, (float) sumCount.get(0) / sumCount.get(1));
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
			// execution time is calcuated and stored in an array
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
