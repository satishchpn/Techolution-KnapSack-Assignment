package com.knapsack.structure;

import java.util.ArrayList;
import java.util.List;

/**
 * This class use to keep the data as List and returns as an array
 *
 */
public class InputFileData {
	private List<Integer> amountSatisfactionList = new ArrayList<Integer>();
	private List<Integer> timeConsumedList = new ArrayList<Integer>();

	public InputFileData() {

	}

	/**
	 * Setting the amount of satisfaction and time consumed
	*/
	public void pushToList(Integer amountSatisfaction, Integer timeTaken) {
		amountSatisfactionList.add(amountSatisfaction);
		timeConsumedList.add(timeTaken);
	}

	public int[] getAmountOfSatisfactions() {
		int arr[] = new int[amountSatisfactionList.size()];
		for (int i = 0; i < amountSatisfactionList.size(); i++) {
			arr[i] = amountSatisfactionList.get(i);
		}
		return arr;
	}

	public int[] getTimeConsumed() {
		int arr[] = new int[timeConsumedList.size()];
		for (int i = 0; i < timeConsumedList.size(); i++) {
			arr[i] = timeConsumedList.get(i);
		}
		return arr;
	}
}
