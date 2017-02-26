package com.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.TreeSet;

public class VehiclesTimeTableService {
	private TreeSet<Vehicle> jaiBusServiceList = new TreeSet<>();
	private TreeSet<Vehicle> veeruBusServiceList = new TreeSet<>();

	/**
	 * Reads original timetable, compare the services and generates the output
	 * timetable
	 */
	public void generateTimeTable(FileReader fileReader, String outputLocation) {

		getOriginalTimeTable(fileReader);

		checkTimeTableOfServices();

		FileWriter fileWriter = null;

		BufferedWriter bufferedWriter = null;

		try {
			fileWriter = new FileWriter(outputLocation);
			bufferedWriter = new BufferedWriter(fileWriter);
			Iterator<Vehicle> iterator;
			boolean isJaiServiceLst = false;
			if (jaiBusServiceList != null && jaiBusServiceList.size() > 0) {
				isJaiServiceLst = true;
				iterator = jaiBusServiceList.iterator();
				while (iterator.hasNext()) {
					bufferedWriter.write(iterator.next().toString());
					bufferedWriter.newLine();
				}
			}

			if (veeruBusServiceList != null && veeruBusServiceList.size() > 0) {

				if (isJaiServiceLst) {
					bufferedWriter.newLine();
				}

				iterator = veeruBusServiceList.iterator();

				while (iterator.hasNext()) {
					bufferedWriter.write(iterator.next().toString());
					bufferedWriter.newLine();
				}
			}

			System.out.println("File Created to  " + outputLocation);

		} catch (IOException | IllegalArgumentException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedWriter != null)
					bufferedWriter.close();

				if (fileWriter != null)
					fileWriter.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Reads the original timetable and split each service in Company Name
	 * Departure Time Arrival Time
	 * 
	 */
	private void getOriginalTimeTable(FileReader fileReader) {
		BufferedReader bufferedReader = null;
		String line = null;
		try {
			bufferedReader = new BufferedReader(fileReader);
			while ((line = bufferedReader.readLine()) != null) {
				String service[] = line.split(" ");
				String companyName = service[0];
				boolean isJai = true;
				if (companyName.equals("Veeru")) {
					isJai = false;
				}
				setServiceDTO(service, isJai);
			}

		} catch (IOException | IllegalArgumentException e) {
			e.printStackTrace();
		} finally {

			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Generates the separate sorted list with respect to departure time of
	 * services. Removes the service if any service longer than an hour.
	 */
	private void setServiceDTO(String service[], boolean isJai) {

		String companyName = service[0];

		String depTime[] = service[1].split(":");

		LocalTime departureTime = LocalTime.of(Integer.parseInt(depTime[0]), Integer.parseInt(depTime[1]));

		String arrTime[] = service[2].split(":");

		LocalTime arrivalTime = LocalTime.of(Integer.parseInt(arrTime[0]), Integer.parseInt(arrTime[1]));

		long netDuration = arrivalTime.toSecondOfDay() - departureTime.toSecondOfDay();

		if (netDuration > 60 * 60)
			return;

		TreeSet<Vehicle> busServiceList;

		if (isJai) {
			busServiceList = jaiBusServiceList == null ? jaiBusServiceList = new TreeSet<>() : jaiBusServiceList;
		} else {
			busServiceList = veeruBusServiceList == null ? veeruBusServiceList = new TreeSet<>() : veeruBusServiceList;
		}

		Vehicle busService = new Vehicle();

		busService.setCompanyName(companyName);
		busService.setDepartureTime(departureTime);
		busService.setArrivalTime(arrivalTime);

		busServiceList.add(busService);
	}

	/**
	 * Loop through the both services and checks for inefficient services.
	 */
	private void checkTimeTableOfServices() {

		if (jaiBusServiceList != null && jaiBusServiceList.size() > 0 && veeruBusServiceList != null
				&& veeruBusServiceList.size() > 0) {

			Object jaiBusService[] = jaiBusServiceList.toArray();

			Object veeruBusService[] = veeruBusServiceList.toArray();

			Vehicle jaiService;

			Vehicle veeruService;

			int result;

			for (int i = 0; i < jaiBusService.length; i++) {

				jaiService = (Vehicle) jaiBusService[i];

				if (jaiService == null)
					continue;

				for (int j = 0; j < veeruBusService.length; j++) {

					veeruService = (Vehicle) veeruBusService[j];

					if (veeruService == null)
						continue;

					result = compareAndRemoveInefficentService(jaiService, veeruService);

					if (result == 1) {
						jaiBusService[i] = null;
						break;
					} else if (result == 2) {
						veeruBusService[j] = null;
						continue;
					}
				}
			}
		}
	}

	/**
	 * Compares the services. Only efficient services shall be added to the
	 * timetable. A service is considered efficient compared to the other one:
	 */

	private int compareAndRemoveInefficentService(Vehicle jaiService, Vehicle veeruService) {

		LocalTime jaiBusDepTime = jaiService.getDepartureTime();

		LocalTime jaiBusArrTime = jaiService.getArrivalTime();

		LocalTime veeruBusDepTime = veeruService.getDepartureTime();

		LocalTime veeruBusArrTime = veeruService.getArrivalTime();

		if (jaiBusDepTime.compareTo(veeruBusDepTime) == 0 && jaiBusArrTime.compareTo(veeruBusArrTime) == 0) {
			veeruBusServiceList.remove(veeruService);
			return 2;
		}

		int result;

		if (jaiBusDepTime.compareTo(veeruBusDepTime) == 0) {

			result = jaiBusArrTime.compareTo(veeruBusArrTime);

			if (result == 1) {

				jaiBusServiceList.remove(jaiService);

				return 1;
			} else {

				veeruBusServiceList.remove(veeruService);

				return 2;
			}
		}

		if (jaiBusArrTime.compareTo(veeruBusArrTime) == 0) {
			result = jaiBusDepTime.compareTo(veeruBusDepTime);

			if (result == 1) {

				veeruBusServiceList.remove(veeruService);

				return 2;
			} else {

				jaiBusServiceList.remove(jaiService);

				return 1;
			}
		}

		if ((jaiBusDepTime.isAfter(veeruBusDepTime)
				&& (jaiBusDepTime.isBefore(veeruBusArrTime) || jaiBusDepTime.compareTo(veeruBusArrTime) == 0))
				|| (veeruBusDepTime.isAfter(jaiBusDepTime) && (veeruBusDepTime.isBefore(jaiBusArrTime)
						|| veeruBusDepTime.compareTo(jaiBusArrTime) == 0))) {

			int jaiBusTotalJourneyTime = jaiBusArrTime.toSecondOfDay() - jaiBusDepTime.toSecondOfDay();

			int veeruBusTotalJourneyTime = veeruBusArrTime.toSecondOfDay() - veeruBusDepTime.toSecondOfDay();

			result = Integer.compare(jaiBusTotalJourneyTime, veeruBusTotalJourneyTime);

			if (result == 0 || result == -1) {

				veeruBusServiceList.remove(veeruService);

				return 2;
			}

			jaiBusServiceList.remove(jaiService);

			return 1;
		}

		return 0;
	}

}