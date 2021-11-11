package de.exxcellent.challenge;

import java.util.stream.Stream;

public class WeatherDataChallenge implements ChallengeTask {

	@Override
	public void executeTask(FixedSizeTable dataTable) {
		
		// Task:
		//
		// In `weather.csv` you’ll find the daily weather data of a single month.
	    // Read the file, then output the day number (column one `Day`) of the day with
	    // the smallest temperature spread (difference between maximum &
	    // minimum temperature of the day.)
	    // The maximum temperature is the second column `MxT`, the minimum
	    // temperature the third column `MnT`.
		
		System.out.println(dataTable);
		
		int minSpread = Integer.MAX_VALUE;
		int minSpreadDay = -1;
		
		int[] days = Stream.of(dataTable.getColumn("Day")).mapToInt(elem -> Integer.parseInt(elem)).toArray();
		int[] maxTemp = Stream.of(dataTable.getColumn("MxT")).mapToInt(elem -> Integer.parseInt(elem)).toArray();
		int[] minTemp = Stream.of(dataTable.getColumn("MnT")).mapToInt(elem -> Integer.parseInt(elem)).toArray();
		
		for (int i = 0; i < days.length; ++i) {
			int spread = maxTemp[i] - minTemp[i];
			if (spread < minSpread) {
				minSpread = spread;
				minSpreadDay = days[i];
			}
		}
		
		System.out.printf("Day with smallest temperature spread : %s%n", minSpreadDay);
	}
}
