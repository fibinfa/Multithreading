package helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilities class implements all methods that helps in running the main
 * application to calculate the average temperature for each station
 * 
 * @author fibin
 *
 */
public class Utilities {

	/**
	 * This method is used to read a file and return the list of lines from the file
	 * given its location
	 * 
	 * @param loc
	 *            This is the location of the file which is to be read
	 * @return List of strings which is created after reading the file line by line
	 * @throws IOException
	 *             on input error or wrong file location
	 */
	public List<String> readFile(String loc) throws IOException {
		List<String> fileOutput = new ArrayList<String>();
		try {
			File f = new File(loc);

			BufferedReader b = new BufferedReader(new FileReader(f));

			String readLine = "";

			System.out.println("Reading file using Buffered Reader");

			// Read line by line and add it to output string list
			while ((readLine = b.readLine()) != null) {
				fileOutput.add(readLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileOutput;
	}

	/**
	 * This method is used to find the fibonacci() of a number in order to slow down
	 * the update in the main program
	 * 
	 * @param n
	 *            - This is the number upto which fibonacci is calculated
	 */
	public void fibonnaci(int n) {

		int x, y, z;
		x = 0;
		y = 1;
		for (int i = 0; i <= n; i++) {
			z = x + y;
			x = y;
			y = z;
		}

	}

}
