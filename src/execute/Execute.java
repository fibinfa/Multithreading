package execute;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import coarselockimpl.CoarseLock;
import coarselockimpl.CoarseLockWithFib;
import finelockimpl.FineLock;
import finelockimpl.FineLockWithFib;
import helper.Utilities;
import nolockimpl.NoLock;
import nolockimpl.NoLockWithFib;
import nosharingimpl.NoSharing;
import nosharingimpl.NoSharingWithFib;
import sequentialimpl.Sequential;
import sequentialimpl.SequentialWithFib;

public class Execute {

	/**
	 * This is the main method which executes program to calculate average TMAX
	 * temperature It also executes another method to calculate average time taken
	 * for 10 executions
	 * 
	 * @param args
	 *            Unused
	 * 
	 */
	public static void main(String[] args) {
		Utilities util = new Utilities();
		List<String> inputList = new ArrayList<String>();
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the location of the file");
		String loc = sc.next();
		try {
			inputList = util.readFile(loc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<String, Float> output = new HashMap<>();
		try {
			do {
				System.out.println();
				System.out.println("Menu");
				System.out.println("-----------");
				System.out.println("1.Sequential");
				System.out.println("2.SequentialWithFib");
				System.out.println("3.NoLock");
				System.out.println("4.NoLockWithFib");
				System.out.println("5.CoarseLock");
				System.out.println("6.CoarseLockWithFib");
				System.out.println("7.FineLock");
				System.out.println("8.FineLockWithFib");
				System.out.println("9.NoSharing");
				System.out.println("10.NoSharingWithFib");
				System.out.println("Press any other key to exit");
				System.out.println("-----------");
				System.out.println("Enter your choice:");
				int ch = sc.nextInt();
				switch (ch) {
				case 1:
					Sequential seqObj = new Sequential();
					output = seqObj.getAvgTempPerStation(inputList);
					// uncomment this line to print the averageTMAX per station
					// printOutput(output);
					seqObj.calculateTime(inputList);
					break;
				case 2:
					SequentialWithFib seqFibObj = new SequentialWithFib();
					output = seqFibObj.getAvgTempPerStation(inputList);
					// uncomment this line to print the averageTMAX per station
					// printOutput(output);
					seqFibObj.calculateTime(inputList);
					break;
				case 3:
					NoLock noLockObj = new NoLock();
					output = noLockObj.getAvgTempPerStation(inputList);
					// uncomment this line to print the averageTMAX per station
					// printOutput(output);
					noLockObj.calculateTime(inputList);
					break;
				case 4:
					NoLockWithFib noLockFibObj = new NoLockWithFib();
					output = noLockFibObj.getAvgTempPerStation(inputList);
					// uncomment this line to print the averageTMAX per station
					// printOutput(output);
					noLockFibObj.calculateTime(inputList);
					break;
				case 5:
					CoarseLock coarseLockObj = new CoarseLock();
					output = coarseLockObj.getAvgTempPerStation(inputList);
					// uncomment this line to print the averageTMAX per station
					// printOutput(output);
					coarseLockObj.calculateTime(inputList);
					break;
				case 6:
					CoarseLockWithFib coarseLockFib = new CoarseLockWithFib();
					output = coarseLockFib.getAvgTempPerStation(inputList);
					// uncomment this line to print the averageTMAX per station
					// printOutput(output);
					coarseLockFib.calculateTime(inputList);
					break;
				case 7:
					FineLock fineLockObj = new FineLock();
					output = fineLockObj.getAvgTempPerStation(inputList);
					// uncomment this line to print the averageTMAX per station
					// printOutput(output);
					fineLockObj.calculateTime(inputList);
					break;
				case 8:
					FineLockWithFib fineLockWithFib = new FineLockWithFib();
					output = fineLockWithFib.getAvgTempPerStation(inputList);
					// uncomment this line to print the averageTMAX per station
					// printOutput(output);
					fineLockWithFib.calculateTime(inputList);
					break;
				case 9:
					NoSharing noSharingObj = new NoSharing();
					output = noSharingObj.getAvgTempPerStation(inputList);
					// uncomment this line to print the averageTMAX per station
					// printOutput(output);
					noSharingObj.calculateTime(inputList);
					break;
				case 10:
					NoSharingWithFib noSharingWithFibObj = new NoSharingWithFib();
					output = noSharingWithFibObj.getAvgTempPerStation(inputList);
					// uncomment this line to print the averageTMAX per station
					// printOutput(output);
					noSharingWithFibObj.calculateTime(inputList);
					break;
				default:
					System.out.println("Exit");
					System.exit(0);
					break;
				}

			} while (true);

		} catch (InputMismatchException e) {
			System.out.println("Exit");
			System.exit(0);
		}

	}

	/**
	 * This function prints the map
	 * 
	 * @param output
	 */
	private static void printOutput(Map<String, Float> output) {
		for (String key : output.keySet()) {
			System.out.println("Average temperature of Station with id: " + key + " = " + output.get(key));
		}
	}

}
