package de.exxcellent.challenge;

import java.io.IOException;

/**
 * The entry class for your solution. This class is only aimed as starting point and not intended as baseline for your software
 * design. Read: create your own classes and packages as appropriate.
 *
 * @author Benjamin Schmid <benjamin.schmid@exxcellent.de>
 * @author Yannick Kaiser <yannick-kaiser@gmx.de>
 */
public final class App {

    /**
     * This is the main entry method of your program.
     * @param args The CLI arguments passed
     */
    public static void main(String... args) {

    	ChallengeTaskExecutor taskExecutor = new ChallengeTaskExecutor();
    	taskExecutor.registerTask("weather", new WeatherDataChallenge());
    	
    	try {
			taskExecutor.executeTask(args);
		} catch (IOException e) {
			e.printStackTrace();
		}

//        String dayWithSmallestTempSpread = "Someday";     // Your day analysis function call …
//        System.out.printf("Day with smallest temperature spread : %s%n", dayWithSmallestTempSpread);
//
//        String teamWithSmallestGoalSpread = "A good team"; // Your goal analysis function call …
//        System.out.printf("Team with smallest goal spread       : %s%n", teamWithSmallestGoalSpread);
    }
}
