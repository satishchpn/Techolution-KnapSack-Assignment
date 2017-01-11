package com.knapsack.serviceImpl;

import java.io.File;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.knapsack.dao.KnapsackDao;
import com.knapsack.service.KnapsackService;
import com.knapsack.structure.InputFileData;

/**
 * Service class for logic of problem.
 * 
 * @author Satish
 *
 */
@Service
@Scope("prototype")
public class KnapsackServiceImpl implements KnapsackService{
	private static Logger logger = Logger.getLogger(KnapsackServiceImpl.class);

	@Autowired(required=true)
	private KnapsackDao knapsackDao;

	/**
	 * This will keep File Data.
	 */
	private InputFileData fileData;

	/**
	 * Used for taking input and displaying output.
	 * 
	 */
	public void findMaximumSatisfaction(String inputFile) {
		logger.info("findMaximumSatisfaction(-) called...");
		Scanner input = null;
		try {
			File file = null;

			if ((file = validateDataFileExist(inputFile)) == null) {
				return;
			}

			fileData = knapsackDao.getDataFromFile(file);

			int givenTime = 0;
			char continueExecution = 'Y';
			input = new Scanner(System.in);
			do {
				System.out.print("Enter Given Time : ");
				givenTime = input.nextInt();
				int maximumSatisfactionValue = knapsack(fileData.getAmountOfSatisfactions(), fileData.getTimeConsumed(),
						givenTime);
				System.out.println("Maximum Satisfaction Value : " + maximumSatisfactionValue);
				System.out.println("\n");
				System.out.print("Do you want to continue y/n? ");
				continueExecution = input.next().charAt(0);
			} while (continueExecution == 'y' || continueExecution == 'Y');
		} catch (Exception e) {
			logger.error("Exception : ", e);
		} finally {
			if (input != null)
				input.close();
		}
	}

	/**
	 * knapsack logic
	 */
	private int knapsack(int amountSatisfactions[], int timeTaken[], int givenTime) {
		// Get the total number of items. Could be timeTaken.length or
		// amountSatisfactions.length. Doesn't matter
		int N = timeTaken.length;

		// Create a matrix. Items are in rows and weight at in columns +1 on
		// each side
		int[][] V = new int[N + 1][givenTime + 1];

		// What if the knapsack's capacity is 0 - Set all columns at row 0 to be
		// 0
		for (int col = 0; col <= givenTime; col++) {
			V[0][col] = 0;
		}

		// What if there are no items at home. Fill the first row with 0
		for (int row = 0; row <= N; row++) {
			V[row][0] = 0;
		}

		for (int item = 1; item <= N; item++) {
			// Let's fill the values row by row
			for (int time = 1; time <= givenTime; time++) {
				// Is the current items time less than or equal to running time
				if (timeTaken[item - 1] <= time) {
					// Given a time, check if the value of the current item +
					// value of the item that we could afford with the remaining
					// time
					// is greater than the value without the current item itself
					V[item][time] = Math.max(amountSatisfactions[item - 1] + V[item - 1][time - timeTaken[item - 1]],
							V[item - 1][time]);
				} else {
					// If the current item's time is more than the running time,
					// just carry forward the value without the current item
					V[item][time] = V[item - 1][time];
				}
			}
		}
		return V[N][givenTime];
	}

	private File validateDataFileExist(String fileName) {
		try {
			if (fileName == null || fileName.trim().length() <= 0) {
				logger.error("PLease give File Name");
				return null;
			}

			File file = new File(fileName);
			if (file.exists()) {
				return file;
			}
			logger.error("File Doesn't exist.");
			return null;
		} catch (Exception ex) {
			logger.error("Validation Failed:", ex);
		}
		return null;
	}
}
