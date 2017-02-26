package com.test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
	public static void main(String[] args) {

		FileReader fileReader = null;

		try {

			fileReader = new FileReader("d:/files/sample3.txt");
			String outputLocation = "d:/files/finalres.txt";

			VehiclesTimeTableService busTimeTable = new VehiclesTimeTableService();

			busTimeTable.generateTimeTable(fileReader, outputLocation);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {

			try {

				if (fileReader != null)
					fileReader.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
